public class Designer extends Person {
    private String favoriteTool;

    public Designer(String name, int age, String birthDate, String favoriteTool) {
        super(name, age, birthDate);
        this.favoriteTool = favoriteTool;
    }

    public String getFavoriteTool() { return favoriteTool; }

    @Override
    public String toString() {
        return "Designer{name='" + getName() + "', age=" + getAge() + 
               ", tool='" + favoriteTool + "'}";
    }
}
