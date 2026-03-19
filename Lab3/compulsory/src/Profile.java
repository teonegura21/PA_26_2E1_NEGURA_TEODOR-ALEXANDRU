
public class Profile {
    private String username;
    private String email;

    public Profile(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public String getUsername() { return username; }
    public String getEmail() { return email; }

    @Override
    public String toString() {
        return "Profile{username='" + username + "', email='" + email + "'}";
    }
}
