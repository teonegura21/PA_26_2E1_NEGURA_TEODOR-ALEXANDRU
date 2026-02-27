package lab2.compulsory.src;

public class Roads {
    public static void main(String[] args) {
        Location city = new Location("Bucharest", "City", 0, 0);
        Location airport = new Location("Otopeni", "Airport", 3, 4);
        Location gasStation = new Location("Ploiesti", "Gas Station", 6, 8);

        System.out.println(city);
        System.out.println(airport);
        System.out.println(gasStation);

        Road highway = new Road("Highway", 5, 130, city, airport);
        Road country = new Road("Country", 6, 90, airport, gasStation);

        System.out.println(highway);
        System.out.println(country);
    }
}


