
public class BoundingBox {
    private double xMin, yMin, xMax, yMax;

    public BoundingBox() {
        this.xMin = Double.MAX_VALUE;
        this.yMin = Double.MAX_VALUE;
        this.xMax = -Double.MAX_VALUE;
        this.yMax = -Double.MAX_VALUE;
    }

    public void addPoint(double x, double y) {
        if (x < xMin) xMin = x;
        if (x > xMax) xMax = x;
        if (y < yMin) yMin = y;
        if (y > yMax) yMax = y;
    }

    public double getArea() {
        return (xMax - xMin) * (yMax - yMin);
    }

    @Override
    public String toString() {
        return "BoundingBox [" + xMin + ", " + yMin + "] - [" + xMax + ", " + yMax + "]";
    }

    public static void main(String[] args) {
        BoundingBox box = new BoundingBox();
        box.addPoint(0, 0);
        box.addPoint(10, 10);
        box.addPoint(5, 15);
        System.out.println(box);
        System.out.println("Area: " + box.getArea());
    }
}
