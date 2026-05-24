package lab12;

import java.lang.reflect.Method;

/**
 * Lab 12 - Reflection
 *
 * This application loads a class by name and invokes its run() method.
 *
 * HOW IT WORKS (step by step):
 * ---------------------------
 * 1. We receive a class name as a command-line argument.
 * 2. We ask the JVM to load that class into memory (Class.forName).
 * 3. We inspect the class to find a method called "run" with no arguments.
 * 4. If found, we invoke it.
 *
 * WHAT IS REFLECTION?
 * ------------------
 * Normally you write:   MyClass obj = new MyClass(); obj.run();
 * With reflection:      You don't know the class at compile time.
 *                       You discover and call methods at runtime.
 */
public class ReflectionRunner {

    public static void main(String[] args) {
        // We need exactly one argument: the fully-qualified class name
        // Example: lab12.TestClass
        if (args.length != 1) {
            System.out.println("Usage: java lab12.ReflectionRunner <fully.qualified.ClassName>");
            System.out.println("Example: java lab12.ReflectionRunner lab12.TestClass");
            System.exit(1);
        }

        String className = args[0];

        try {
            // STEP 1: Load the class into memory
            // Class.forName() triggers the JVM classloader to find and load the .class file
            // This returns a Class object - a "mirror" that describes everything about the class
            Class<?> clazz = Class.forName(className);
            System.out.println("[OK] Loaded class: " + clazz.getName());

            // STEP 2: Find the "run" method with NO arguments
            // getMethod(name, paramTypes...) searches public methods
            // We pass no param types to find run()
            Method runMethod = clazz.getMethod("run");
            System.out.println("[OK] Found method: " + runMethod);

            // STEP 3: Create an instance of the class
            // newInstance() calls the no-arg constructor
            // This is like doing: Object obj = new SomeClass();
            Object instance = clazz.getDeclaredConstructor().newInstance();
            System.out.println("[OK] Created instance: " + instance.getClass().getSimpleName());

            // STEP 4: Invoke the method on the instance
            // invoke(object, args...) - pass null for args since run() takes none
            System.out.println("[OK] Invoking run()...");
            System.out.println("--- OUTPUT FROM run() ---");
            runMethod.invoke(instance);
            System.out.println("--- END OUTPUT ---");

        } catch (NoSuchMethodException e) {
            System.err.println("[ERROR] Class '" + className + "' does not have a public run() method.");
        } catch (ClassNotFoundException e) {
            System.err.println("[ERROR] Class '" + className + "' not found in classpath.");
        } catch (Exception e) {
            System.err.println("[ERROR] " + e.getClass().getSimpleName() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}
