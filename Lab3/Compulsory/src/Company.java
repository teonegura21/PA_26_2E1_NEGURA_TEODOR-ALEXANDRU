package Lab3.Compulsory.src;

public class Company implements Profile, Comparable<Company> {
    private final String id;
    private final String name;

    public Company(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int compareTo(Company other) {
        return this.name.compareTo(other.name);
    }

    @Override
    public String toString() {
        return "Company{id='" + id + "', name='" + name + "'}";
    }
}
