public class Company implements Profile {
    private String name;

    public Company(String name) {
        this.name = name;
    }

    @Override
    public String getName() { return name; }

    @Override
    public int compareTo(Profile other) {
        return this.name.compareTo(other.getName());
    }

    @Override
    public String toString() {
        return "Company{name='" + name + "'}";
    }
}
