import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class SocialNetworkJUnitTest {
    private SocialNetwork network;
    private Person p1, p2, p3, p4;
    private Company c1;

    @BeforeEach
    void setUp() {
        network = new SocialNetwork();
        p1 = new Person("Alice", 25, "1998-05-15");
        p2 = new Person("Bob", 30, "1993-08-22");
        p3 = new Person("Charlie", 28, "1995-03-10");
        p4 = new Person("David", 35, "1988-11-20");
        c1 = new Company("Google", 100000);
    }

    @Test
    void testNetworkConnectivity() {
        network.addNode(p1);
        network.addNode(p2);
        network.addRelationship(p1, p2);

        assertTrue(network.getNeighbors(p1).contains(p2));
        assertTrue(network.getNeighbors(p2).contains(p1));
    }

    @Test
    void testArticulationPoints() {
        network.addNode(p1);
        network.addNode(p2);
        network.addNode(p3);
        network.addNode(p4);

        network.addRelationship(p1, p2);
        network.addRelationship(p2, p3);
        network.addRelationship(p2, p4);

        List<Node> articulationPoints = SocialNetworkTest.findArticulationPoints(network);
        
        assertEquals(1, articulationPoints.size(), "Should have exactly 1 articulation point");
        assertEquals("Bob", articulationPoints.get(0).getName(), "Bob should be the articulation point");
    }

    @Test
    void testNoArticulationPointsInCompleteGraph() {
        network.addNode(p1);
        network.addNode(p2);
        network.addNode(p3);

        network.addRelationship(p1, p2);
        network.addRelationship(p2, p3);
        network.addRelationship(p1, p3);

        List<Node> articulationPoints = SocialNetworkTest.findArticulationPoints(network);
        
        assertTrue(articulationPoints.isEmpty(), "Complete graph should have no articulation points");
    }

    @Test
    void testBiconnectedComponents() {
        network.addNode(p1);
        network.addNode(p2);
        network.addNode(p3);
        network.addNode(p4);

        network.addRelationship(p1, p2);
        network.addRelationship(p2, p3);
        network.addRelationship(p2, p4);

        List<Set<Node>> components = SocialNetworkTest.findBiconnectedComponents(network);
        
        assertFalse(components.isEmpty(), "Should have at least one biconnected component");
    }

    @Test
    void testPersonNode() {
        assertEquals("Alice", p1.getName());
        assertEquals(25, p1.getAge());
        assertEquals("1998-05-15", p1.getBirthDate());
    }

    @Test
    void testCompanyNode() {
        assertEquals("Google", c1.getName());
        assertEquals(100000, c1.getEmployeeCount());
    }

    @Test
    void testImportanceCalculation() {
        network.addNode(p1);
        network.addNode(p2);
        network.addNode(p3);
        network.addNode(c1);

        network.addRelationship(p1, p2);
        network.addRelationship(p1, p3);
        network.addRelationship(p1, c1);

        assertEquals(3, network.getImportance(p1), "Alice should have importance 3");
        assertEquals(1, network.getImportance(p2), "Bob should have importance 1");
    }
}
