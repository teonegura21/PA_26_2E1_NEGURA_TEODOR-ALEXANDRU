
public class Road {
    private String name;
    private double length;
    private Location start;
    private Location end;

    public Road(String name, double length, Location start, Location end) {
        this.name = name;
        this.length = length;
        this.start = start;
        this.end = end;
    }

    public String getName() { return name; }
    public double getLength() { return length; }
    public Location getStart() { return start; }
    public Location getEnd() { return end; }

    @Override
    public String toString() {
        return "Road{" + "name='" + name + '\'' + ", length=" + length + 
               ", start=" + start.getName() + ", end=" + end.getName() + '}';
    }
}
