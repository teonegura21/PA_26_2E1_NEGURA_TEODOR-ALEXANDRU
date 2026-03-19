
import java.util.*;

public class Compulsory {
    public static void main(String[] args) {
        List<Person> persons = new ArrayList<>();
        persons.add(new Person("Alice", 25));
        persons.add(new Person("Bob", 30));
        persons.add(new Person("Charlie", 22));

        Collections.sort(persons);
        System.out.println("Persons: " + persons);

        Set<Company> companies = new TreeSet<>();
        companies.add(new Company("Google"));
        companies.add(new Company("Microsoft"));
        companies.add(new Company("Apple"));

        System.out.println("Companies: " + companies);
    }
}
