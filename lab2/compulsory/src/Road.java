package lab2.compulsory.src;

public class Road {
    private String type;
    private double length;
    private double speedLimit;
    private Location from;
    private Location to;

    public Road(String type, double length, double speedLimit, Location from, Location to) {
        double minLength = Math.sqrt(Math.pow(to.getX() - from.getX(), 2) + Math.pow(to.getY() - from.getY(), 2));
        if (length < minLength) {
            throw new IllegalArgumentException("Length cannot be less than Euclidean distance: " + minLength);
        }
        this.type = type;
        this.length = length;
        this.speedLimit = speedLimit;
        this.from = from;
        this.to = to;
    }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public double getLength() { return length; }
    public void setLength(double length) { this.length = length; }

    public double getSpeedLimit() { return speedLimit; }
    public void setSpeedLimit(double speedLimit) { this.speedLimit = speedLimit; }

    public Location getFrom() { return from; }
    public void setFrom(Location from) { this.from = from; }

    public Location getTo() { return to; }
    public void setTo(Location to) { this.to = to; }

    @Override
    public String toString() {
        return "Road{type='" + type + "', length=" + length + ", speedLimit=" + speedLimit +
               ", from=" + from + ", to=" + to + "}";
    }
}
