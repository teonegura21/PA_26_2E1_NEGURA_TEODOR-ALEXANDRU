
import java.util.*;

public class SocialNetworkTest {

    public static List<Node> findArticulationPoints(SocialNetwork network) {
        List<Node> articulationPoints = new ArrayList<>();
        Set<Node> visited = new HashSet<>();
        Map<Node, Integer> discovery = new HashMap<>();
        Map<Node, Integer> low = new HashMap<>();
        Map<Node, Node> parent = new HashMap<>();
        int[] time = {0};

        for (Node node : network.getNodes()) {
            if (!visited.contains(node)) {
                findAPUtil(node, visited, discovery, low, parent, articulationPoints, network, time);
            }
        }

        return articulationPoints;
    }

    private static void findAPUtil(Node u, Set<Node> visited, Map<Node, Integer> discovery,
                                   Map<Node, Integer> low, Map<Node, Node> parent,
                                   List<Node> articulationPoints, SocialNetwork network, int[] time) {
        visited.add(u);
        discovery.put(u, time[0]);
        low.put(u, time[0]);
        time[0]++;

        int children = 0;

        for (Node v : network.getNeighbors(u)) {
            if (!visited.contains(v)) {
                children++;
                parent.put(v, u);
                findAPUtil(v, visited, discovery, low, parent, articulationPoints, network, time);

                low.put(u, Math.min(low.get(u), low.get(v)));

                if (parent.get(u) == null && children > 1) {
                    articulationPoints.add(u);
                }

                if (parent.get(u) != null && low.get(v) >= discovery.get(u)) {
                    articulationPoints.add(u);
                }
            } else if (!v.equals(parent.get(u))) {
                low.put(u, Math.min(low.get(u), discovery.get(v)));
            }
        }
    }

    public static void main(String[] args) {
        SocialNetwork network = new SocialNetwork();

        Person p1 = new Person("Alice", 25, "1998-05-15");
        Person p2 = new Person("Bob", 30, "1993-08-22");
        Person p3 = new Person("Charlie", 28, "1995-03-10");
        Person p4 = new Person("David", 35, "1988-11-20");

        network.addNode(p1);
        network.addNode(p2);
        network.addNode(p3);
        network.addNode(p4);

        network.addRelationship(p1, p2);
        network.addRelationship(p2, p3);
        network.addRelationship(p2, p4);

        System.out.println("Network:");
        network.printNetwork();

        List<Node> articulationPoints = findArticulationPoints(network);
        System.out.println("\nArticulation Points: " + articulationPoints);
    }
}
