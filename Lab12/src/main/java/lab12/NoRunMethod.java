package lab12;

/**
 * This class does NOT have a run() method.
 * ReflectionRunner will detect this and report it.
 */
public class NoRunMethod {

    public void doSomething() {
        System.out.println("I exist, but I have no run() method.");
    }
}
