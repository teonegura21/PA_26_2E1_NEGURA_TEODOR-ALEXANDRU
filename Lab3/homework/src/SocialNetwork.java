
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

    public void printNetwork() {
        for (Node node : nodes) {
            System.out.println(node + " -> " + getNeighbors(node));
        }
    }

    public static void main(String[] args) {
        SocialNetwork network = new SocialNetwork();

        Person p1 = new Programmer("Alice", 25, "1998-05-15", "Java");
        Person p2 = new Designer("Bob", 30, "1993-08-22", "Figma");
        Company c1 = new Company("Google", 100000);

        network.addNode(p1);
        network.addNode(p2);
        network.addNode(c1);

        network.addRelationship(p1, p2);
        network.addRelationship(p1, c1);

        network.printNetwork();
    }
}
