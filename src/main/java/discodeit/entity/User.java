package discodeit.entity;
import java.util.List;
import java.util.UUID;

public class User {
    private final String userId;
    private String userName;
    private String email;
    private String phoneNum;
    private final long createdAt;
    private long updatedAt;

    public User(String userName, String email, String phoneNum) {
        this.userId = UUID.randomUUID().toString();
        this.userName = userName;
        this.email = email;
        this.phoneNum = phoneNum;
        this.createdAt = System.currentTimeMillis();
    }

    // Getter
    public String getUserId() { return userId; }

    public String getUserName() { return userName; }

    public String getEmail() { return email; }

    public String getPhoneNum() { return phoneNum; }

    public long getCreatedAt() { return createdAt; }

    public long getUpdatedAt() { return updatedAt; }

    // Setter
    public void updateEmail(String email) {
        this.email = email;
        this.updatedAt = System.currentTimeMillis();
    }

    public void updatePhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
        this.updatedAt = System.currentTimeMillis();
    }

}
