package lab12;

/**
 * Another class with a run() method.
 * Same method name, different behavior - discovered at runtime.
 */
public class AnotherTest {

    public void run() {
        System.out.println("Greetings from AnotherTest!");
        for (int i = 1; i <= 3; i++) {
            System.out.println("  Count: " + i);
        }
    }
}
