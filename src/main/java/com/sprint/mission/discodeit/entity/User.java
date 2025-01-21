package com.sprint.mission.discodeit.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class User extends BaseObject implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String userName;
    private String userEmail;
    private String userPassword;

    public User(String userName, String userEmail, String userPassword) {
        super();
        setUserName(userName);
        setUserEmail(userEmail);
        setUserPassword(userPassword);
    }

    private void setUserName(String userName) {
        checkUserName(userName);
        this.userName = userName;
        setUpdatedAt();
    }

    private void setUserEmail(String userEmail) {
        checkUserEmail(userEmail);
        this.userEmail = userEmail;
        setUpdatedAt();
    }

    private void setUserPassword(String userPassword) {
        checkUserPassword(userPassword);
        this.userPassword = userPassword;
        setUpdatedAt();
    }

    private boolean checkUserName(String userName) {
        if (userName == null || userName.isEmpty()) {
            throw new IllegalArgumentException("이름을 작성해주세요.");
        }
        return true;
    }

    private boolean checkUserEmail(String userEmail) {
        final String EMAIL_REGEX = "^[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-zA-Z0-9]+$";

        if (userEmail == null || userEmail.isEmpty()) {
            throw new IllegalArgumentException("이메일을 적어주세요.");
        } else if (!userEmail.matches(EMAIL_REGEX)) {
            throw new IllegalArgumentException("이메일 형식이 잘못되었습니다.");
        }

        return true;
    }

    private boolean checkUserPassword(String userPassword) {
        if (userPassword == null || userPassword.length() < 6) {
            throw new IllegalArgumentException("비밀번호가 잘못되었습니다.");
        }
        return true;
    }

    public String updateName(String updateName) {
        setUserName(updateName);
        return this.userName;
    }

    public String updateEmail(String updateEmail) {
        setUserEmail(updateEmail);
        return this.userEmail;
    }

    public String updatePassword(String updatePassword) {
        setUserPassword(updatePassword);
        return this.userPassword;
    }

    public UUID getUserId() {
        return getId();
    }

    public String getUserName() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public Long getCreatedAt() {
        return getCreatedAtBaseObject();
    }

    public Long getUpdatedAt() {
        return getUpdatedAtBaseObject();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + getId() +
                ", name='" + userName + '\'' +
                ", email='" + userEmail + '\'' +
                ", createdAt=" + getCreatedAt() +
                ", updatedAt=" + getUpdatedAt() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userName, user.userName) && Objects.equals(userEmail, user.userEmail) && Objects.equals(userPassword, user.userPassword) && Objects.equals(getId(), user.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), userName, userEmail, userPassword);
    }
}
