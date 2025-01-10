package discodeit.entity;

public class User extends Base {
    private String username;
    private String email;

    // 생성자
    public User(String username, String email) {
        super();
        this.username = username;
        this.email = email;
    }

    // Getter
    public String getUsername() {
        return username;
    }
    public String getEmail() {
        return email;
    }

    // Update
    public void updateUsername(String username) {
        this.username = username;
    }
    public void updateEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
