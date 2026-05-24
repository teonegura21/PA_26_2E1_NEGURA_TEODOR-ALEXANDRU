package lab12;

/**
 * Another class with @RunMe annotated methods.
 */
public class AnotherAnnotated {

    @RunMe
    public void greet() {
        System.out.println("  -> Greetings from AnotherAnnotated!");
    }

    @RunMe
    public void square(int x) {
        System.out.println("  -> " + x + " squared = " + (x * x));
    }

    public void notAnnotated() {
        System.out.println("  -> This won't run.");
    }
}
