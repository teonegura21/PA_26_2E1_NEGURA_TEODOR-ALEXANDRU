import java.util.*;

public class Advanced {
    public static void main(String[] args) {
        System.out.println("=== Lab 4 Advanced: TSP with 2-Approximation ===\n");

        // Generate random problem with triangle inequality
        GraphGenerator generator = new GraphGenerator(12345); // Fixed seed for reproducibility
        City city = generator.generateRandomCity(10, 20);

        System.out.println("Generated City:");
        System.out.println("  Intersections: " + city.getIntersections().size());
        System.out.println("  Streets: " + city.getStreets().size());
        
        System.out.println("\nStreets (with distances satisfying triangle inequality):");
        city.getStreets().forEach(s -> 
            System.out.println("  " + s.getName() + ": " + s.getStart().getName() + 
                " <-> " + s.getEnd().getName() + " (" + s.getLength() + ")"));

        // Solve TSP using 2-approximation
        System.out.println("\n=== Solving TSP ===");
        long startTime = System.currentTimeMillis();
        TSP.TSPSolution solution = TSP.solve(city);
        long endTime = System.currentTimeMillis();

        System.out.println("Solution found in " + (endTime - startTime) + "ms");
        System.out.println("Total route distance: " + solution.getTotalDistance());
        System.out.println("\nTour route:");
        
        List<Intersection> tour = solution.getTour();
        for (int i = 0; i < tour.size(); i++) {
            System.out.print(tour.get(i).getName());
            if (i < tour.size() - 1) System.out.print(" -> ");
        }
        System.out.println();

        // Verify MST cost as lower bound for TSP
        MSTSolver.Solution mst = MSTSolver.findMST(city);
        System.out.println("\n=== Quality Verification ===");
        System.out.println("MST cost (lower bound for TSP): " + mst.getTotalCost());
        System.out.println("TSP solution cost: " + solution.getTotalDistance());
        
        double ratio = (double) solution.getTotalDistance() / mst.getTotalCost();
        System.out.println("Solution/MST ratio: " + String.format("%.2f", ratio));
        System.out.println("Guarantee: TSP <= 2 * MST <= 2 * OPTIMAL");
        System.out.println("Status: " + (ratio <= 2.0 ? "✓ SATISFIES 2-approximation" : "✗ FAILS"));
    }
}
