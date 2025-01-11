package discodeit.entity;

import java.util.UUID;

public class User {

    private UUID id;
    private long createdAt;
    private long updatedAt;
    private String userName;
    private String displayName;
    private String email;
    private String phoneNumber;
    private String password;

    public User(String userName, String displayName, String email, String phoneNumber, String password) {
        long currentUnixTime = System.currentTimeMillis() / 1000;
        id = UUID.randomUUID();
        createdAt = currentUnixTime;
        updatedAt = currentUnixTime;

        this.userName = userName;
        this.displayName = displayName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public UUID getId() {
        return id;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void updateUpdatedAt() {
        this.updatedAt = System.currentTimeMillis() / 1000;
    }

    public String getUserName() {
        return userName;
    }

    public void updateUserName(String userName) {
        this.userName = userName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void updateDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void updateEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void updatePhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void updatePassword(String password) {
        this.password = password;
    }
}
