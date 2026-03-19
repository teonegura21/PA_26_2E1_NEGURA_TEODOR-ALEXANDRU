
public class Roads {
    public static void main(String[] args) {
        Location loc1 = new Location("Iasi", 47.1585, 27.6014);
        Location loc2 = new Location("Bucuresti", 44.4268, 26.1025);
        Location loc3 = new Location("Cluj", 46.7712, 23.6236);

        Road road1 = new Road("DN1", 300, loc1, loc2);
        Road road2 = new Road("DN2", 450, loc2, loc3);

        System.out.println(loc1);
        System.out.println(loc2);
        System.out.println(loc3);
        System.out.println(road1);
        System.out.println(road2);
    }
}
