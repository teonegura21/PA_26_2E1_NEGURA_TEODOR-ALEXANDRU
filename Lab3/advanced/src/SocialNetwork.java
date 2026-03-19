
import java.util.*;

public class SocialNetwork {
    private List<Node> nodes;
    private Map<Node, List<Node>> relationships;

    public SocialNetwork() {
        this.nodes = new ArrayList<>();
        this.relationships = new HashMap<>();
    }

    public void addNode(Node node) {
        nodes.add(node);
        relationships.put(node, new ArrayList<>());
    }

    public void addRelationship(Node a, Node b) {
        relationships.get(a).add(b);
        relationships.get(b).add(a);
    }

    public List<Node> getNeighbors(Node node) {
        return relationships.getOrDefault(node, new ArrayList<>());
    }

    public List<Node> getNodes() { return nodes; }

    public void printNetwork() {
        for (Node node : nodes) {
            System.out.println(node + " -> " + getNeighbors(node));
        }
    }

    public static void main(String[] args) {
        SocialNetwork network = new SocialNetwork();

        Person p1 = new Person("Alice", 25, "1998-05-15");
        Person p2 = new Person("Bob", 30, "1993-08-22");
        Person p3 = new Person("Charlie", 28, "1995-03-10");
        Company c1 = new Company("Google", 100000);
        Company c2 = new Company("Microsoft", 120000);

        network.addNode(p1);
        network.addNode(p2);
        network.addNode(p3);
        network.addNode(c1);
        network.addNode(c2);

        network.addRelationship(p1, p2);
        network.addRelationship(p2, p3);
        network.addRelationship(p1, c1);
        network.addRelationship(p2, c2);

        network.printNetwork();
    }
}
