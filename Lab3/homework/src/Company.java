
public class Company implements Comparable<Company>, Node {
    private String name;
    private int employeeCount;

    public Company(String name, int employeeCount) {
        this.name = name;
        this.employeeCount = employeeCount;
    }

    public String getName() { return name; }
    public int getEmployeeCount() { return employeeCount; }

    @Override
    public int compareTo(Company other) {
        return this.name.compareTo(other.name);
    }

    @Override
    public String toString() {
        return "Company{name='" + name + "', employees=" + employeeCount + "}";
    }
}
