package lab12;

/**
 * This class exists as a .java source file.
 * The bonus program will compile it at runtime, then load and modify it.
 */
public class DynamicSource {

    @RunMe
    public void hello() {
        System.out.println("Hello from dynamically compiled code!");
    }

    @RunMe
    public void doubleIt(int x) {
        System.out.println("Double of " + x + " is " + (x * 2));
    }

    public void notAnnotated() {
        System.out.println("This should not be called.");
    }
}
