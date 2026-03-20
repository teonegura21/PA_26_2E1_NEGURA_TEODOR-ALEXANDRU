import java.util.*;
import java.util.stream.Collectors;

public class City {
    private List<Street> streets;
    private Set<Intersection> intersections;

    public City() {
        this.streets = new ArrayList<>();
        this.intersections = new HashSet<>();
    }

    public void addStreet(Street street) {
        streets.add(street);
        intersections.add(street.getStart());
        intersections.add(street.getEnd());
    }

    public List<Street> getStreets() {
        return streets;
    }

    public Set<Intersection> getIntersections() {
        return intersections;
    }

    /**
     * Find streets longer than specified length that join at least minStreets intersections
     */
    public List<Street> getStreetsLongerThan(int length, int minStreets) {
        return streets.stream()
                .filter(s -> s.getLength() > length)
                .filter(s -> countConnectedStreets(s) >= minStreets)
                .collect(Collectors.toList());
    }

    /**
     * Count how many streets are connected to a given street (at both ends)
     */
    private int countConnectedStreets(Street street) {
        int count = 0;
        Intersection start = street.getStart();
        Intersection end = street.getEnd();
        
        for (Street s : streets) {
            if (s.getStart().equals(start) || s.getEnd().equals(start) ||
                s.getStart().equals(end) || s.getEnd().equals(end)) {
                count++;
            }
        }
        return count;
    }

    /**
     * Get streets connected to an intersection
     */
    public List<Street> getStreetsForIntersection(Intersection intersection) {
        return streets.stream()
                .filter(s -> s.getStart().equals(intersection) || s.getEnd().equals(intersection))
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "City{intersections=" + intersections.size() + ", streets=" + streets.size() + "}";
    }
}
