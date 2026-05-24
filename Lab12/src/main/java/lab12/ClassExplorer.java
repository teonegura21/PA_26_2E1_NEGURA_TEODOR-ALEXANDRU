package lab12;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

/**
 * Homework: Class Explorer
 *
 * Scans a folder for .class files, identifies annotations,
 * displays class prototypes, and invokes annotated methods.
 *
 * ONE ENTRY POINT, ONE COMMAND:
 *   mvn exec:java -Dexec.mainClass="lab12.ClassExplorer" -Dexec.args="/path/to/folder" -q
 *
 * HOW CLASS LOADING WORKS:
 * ------------------------
 * A .class file compiled with `package lab12;` must be loaded as `lab12.ClassName`.
 * The classloader URL points to the PARENT of the package root.
 * If the file is at /tmp/test/lab12/Foo.class, the classloader URL is /tmp/test
 * and the class name is "lab12.Foo".
 */
public class ClassExplorer {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java lab12.ClassExplorer <folder-path>");
            System.out.println("Example: java lab12.ClassExplorer /home/user/myclasses");
            System.exit(1);
        }

        File folder = new File(args[0]);
        if (!folder.exists() || !folder.isDirectory()) {
            System.err.println("[ERROR] Not a valid directory: " + args[0]);
            System.exit(1);
        }

        System.out.println("========================================");
        System.out.println("  SCANNING: " + folder.getAbsolutePath());
        System.out.println("========================================\n");

        try {
            explore(folder);
        } catch (Exception e) {
            System.err.println("[ERROR] " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * The main exploration logic.
     */
    private static void explore(File folder) throws Exception {

        // --- STEP 1: Create a classloader that can load classes from the folder ---
        // The folder is the ROOT of the classpath. Classes inside package subdirs
        // will be loaded with their fully qualified names.
        URL[] urls = { folder.toURI().toURL() };
        URLClassLoader classLoader = new URLClassLoader(urls);

        // --- STEP 2: Recursively find all .class files ---
        List<File> classFiles = new ArrayList<>();
        findClassFiles(folder, classFiles);

        if (classFiles.isEmpty()) {
            System.out.println("No .class files found in this folder.");
            return;
        }

        // --- STEP 3: Separate annotations from regular classes ---
        List<Class<?>> annotations = new ArrayList<>();
        List<Class<?>> classes = new ArrayList<>();

        for (File f : classFiles) {
            // Convert file path to fully qualified class name
            // Example: /tmp/test/lab12/Foo.class -> lab12.Foo
            String className = toClassName(folder, f);

            // Load the class using our custom classloader
            Class<?> clazz = classLoader.loadClass(className);

            // isAnnotation() tells us if this is an @interface
            if (clazz.isAnnotation()) {
                annotations.add(clazz);
            } else {
                classes.add(clazz);
            }
        }

        // --- STEP 4: Print annotations found ---
        System.out.println("--- ANNOTATIONS FOUND ---");
        if (annotations.isEmpty()) {
            System.out.println("  (none)");
        } else {
            for (Class<?> ann : annotations) {
                System.out.println("  @" + ann.getSimpleName());
            }
        }
        System.out.println();

        // --- STEP 5: For each class, show prototype and invoke annotated methods ---
        System.out.println("--- CLASSES FOUND ---\n");

        for (Class<?> clazz : classes) {
            System.out.println("CLASS: " + clazz.getName());
            System.out.println("  Package: " + clazz.getPackageName());
            System.out.println("  Methods:");

            // getMethods() returns all PUBLIC methods (inherited + own)
            Method[] methods = clazz.getMethods();
            for (Method m : methods) {
                // Skip methods from Object (toString, equals, hashCode, etc.)
                if (m.getDeclaringClass() == Object.class) {
                    continue;
                }

                System.out.println("    - " + m.getReturnType().getSimpleName()
                        + " " + m.getName()
                        + "(" + params(m) + ")");

                // Check if this method has any of our discovered annotations
                for (Class<?> annType : annotations) {
                    if (m.isAnnotationPresent(annType.asSubclass(Annotation.class))) {
                        System.out.println("      [ANNOTATED with @" + annType.getSimpleName() + "]");
                        invokeMethod(clazz, m);
                    }
                }
            }
            System.out.println();
        }

        classLoader.close();
    }

    /**
     * Recursively find all .class files under a folder.
     */
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

    /**
     * Convert a .class file path to a fully qualified class name.
     *
     * Example:
     *   root = /tmp/test
     *   file = /tmp/test/lab12/Foo.class
     *   result = "lab12.Foo"
     */
    private static String toClassName(File root, File classFile) {
        String rootPath = root.getAbsolutePath();
        String filePath = classFile.getAbsolutePath();

        // Get relative path: /tmp/test/lab12/Foo.class -> lab12/Foo.class
        String relative = filePath.substring(rootPath.length() + 1);

        // Remove .class suffix and replace / with .
        relative = relative.replace(".class", "");
        relative = relative.replace(File.separator, ".");

        return relative;
    }

    /**
     * Build a string of parameter types for display.
     */
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

    /**
     * Invoke a method on a new instance of the class.
     * Handles: no args -> invoke directly
     *          one int arg -> invoke with mock value 42
     */
    private static void invokeMethod(Class<?> clazz, Method m) {
        try {
            // Create a new instance (needs no-arg constructor)
            Object instance = clazz.getDeclaredConstructor().newInstance();

            Class<?>[] paramTypes = m.getParameterTypes();

            if (paramTypes.length == 0) {
                // No arguments - just invoke
                System.out.println("      -> INVOKING (no args):");
                m.invoke(instance);

            } else if (paramTypes.length == 1 && paramTypes[0] == int.class) {
                // One int argument - use mock value 42
                System.out.println("      -> INVOKING (int arg = 42):");
                m.invoke(instance, 42);

            } else {
                // Unsupported signature
                System.out.println("      -> SKIPPED (unsupported params)");
            }

        } catch (Exception e) {
            System.out.println("      -> FAILED: " + e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }
}
