
public class Company implements Comparable<Company> {
    private String name;

    public Company(String name) {
        this.name = name;
    }

    public String getName() { return name; }

    @Override
    public int compareTo(Company other) {
        return this.name.compareTo(other.name);
    }

    @Override
    public String toString() {
        return "Company{name='" + name + "'}";
    }
}
