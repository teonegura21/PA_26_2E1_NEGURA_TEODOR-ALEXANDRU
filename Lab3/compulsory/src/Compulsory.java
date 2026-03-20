import java.util.*;

public class Compulsory {
    public static void main(String[] args) {
        List<Profile> profiles = new ArrayList<>();
        
        profiles.add(new Person("Alice", 25));
        profiles.add(new Person("Bob", 30));
        profiles.add(new Person("Charlie", 22));
        profiles.add(new Company("Google"));
        profiles.add(new Company("Microsoft"));
        profiles.add(new Company("Apple"));

        System.out.println("Before sorting:");
        for (Profile p : profiles) {
            System.out.println(p);
        }

        Collections.sort(profiles);

        System.out.println("\nAfter sorting by name:");
        for (Profile p : profiles) {
            System.out.println(p);
        }
    }
}
