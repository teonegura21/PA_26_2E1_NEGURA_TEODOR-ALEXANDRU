package lab12;

/**
 * A class with methods annotated with @RunMe.
 * The homework tool will find these and invoke them.
 */
public class AnnotatedClass {

    @RunMe
    public void sayHello() {
        System.out.println("  -> Hello from sayHello()!");
    }

    @RunMe
    public void countTo(int n) {
        System.out.println("  -> Counting to " + n + ":");
        for (int i = 1; i <= n; i++) {
            System.out.println("     " + i);
        }
    }

    // This method is NOT annotated - it will be listed but not invoked
    public void ignoredMethod() {
        System.out.println("  -> You should not see this!");
    }

    // Private method - not visible to getMethods(), ignored
    private void secret() {
        System.out.println("  -> Secret!");
    }
}
