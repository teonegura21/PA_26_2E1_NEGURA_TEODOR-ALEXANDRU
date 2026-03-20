import java.util.HashMap;
import java.util.Map;

public class Person implements Comparable<Person>, Node {
    private String name;
    private int age;
    private String birthDate;
    private Map<Node, String> relationships;

    public Person(String name, int age, String birthDate) {
        this.name = name;
        this.age = age;
        this.birthDate = birthDate;
        this.relationships = new HashMap<>();
    }

    public String getName() { return name; }
    public int getAge() { return age; }
    public String getBirthDate() { return birthDate; }
    public Map<Node, String> getRelationships() { return relationships; }

    public void addRelationship(Node node, String relationshipType) {
        relationships.put(node, relationshipType);
    }

    public int getRelationshipCount() {
        return relationships.size();
    }

    @Override
    public int compareTo(Person other) {
        return this.name.compareTo(other.name);
    }

    @Override
    public String toString() {
        return "Person{name='" + name + "', age=" + age + "}";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Person person = (Person) obj;
        return age == person.age && name.equals(person.name);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(name, age);
    }
}
