public class Programmer extends Person {
    private String favoriteLanguage;

    public Programmer(String name, int age, String birthDate, String favoriteLanguage) {
        super(name, age, birthDate);
        this.favoriteLanguage = favoriteLanguage;
    }

    public String getFavoriteLanguage() { return favoriteLanguage; }

    @Override
    public String toString() {
        return "Programmer{name='" + getName() + "', age=" + getAge() + 
               ", language='" + favoriteLanguage + "'}";
    }
}
