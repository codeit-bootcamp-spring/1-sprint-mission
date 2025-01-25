package discodeit.entity;

import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User {

    private UUID id;
    private long createdAt;
    private long updatedAt;
    private String name;
    private String email;
    private String phoneNumber;
    private String password;

    public User(String name, String email, String phoneNumber, String password) {
        long currentUnixTime = System.currentTimeMillis() / 1000;
        this.id = UUID.randomUUID();
        this.createdAt = currentUnixTime;
        this.updatedAt = currentUnixTime;

        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public UUID getId() {
        return id;
    }

    public void updateUpdatedAt() {
        this.updatedAt = System.currentTimeMillis() / 1000;
    }

    public String getName() {
        return name;
    }

    public void update(String name, String email, String phoneNumber) {
        boolean updated = updateName(name) || updateEmail(email) || updatePhoneNumber(phoneNumber);
        if (updated) {
            updateUpdatedAt();
        }
    }

    public boolean updateName(String name) {
        if (this.name.equals(name)) {
            return false;
        }
        this.name = name;
        return true;
    }

    public boolean updateEmail(String email) {
        if (this.email.equals(email)) {
            return false;
        }
        this.email = email;
        return true;
    }

    public boolean updatePhoneNumber(String phoneNumber) {
        if (this.phoneNumber.equals(phoneNumber)) {
            return false;
        }
        this.phoneNumber = phoneNumber;
        return true;
    }

    public void updatePassword(String originalPassword, String newPassword) {
        if (!BCrypt.checkpw(originalPassword, password)) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }
        this.password = BCrypt.hashpw(newPassword, BCrypt.gensalt());
    }

    public boolean isEqualTo(User user) {
        return id.equals(user.getId());
    }

    public boolean isIdEqualTo(UUID id) {
        return this.id.equals(id);
    }

    public void isDuplicateEmail(String email) {
        if (this.email.equals(email)) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }
    }

    public void isDuplicatePhoneNumber(String phoneNumber) {
        if (this.phoneNumber.equals(phoneNumber)) {
            throw new IllegalArgumentException("이미 존재하는 번호입니다.");
        }
    }

    public void withdraw() {
        this.id = null;
        this.name = "(알 수 없음)";
        this.email = null;
        this.phoneNumber = null;
        this.password = null;
    }

    @Override
    public String toString() {
        return String.format(
                name + "님의 정보입니다." + System.lineSeparator()
                        + "Name: " + name + System.lineSeparator()
                        + "Email: " + email + System.lineSeparator()
                        + "Phone number: " + phoneNumber + System.lineSeparator()
        );
    }
}
