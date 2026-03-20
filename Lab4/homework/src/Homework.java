import com.github.javafaker.Faker;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Homework {
    public static void main(String[] args) {
        Faker faker = new Faker();
        City city = new City();

        // Generate 10 intersections with fake names
        List<Intersection> intersections = IntStream.range(0, 10)
                .mapToObj(i -> new Intersection(faker.address().cityName() + "_" + i))
                .collect(Collectors.toList());

        System.out.println("=== Generated Intersections ===");
        intersections.forEach(System.out::println);

        // Generate streets connecting intersections with fake names
        Random rand = new Random(42); // Seed for reproducibility
        List<Street> streets = new ArrayList<>();
        
        // Create a connected graph (minimum spanning tree base)
        for (int i = 0; i < intersections.size() - 1; i++) {
            String streetName = faker.address().streetName();
            int length = rand.nextInt(10) + 1; // 1-10
            streets.add(new Street(streetName, length, intersections.get(i), intersections.get(i + 1)));
        }
        
        // Add more streets to create cycles
        for (int i = 0; i < 8; i++) {
            String streetName = faker.address().streetName();
            int startIdx = rand.nextInt(intersections.size());
            int endIdx = rand.nextInt(intersections.size());
            if (startIdx != endIdx) {
                int length = rand.nextInt(10) + 1;
                streets.add(new Street(streetName, length, 
                    intersections.get(startIdx), intersections.get(endIdx)));
            }
        }

        // Add streets to city
        streets.forEach(city::addStreet);

        System.out.println("\n=== Generated Streets ===");
        streets.forEach(System.out::println);

        System.out.println("\n=== City Summary ===");
        System.out.println(city);

        // Stream query: streets longer than 5 that join at least 3 streets
        System.out.println("\n=== Stream Query: Streets > length 5 AND join >= 3 streets ===");
        List<Street> filteredStreets = city.getStreetsLongerThan(5, 3);
        if (filteredStreets.isEmpty()) {
            System.out.println("No streets match criteria (trying with lower threshold)...");
            filteredStreets = city.getStreetsLongerThan(3, 2);
        }
        filteredStreets.forEach(s -> System.out.println(s + " [connected to " + 
            city.getStreetsForIntersection(s.getStart()).size() + " streets]"));

        // Find MST (minimum cost solution)
        System.out.println("\n=== MST Solution (Minimum Cost) ===");
        MSTSolver.Solution mst = MSTSolver.findMST(city);
        System.out.println("Total cost: " + mst.getTotalCost());
        System.out.println("Selected streets:");
        mst.getStreets().forEach(s -> System.out.println("  " + s));

        // Find multiple solutions
        System.out.println("\n=== Multiple Solutions (sorted by cost) ===");
        List<MSTSolver.Solution> solutions = MSTSolver.findMultipleSolutions(city, 5);
        for (int i = 0; i < solutions.size(); i++) {
            System.out.println((i + 1) + ". " + solutions.get(i));
        }
    }
}
