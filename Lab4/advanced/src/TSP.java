import java.util.*;

/**
 * TSP 2-Approximation Algorithm using MST.
 * 
 * Algorithm:
 1. Find MST of the graph
 2. Double the MST edges (create an Eulerian graph)
 3. Find Eulerian tour
 4. Shortcut the tour (skip already visited nodes)
 
 * Guarantee: Cost <= 2 * Optimal
 * Runtime: O(n^2 log n) - polynomial time
 */
public class TSP {

    /**
     * Result of TSP algorithm
     */
    public static class TSPSolution {
        private List<Intersection> tour;
        private int totalDistance;
        private double approximationRatio; // If optimal is known

        public TSPSolution(List<Intersection> tour, int totalDistance) {
            this.tour = tour;
            this.totalDistance = totalDistance;
        }

        public List<Intersection> getTour() { return tour; }
        public int getTotalDistance() { return totalDistance; }

        @Override
        public String toString() {
            return "TSPSolution{distance=" + totalDistance + ", nodes=" + tour.size() + "}";
        }
    }

    /**
     * Solve TSP using 2-approximation algorithm
     */
    public static TSPSolution solve(City city) {
        // Step 1: Find MST
        List<Street> mst = findMST(city);
        
        // Step 2: Build adjacency list for MST (doubled edges)
        Map<Intersection, List<Intersection>> adj = new HashMap<>();
        for (Intersection node : city.getIntersections()) {
            adj.put(node, new ArrayList<>());
        }
        
        // Add edges in both directions (doubling)
        for (Street street : mst) {
            adj.get(street.getStart()).add(street.getEnd());
            adj.get(street.getEnd()).add(street.getStart());
        }

        // Step 3: Find Eulerian tour using DFS
        List<Intersection> eulerianTour = new ArrayList<>();
        Set<Intersection> visited = new HashSet<>();
        
        Intersection start = city.getIntersections().iterator().next();
        findEulerianPath(start, adj, eulerianTour, visited);

        // Step 4: Shortcut - remove duplicates while preserving order
        List<Intersection> tour = new ArrayList<>();
        Set<Intersection> inTour = new HashSet<>();
        
        for (Intersection node : eulerianTour) {
            if (!inTour.contains(node)) {
                tour.add(node);
                inTour.add(node);
            }
        }
        // Return to start
        tour.add(tour.get(0));

        // Calculate total distance
        int totalDistance = calculateTourDistance(tour, city);

        return new TSPSolution(tour, totalDistance);
    }

    private static void findEulerianPath(Intersection node, 
                                         Map<Intersection, List<Intersection>> adj,
                                         List<Intersection> path, 
                                         Set<Intersection> visited) {
        visited.add(node);
        path.add(node);
        
        for (Intersection neighbor : adj.get(node)) {
            if (!visited.contains(neighbor)) {
                findEulerianPath(neighbor, adj, path, visited);
                path.add(node); // Return to current node
            }
        }
    }

    private static int calculateTourDistance(List<Intersection> tour, City city) {
        int distance = 0;
        
        // Build distance map from streets
        Map<String, Integer> distMap = new HashMap<>();
        for (Street s : city.getStreets()) {
            String key1 = s.getStart().getName() + "-" + s.getEnd().getName();
            String key2 = s.getEnd().getName() + "-" + s.getStart().getName();
            distMap.put(key1, s.getLength());
            distMap.put(key2, s.getLength());
        }
        
        // Sum distances
        for (int i = 0; i < tour.size() - 1; i++) {
            String key = tour.get(i).getName() + "-" + tour.get(i + 1).getName();
            Integer d = distMap.get(key);
            if (d != null) {
                distance += d;
            } else {
                // If direct edge doesn't exist, use shortest path estimate
                distance += estimateDistance(tour.get(i), tour.get(i + 1), city);
            }
        }
        
        return distance;
    }

    private static int estimateDistance(Intersection a, Intersection b, City city) {
        // Simple BFS to find shortest path
        Queue<Intersection> queue = new LinkedList<>();
        Map<Intersection, Integer> dist = new HashMap<>();
        queue.add(a);
        dist.put(a, 0);
        
        while (!queue.isEmpty()) {
            Intersection curr = queue.poll();
            if (curr.equals(b)) {
                return dist.get(curr);
            }
            
            for (Street s : city.getStreets()) {
                Intersection next = null;
                if (s.getStart().equals(curr)) next = s.getEnd();
                else if (s.getEnd().equals(curr)) next = s.getStart();
                
                if (next != null && !dist.containsKey(next)) {
                    dist.put(next, dist.get(curr) + s.getLength());
                    queue.add(next);
                }
            }
        }
        
        return 1000; // Large penalty if no path
    }

    /**
     * Find MST using Prim's algorithm
     */
    private static List<Street> findMST(City city) {
        List<Street> mst = new ArrayList<>();
        Set<Intersection> inMST = new HashSet<>();
        
        Intersection start = city.getIntersections().iterator().next();
        inMST.add(start);
        
        PriorityQueue<Edge> pq = new PriorityQueue<>(Comparator.comparingInt(e -> e.weight));
        
        // Add all edges from start
        for (Street s : city.getStreets()) {
            if (s.getStart().equals(start) || s.getEnd().equals(start)) {
                Intersection other = s.getStart().equals(start) ? s.getEnd() : s.getStart();
                pq.add(new Edge(start, other, s.getLength(), s));
            }
        }
        
        while (!pq.isEmpty() && inMST.size() < city.getIntersections().size()) {
            Edge edge = pq.poll();
            
            if (inMST.contains(edge.to)) continue;
            
            inMST.add(edge.to);
            mst.add(edge.street);
            
            // Add new edges
            for (Street s : city.getStreets()) {
                if (s.getStart().equals(edge.to) || s.getEnd().equals(edge.to)) {
                    Intersection other = s.getStart().equals(edge.to) ? s.getEnd() : s.getStart();
                    if (!inMST.contains(other)) {
                        pq.add(new Edge(edge.to, other, s.getLength(), s));
                    }
                }
            }
        }
        
        return mst;
    }

    private static class Edge {
        Intersection from, to;
        int weight;
        Street street;
        
        Edge(Intersection from, Intersection to, int weight, Street street) {
            this.from = from;
            this.to = to;
            this.weight = weight;
            this.street = street;
        }
    }
}
