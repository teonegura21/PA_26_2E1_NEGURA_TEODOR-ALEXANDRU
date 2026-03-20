import java.util.*;

public class MSTSolver {

    /**
     * Represents a solution with its cost and selected streets
     */
    public static class Solution {
        private List<Street> streets;
        private int totalCost;

        public Solution(List<Street> streets, int totalCost) {
            this.streets = new ArrayList<>(streets);
            this.totalCost = totalCost;
        }

        public List<Street> getStreets() { return streets; }
        public int getTotalCost() { return totalCost; }

        @Override
        public String toString() {
            return "Solution{cost=" + totalCost + ", streets=" + streets.size() + "}";
        }
    }

    /**
     * Kruskal's algorithm to find MST
     */
    public static Solution findMST(City city) {
        List<Street> allStreets = new ArrayList<>(city.getStreets());
        Collections.sort(allStreets, Comparator.comparingInt(Street::getLength));

        List<Street> mst = new ArrayList<>();
        int totalCost = 0;

        UnionFind uf = new UnionFind(city.getIntersections());

        for (Street street : allStreets) {
            Intersection start = street.getStart();
            Intersection end = street.getEnd();

            if (!uf.connected(start, end)) {
                uf.union(start, end);
                mst.add(street);
                totalCost += street.getLength();
            }

            // MST complete when we have n-1 edges
            if (mst.size() == city.getIntersections().size() - 1) {
                break;
            }
        }

        return new Solution(mst, totalCost);
    }

    /**
     * Find multiple MST solutions by varying edge selection
     */
    public static List<Solution> findMultipleSolutions(City city, int count) {
        List<Solution> solutions = new ArrayList<>();
        List<Street> allStreets = new ArrayList<>(city.getStreets());
        
        // Sort by length
        Collections.sort(allStreets, Comparator.comparingInt(Street::getLength));
        
        // Find minimum cost
        Solution minSolution = findMST(city);
        solutions.add(minSolution);
        
        // Generate alternative solutions by excluding one MST edge at a time
        Set<String> seen = new HashSet<>();
        seen.add(getSolutionKey(minSolution.getStreets()));
        
        for (Street excluded : minSolution.getStreets()) {
            if (solutions.size() >= count) break;
            
            Solution alt = findMSTWithoutEdge(city, allStreets, excluded);
            if (alt != null) {
                String key = getSolutionKey(alt.getStreets());
                if (!seen.contains(key)) {
                    seen.add(key);
                    solutions.add(alt);
                }
            }
        }
        
        // Sort by cost
        solutions.sort(Comparator.comparingInt(Solution::getTotalCost));
        return solutions;
    }

    private static Solution findMSTWithoutEdge(City city, List<Street> sortedStreets, Street excluded) {
        List<Street> mst = new ArrayList<>();
        int totalCost = 0;

        UnionFind uf = new UnionFind(city.getIntersections());

        for (Street street : sortedStreets) {
            if (street.equals(excluded)) continue;
            
            Intersection start = street.getStart();
            Intersection end = street.getEnd();

            if (!uf.connected(start, end)) {
                uf.union(start, end);
                mst.add(street);
                totalCost += street.getLength();
            }

            if (mst.size() == city.getIntersections().size() - 1) {
                return new Solution(mst, totalCost);
            }
        }
        
        return null; // No valid MST without this edge
    }

    private static String getSolutionKey(List<Street> streets) {
        List<String> names = new ArrayList<>();
        for (Street s : streets) names.add(s.getName());
        Collections.sort(names);
        return String.join(",", names);
    }

    /**
     * Union-Find data structure for Kruskal's algorithm
     */
    private static class UnionFind {
        private Map<Intersection, Intersection> parent;
        private Map<Intersection, Integer> rank;

        public UnionFind(Set<Intersection> nodes) {
            parent = new HashMap<>();
            rank = new HashMap<>();
            for (Intersection node : nodes) {
                parent.put(node, node);
                rank.put(node, 0);
            }
        }

        public Intersection find(Intersection x) {
            if (!parent.get(x).equals(x)) {
                parent.put(x, find(parent.get(x)));
            }
            return parent.get(x);
        }

        public void union(Intersection x, Intersection y) {
            Intersection px = find(x);
            Intersection py = find(y);
            
            if (px.equals(py)) return;
            
            if (rank.get(px) < rank.get(py)) {
                parent.put(px, py);
            } else if (rank.get(px) > rank.get(py)) {
                parent.put(py, px);
            } else {
                parent.put(py, px);
                rank.put(px, rank.get(px) + 1);
            }
        }

        public boolean connected(Intersection x, Intersection y) {
            return find(x).equals(find(y));
        }
    }
}
