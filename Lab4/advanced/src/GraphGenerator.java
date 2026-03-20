import java.util.*;

/**
 * Generates random problem instances that satisfy triangle inequality.
 * Places intersections on a 2D plane and computes Euclidean distances.
 */
public class GraphGenerator {
    private Random random;

    public GraphGenerator() {
        this.random = new Random();
    }

    public GraphGenerator(long seed) {
        this.random = new Random(seed);
    }

    /**
     * Generate a random city with intersections placed on 2D plane.
     * Triangle inequality is guaranteed because we use Euclidean distances.
     */
    public City generateRandomCity(int numIntersections, int numStreets) {
        City city = new City();
        
        // Generate random points on 2D plane
        List<Point> points = new ArrayList<>();
        for (int i = 0; i < numIntersections; i++) {
            points.add(new Point(
                random.nextDouble() * 100,
                random.nextDouble() * 100,
                "Intersection_" + i
            ));
        }

        // Create complete graph with Euclidean distances (guaranteed triangle inequality)
        List<Intersection> intersections = new ArrayList<>();
        for (Point p : points) {
            intersections.add(new Intersection(p.name));
        }

        // Generate streets with distances based on Euclidean distance
        // First create a connected graph (spanning tree)
        Set<String> addedEdges = new HashSet<>();
        
        for (int i = 1; i < intersections.size(); i++) {
            int parent = random.nextInt(i);
            Point p1 = points.get(parent);
            Point p2 = points.get(i);
            int distance = (int) Math.ceil(p1.distanceTo(p2));
            
            Street street = new Street(
                "Street_" + parent + "_" + i,
                distance,
                intersections.get(parent),
                intersections.get(i)
            );
            city.addStreet(street);
            addedEdges.add(edgeKey(parent, i));
        }

        // Add additional random streets up to numStreets
        int attempts = 0;
        while (city.getStreets().size() < numStreets && attempts < numStreets * 10) {
            attempts++;
            int i = random.nextInt(intersections.size());
            int j = random.nextInt(intersections.size());
            
            if (i != j && !addedEdges.contains(edgeKey(i, j))) {
                Point p1 = points.get(i);
                Point p2 = points.get(j);
                int distance = (int) Math.ceil(p1.distanceTo(p2));
                
                Street street = new Street(
                    "Street_" + i + "_" + j,
                    distance,
                    intersections.get(i),
                    intersections.get(j)
                );
                city.addStreet(street);
                addedEdges.add(edgeKey(i, j));
            }
        }

        return city;
    }

    private String edgeKey(int i, int j) {
        return Math.min(i, j) + "-" + Math.max(i, j);
    }

    /**
     * Point in 2D space
     */
    private static class Point {
        double x, y;
        String name;

        Point(double x, double y, String name) {
            this.x = x;
            this.y = y;
            this.name = name;
        }

        double distanceTo(Point other) {
            double dx = this.x - other.x;
            double dy = this.y - other.y;
            return Math.sqrt(dx * dx + dy * dy);
        }
    }
}
