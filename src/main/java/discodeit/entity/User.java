package discodeit.entity;

public class User extends Base {
    private String username;
    private String email;
    private String password;

    // 생성자
    public User(String username, String email, String password) {
        super();
        this.username = username;
        this.email = email;
        this.password = password;
    }

    // Getter
    public String getUsername() {
        return username;
    }
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }

    // Update
    public void update(String newUsername, String newEmail, String newPassword) {
        boolean anyValueUpdated = false;

        if(newUsername != null && !newUsername.equals(username)) {
            username = newUsername;
            anyValueUpdated = true;
        }
        if(newEmail != null && !newEmail.equals(email)) {
            email = newEmail;
            anyValueUpdated = true;
        }
        if(newPassword != null && !newPassword.equals(password)) {
            password = newPassword;
            anyValueUpdated = true;
        }

        if(anyValueUpdated) {
            this.updateUpdatedAt();
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
