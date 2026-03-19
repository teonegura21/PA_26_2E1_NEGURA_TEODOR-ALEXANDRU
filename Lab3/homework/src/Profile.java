
public class Profile {
    private String username;
    private String email;
    private String bio;

    public Profile(String username, String email, String bio) {
        this.username = username;
        this.email = email;
        this.bio = bio;
    }

    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getBio() { return bio; }

    @Override
    public String toString() {
        return "Profile{username='" + username + "', email='" + email + "'}";
    }
}
