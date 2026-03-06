package Lab3.Compulsory.src;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Compulsory {
    public static void main(String[] args) {
        List<Profile> profiles = new ArrayList<>();

        profiles.add(new Person("P1", "Ana"));
        profiles.add(new Company("C1", "BitSoft"));
        profiles.add(new Person("P2", "Mihai"));
        profiles.add(new Company("C2", "ArtVision"));

        profiles.sort(Comparator.comparing(Profile::getName));

        for (Profile profile : profiles) {
            System.out.println(profile);
        }
    }
}
