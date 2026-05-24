package lab12;

/**
 * A simple class with a run() method.
 * The ReflectionRunner will discover and invoke this at runtime.
 */
public class TestClass {

    public void run() {
        System.out.println("Hello from TestClass!");
        System.out.println("This method was discovered and invoked via reflection.");
    }
}
