package discodeit.entity;

import org.mindrot.jbcrypt.BCrypt;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    private final UUID id;
    private final long createdAt;
    private long updatedAt;
    private String name;
    private String email;
    private String phoneNumber;
    private transient String password;

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

    public String getName() {
        return name;
    }

    public void updateUpdatedAt() {
        this.updatedAt = System.currentTimeMillis() / 1000;
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
        if (!BCrypt.checkpw(originalPassword, password) || BCrypt.checkpw(newPassword, password)) {
            throw new IllegalArgumentException("비밀번호 변경에 실패하였습니다.");
        }
        this.password = BCrypt.hashpw(newPassword, BCrypt.gensalt());
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
