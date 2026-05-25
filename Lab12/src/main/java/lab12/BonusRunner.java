package lab12;

import javassist.*;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * Bonus: Compile .java files at runtime, then modify bytecode to add logging.
 *
 * WHAT THIS DOES:
 * ---------------
 * 1. Finds .java source files in a folder
 * 2. Compiles them to .class files using the Java Compiler API
 * 3. Uses Javassist to modify the bytecode:
 *    - For each @RunMe annotated method, injects a System.out.println at the start
 * 4. Loads the modified classes and invokes the annotated methods
 *
 * WHY BYTECODE MANIPULATION?
 * --------------------------
 * Normally, to add logging you'd edit the .java source and recompile.
 * With bytecode manipulation, you modify the .class file directly.
 * The JVM never sees the original - it sees your modified version.
 *
 * ONE COMMAND:
 *   mvn exec:java -Dexec.mainClass="lab12.BonusRunner" -Dexec.args="/path/to/folder" -q
 */
public class BonusRunner {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java lab12.BonusRunner <folder-path>");
            System.out.println("Example: java lab12.BonusRunner /home/user/sources");
            System.exit(1);
        }

        File sourceFolder = new File(args[0]);
        if (!sourceFolder.exists() || !sourceFolder.isDirectory()) {
            System.err.println("[ERROR] Not a valid directory: " + args[0]);
            System.exit(1);
        }

        System.out.println("========================================");
        System.out.println("  BONUS: Compile + Bytecode Modify");
        System.out.println("  Source folder: " + sourceFolder.getAbsolutePath());
        System.out.println("========================================\n");

        try {
            runBonus(sourceFolder);
        } catch (Exception e) {
            System.err.println("[ERROR] " + e.getClass().getSimpleName() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void runBonus(File sourceFolder) throws Exception {

        // --- STEP 1: Find all .java source files ---
        List<File> javaFiles = new ArrayList<>();
        findJavaFiles(sourceFolder, javaFiles);

        if (javaFiles.isEmpty()) {
            System.out.println("No .java files found.");
            return;
        }
        System.out.println("Found " + javaFiles.size() + " .java file(s).\n");

        // --- STEP 2: Compile them ---
        // We need a temp directory for compiled .class files
        File compileDir = Files.createTempDirectory("lab12compile").toFile();
        compileDir.deleteOnExit();

        boolean compiled = compileJavaFiles(javaFiles, compileDir, sourceFolder);
        if (!compiled) {
            System.err.println("Compilation failed.");
            return;
        }
        System.out.println("[OK] Compiled to: " + compileDir.getAbsolutePath() + "\n");

        // --- STEP 3: Modify bytecode with Javassist ---
        // Javassist lets us edit .class files at the bytecode level.
        // We create a ClassPool that looks in our compile directory.
        ClassPool pool = new ClassPool();
        pool.appendClassPath(compileDir.getAbsolutePath());
        // Also need the classpath so Javassist can find dependencies (like our @RunMe)
        pool.appendClassPath(new LoaderClassPath(BonusRunner.class.getClassLoader()));

        System.out.println("--- BYTECODE MODIFICATION ---");

        // Find all compiled .class files
        List<File> classFiles = new ArrayList<>();
        findClassFiles(compileDir, classFiles);

        for (File cf : classFiles) {
            String className = toClassName(compileDir, cf);
            modifyClass(pool, className, compileDir);
        }
        System.out.println();

        // --- STEP 4: Load modified classes and invoke annotated methods ---
        System.out.println("--- INVOKING MODIFIED CLASSES ---\n");

        URL[] urls = { compileDir.toURI().toURL() };
        URLClassLoader classLoader = new URLClassLoader(urls);

        // First pass: identify annotations
        List<Class<?>> annotations = new ArrayList<>();
        List<Class<?>> classes = new ArrayList<>();

        for (File cf : classFiles) {
            String className = toClassName(compileDir, cf);
            Class<?> clazz = classLoader.loadClass(className);
            if (clazz.isAnnotation()) {
                annotations.add(clazz);
            } else {
                classes.add(clazz);
            }
        }

        // Second pass: invoke annotated methods
        for (Class<?> clazz : classes) {
            System.out.println("CLASS: " + clazz.getName());

            for (Method m : clazz.getMethods()) {
                if (m.getDeclaringClass() == Object.class) continue;

                for (Class<?> annType : annotations) {
                    if (m.isAnnotationPresent(annType.asSubclass(Annotation.class))) {
                        System.out.println("  Method: " + m.getName() + "(" + params(m) + ")");
                        invokeModifiedMethod(clazz, m);
                    }
                }
            }
            System.out.println();
        }

        classLoader.close();
    }

    /**
     * STEP 2: Compile .java files using the Java Compiler API.
     *
     * ToolProvider.getSystemJavaCompiler() gives us the compiler that
     * comes with the JDK. We compile all .java files into compileDir.
     */
    private static boolean compileJavaFiles(List<File> javaFiles, File compileDir, File sourceFolder) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            System.err.println("[ERROR] No Java compiler found. Use a JDK, not a JRE.");
            return false;
        }

        // Build the command-line arguments for the compiler
        // -d specifies the output directory for .class files
        // -sourcepath tells the compiler where to find other source files
        List<String> args = new ArrayList<>();
        args.add("-d");
        args.add(compileDir.getAbsolutePath());
        args.add("-sourcepath");
        args.add(sourceFolder.getAbsolutePath());

        // Add all .java files to compile
        for (File f : javaFiles) {
            args.add(f.getAbsolutePath());
        }

        // Run the compiler
        int result = compiler.run(null, null, null, args.toArray(new String[0]));
        return result == 0;  // 0 means success
    }

    /**
     * STEP 3: Modify a class's bytecode using Javassist.
     *
     * For each @RunMe annotated method, we inject this at the very start:
     *     System.out.println("[LOG] Entering method: methodName");
     *
     * This edits the .class file directly. When the JVM loads it,
     * the method already contains the print statement.
     */
    private static void modifyClass(ClassPool pool, String className, File compileDir) throws Exception {
        CtClass ctClass = pool.get(className);

        boolean modified = false;

        // Check all methods in the class
        for (CtMethod method : ctClass.getDeclaredMethods()) {
            // Check if method has @RunMe annotation
            // Javassist uses its own annotation API
            Object[] annos = method.getAnnotations();
            boolean hasRunMe = false;
            for (Object anno : annos) {
                if (anno.toString().contains("RunMe")) {
                    hasRunMe = true;
                    break;
                }
            }

            if (hasRunMe) {
                // Inject logging at the start of the method body
                // $proceed is not needed here; we just insert before the body
                String logCode = "System.out.println(\"  [BYTECODE LOG] Entering method: " + method.getName() + "()\");";
                method.insertBefore(logCode);
                modified = true;
                System.out.println("  Modified: " + className + "." + method.getName() + "()");
            }
        }

        if (modified) {
            // Write the modified bytecode back to the .class file
            ctClass.writeFile(compileDir.getAbsolutePath());
            System.out.println("  [OK] Saved modified: " + className);
        }

        ctClass.detach();
    }

    /**
     * STEP 4: Invoke the modified method.
     * Same logic as homework: no args or one int with mock value.
     */
    private static void invokeModifiedMethod(Class<?> clazz, Method m) {
        try {
            Object instance = clazz.getDeclaredConstructor().newInstance();
            Class<?>[] paramTypes = m.getParameterTypes();

            if (paramTypes.length == 0) {
                System.out.println("  -> Invoking (no args):");
                m.invoke(instance);
            } else if (paramTypes.length == 1 && paramTypes[0] == int.class) {
                System.out.println("  -> Invoking (int arg = 99):");
                m.invoke(instance, 99);
            } else {
                System.out.println("  -> Skipped (unsupported params)");
            }
        } catch (Exception e) {
            System.out.println("  -> FAILED: " + e.getMessage());
        }
    }

    // --- Helper methods ---

    private static void findJavaFiles(File dir, List<File> result) {
        File[] files = dir.listFiles();
        if (files == null) return;
        for (File f : files) {
            if (f.isDirectory()) {
                findJavaFiles(f, result);
            } else if (f.getName().endsWith(".java")) {
                result.add(f);
            }
        }
    }

    private static void findClassFiles(File dir, List<File> result) {
        File[] files = dir.listFiles();
        if (files == null) return;
        for (File f : files) {
            if (f.isDirectory()) {
                findClassFiles(f, result);
            } else if (f.getName().endsWith(".class")) {
                result.add(f);
            }
        }
    }

    private static String toClassName(File root, File classFile) {
        String rootPath = root.getAbsolutePath();
        String filePath = classFile.getAbsolutePath();
        String relative = filePath.substring(rootPath.length() + 1);
        relative = relative.replace(".class", "");
        relative = relative.replace(File.separator, ".");
        return relative;
    }

    private static String params(Method m) {
        Class<?>[] types = m.getParameterTypes();
        if (types.length == 0) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < types.length; i++) {
            if (i > 0) sb.append(", ");
            sb.append(types[i].getSimpleName());
        }
        return sb.toString();
    }
}
