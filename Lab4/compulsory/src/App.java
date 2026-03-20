import java.util.*;
import java.util.stream.IntStream;

public class App {
    public static void main(String[] args) {
        // Create 10 intersections using Streams
        var nodes = IntStream.rangeClosed(0, 9)
                .mapToObj(i -> new Intersection("i" + i))
                .toArray(Intersection[]::new);

        // Create LinkedList of streets
        List<Street> streets = new LinkedList<>();
        streets.add(new Street("s1", 2, nodes[0], nodes[1]));
        streets.add(new Street("s2", 3, nodes[1], nodes[2]));
        streets.add(new Street("s3", 1, nodes[2], nodes[3]));
        streets.add(new Street("s4", 4, nodes[3], nodes[4]));
        streets.add(new Street("s5", 2, nodes[4], nodes[5]));
        streets.add(new Street("s6", 5, nodes[5], nodes[6]));
        streets.add(new Street("s7", 1, nodes[6], nodes[7]));
        streets.add(new Street("s8", 3, nodes[7], nodes[8]));
        streets.add(new Street("s9", 2, nodes[8], nodes[9]));
        streets.add(new Street("s10", 4, nodes[9], nodes[0]));

        // Sort streets by length using lambda comparator
        streets.sort(Comparator.comparingInt(Street::getLength));

        System.out.println("=== Streets sorted by length ===");
        streets.forEach(System.out::println);

        // Create HashSet of intersections - Set doesn't contain duplicates
        Set<Intersection> intersectionSet = new HashSet<>();
        
        // Add all intersections
        for (Intersection node : nodes) {
            intersectionSet.add(node);
        }
        
        System.out.println("\n=== HashSet verification ===");
        System.out.println("Original intersections count: " + nodes.length);
        System.out.println("HashSet size after adding all: " + intersectionSet.size());
        
        // Try to add a duplicate intersection
        Intersection duplicate = new Intersection("i0"); // Same name as first intersection
        boolean added = intersectionSet.add(duplicate);
        System.out.println("Tried to add duplicate 'i0': " + (added ? "ADDED" : "REJECTED"));
        System.out.println("HashSet size after duplicate attempt: " + intersectionSet.size());
        
        // Verify that equals() and hashCode() work correctly
        System.out.println("\n=== equals() and hashCode() verification ===");
        System.out.println("nodes[0] equals duplicate: " + nodes[0].equals(duplicate));
        System.out.println("nodes[0] hashCode: " + nodes[0].hashCode());
        System.out.println("duplicate hashCode: " + duplicate.hashCode());
    }
}
