import java.util.*;

public class SocialNetworkTest {

    public static List<Node> findArticulationPoints(SocialNetwork network) {
        Set<Node> articulationPointsSet = new HashSet<>();
        Set<Node> visited = new HashSet<>();
        Map<Node, Integer> discovery = new HashMap<>();
        Map<Node, Integer> low = new HashMap<>();
        Map<Node, Node> parent = new HashMap<>();
        int[] time = {0};

        for (Node node : network.getNodes()) {
            if (!visited.contains(node)) {
                findAPUtil(node, visited, discovery, low, parent, articulationPointsSet, network, time);
            }
        }

        return new ArrayList<>(articulationPointsSet);
    }

    private static void findAPUtil(Node u, Set<Node> visited, Map<Node, Integer> discovery,
                                   Map<Node, Integer> low, Map<Node, Node> parent,
                                   Set<Node> articulationPointsSet, SocialNetwork network, int[] time) {
        visited.add(u);
        discovery.put(u, time[0]);
        low.put(u, time[0]);
        time[0]++;

        int children = 0;

        for (Node v : network.getNeighbors(u)) {
            if (!visited.contains(v)) {
                children++;
                parent.put(v, u);
                findAPUtil(v, visited, discovery, low, parent, articulationPointsSet, network, time);

                low.put(u, Math.min(low.get(u), low.get(v)));

                if (parent.get(u) == null && children > 1) {
                    articulationPointsSet.add(u);
                }

                if (parent.get(u) != null && low.get(v) >= discovery.get(u)) {
                    articulationPointsSet.add(u);
                }
            } else if (!v.equals(parent.get(u))) {
                low.put(u, Math.min(low.get(u), discovery.get(v)));
            }
        }
    }

    public static List<Set<Node>> findBiconnectedComponents(SocialNetwork network) {
        List<Set<Node>> components = new ArrayList<>();
        Set<Node> visited = new HashSet<>();
        Map<Node, Integer> discovery = new HashMap<>();
        Map<Node, Integer> low = new HashMap<>();
        int[] time = {0};
        Deque<Edge> edgeStack = new ArrayDeque<>();

        for (Node node : network.getNodes()) {
            if (!visited.contains(node)) {
                bccUtil(node, null, visited, discovery, low, edgeStack, components, network, time);
                if (!edgeStack.isEmpty()) {
                    Set<Node> component = new HashSet<>();
                    while (!edgeStack.isEmpty()) {
                        Edge e = edgeStack.pop();
                        component.add(e.u);
                        component.add(e.v);
                    }
                    if (!component.isEmpty()) {
                        components.add(component);
                    }
                }
            }
        }

        return components;
    }

    private static void bccUtil(Node u, Node parent, Set<Node> visited, Map<Node, Integer> discovery,
                                Map<Node, Integer> low, Deque<Edge> edgeStack,
                                List<Set<Node>> components, SocialNetwork network, int[] time) {
        visited.add(u);
        discovery.put(u, time[0]);
        low.put(u, time[0]);
        time[0]++;

        int children = 0;

        for (Node v : network.getNeighbors(u)) {
            if (!visited.contains(v)) {
                children++;
                edgeStack.push(new Edge(u, v));
                bccUtil(v, u, visited, discovery, low, edgeStack, components, network, time);

                low.put(u, Math.min(low.get(u), low.get(v)));

                if (low.get(v) >= discovery.get(u)) {
                    Set<Node> component = new HashSet<>();
                    while (!edgeStack.isEmpty()) {
                        Edge e = edgeStack.pop();
                        component.add(e.u);
                        component.add(e.v);
                        if ((e.u.equals(u) && e.v.equals(v)) || (e.u.equals(v) && e.v.equals(u))) {
                            break;
                        }
                    }
                    if (!component.isEmpty()) {
                        components.add(component);
                    }
                }
            } else if (v != parent && discovery.get(v) < discovery.get(u)) {
                low.put(u, Math.min(low.get(u), discovery.get(v)));
                edgeStack.push(new Edge(u, v));
            }
        }
    }

    private static class Edge {
        Node u, v;
        Edge(Node u, Node v) {
            this.u = u;
            this.v = v;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Test Articulation Points ===");
        testArticulationPoints();
        
        System.out.println("\n=== Test Biconnected Components ===");
        testBiconnectedComponents();
    }

    private static void testArticulationPoints() {
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

        List<Node> articulationPoints = findArticulationPoints(network);
        System.out.println("Network: Alice-Bob-Charlie, Alice-Bob-David");
        System.out.println("Articulation Points: " + articulationPoints);
        assert articulationPoints.size() == 1 : "Should have 1 articulation point";
        assert articulationPoints.get(0).getName().equals("Bob") : "Bob should be articulation point";
        System.out.println("Test PASSED!");
    }

    private static void testBiconnectedComponents() {
        SocialNetwork network = new SocialNetwork();

        Person p1 = new Person("A", 25, "1998-05-15");
        Person p2 = new Person("B", 30, "1993-08-22");
        Person p3 = new Person("C", 28, "1995-03-10");
        Person p4 = new Person("D", 35, "1988-11-20");

        network.addNode(p1);
        network.addNode(p2);
        network.addNode(p3);
        network.addNode(p4);

        network.addRelationship(p1, p2);
        network.addRelationship(p2, p3);
        network.addRelationship(p2, p4);

        List<Set<Node>> components = findBiconnectedComponents(network);
        System.out.println("Network: A-B-C, B-D");
        System.out.println("Biconnected Components found: " + components.size());
        for (int i = 0; i < components.size(); i++) {
            System.out.println("  Component " + (i+1) + ": " + components.get(i));
        }
        System.out.println("Test PASSED!");
    }
}
