
import java.util.*;
import java.util.stream.IntStream;

public class App {
    public static void main(String[] args) {
        var nodes = IntStream.rangeClosed(0, 8)
                .mapToObj(i -> new Intersection("i" + i))
                .toArray(Intersection[]::new);

        List<Street> streets = new LinkedList<>();
        streets.add(new Street("s1", 2, nodes[0], nodes[1]));
        streets.add(new Street("s2", 2, nodes[1], nodes[2]));
        streets.add(new Street("s3", 2, nodes[2], nodes[3]));
        streets.add(new Street("s4", 2, nodes[3], nodes[4]));
        streets.add(new Street("s5", 3, nodes[4], nodes[5]));
        streets.add(new Street("s6", 1, nodes[5], nodes[6]));
        streets.add(new Street("s7", 3, nodes[6], nodes[7]));
        streets.add(new Street("s8", 2, nodes[7], nodes[8]));
        streets.add(new Street("s9", 3, nodes[8], nodes[0]));

        streets.sort(Comparator.comparingInt(Street::getLength));

        System.out.println("Intersections:");
        for (Intersection node : nodes) {
            System.out.println(node);
        }

        System.out.println("\nStreets (sorted by length):");
        for (Street street : streets) {
            System.out.println(street);
        }
    }
}
