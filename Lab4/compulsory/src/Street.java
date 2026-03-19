
import java.util.Objects;

public class Street implements Comparable<Street> {
    private String name;
    private int length;
    private Intersection start;
    private Intersection end;

    public Street(String name, int length, Intersection start, Intersection end) {
        this.name = name;
        this.length = length;
        this.start = start;
        this.end = end;
    }

    public String getName() { return name; }
    public int getLength() { return length; }
    public Intersection getStart() { return start; }
    public Intersection getEnd() { return end; }

    @Override
    public int compareTo(Street other) {
        return Integer.compare(this.length, other.length);
    }

    @Override
    public String toString() {
        return "Street{name='" + name + "', length=" + length + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Street street = (Street) o;
        return length == street.length && Objects.equals(name, street.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, length);
    }
}
