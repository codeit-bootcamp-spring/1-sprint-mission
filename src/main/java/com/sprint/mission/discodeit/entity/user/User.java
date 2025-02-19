package com.sprint.mission.discodeit.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sprint.mission.discodeit.entity.BaseObject;
import com.sprint.mission.discodeit.exception.user.IllegalUserException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class User extends BaseObject implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("userName")
    private String userName;

    @JsonProperty("userEmail")
    private String userEmail;

    @JsonProperty("userPassword")
    private transient String userPassword;

    @JsonProperty("userRole")
    private UserRole userRole;

    public User() {}

    public User(UUID userid) {
        super(userid);
    }

    public User(UUID userId, String userName, String userEmail) {
        super(userId);
        setUserName(userName);
        setUserEmail(userEmail);
    }

    public User(String userName, String userEmail, String userPassword) {
        super();
        setUserName(userName);
        setUserEmail(userEmail);
        setUserRole(UserRole.ROLE_COMMON);
        setUserPassword(userPassword);
    }

    public User(String userName, String userEmail, String userPassword, UserRole userRole) {
        super();
        setUserName(userName);
        setUserEmail(userEmail);
        setUserRole(userRole);
        setUserPassword(userPassword);
    }

    public User(UUID userId, String userName, String userEmail, String userPassword) {
        super(userId);
        setUserName(userName);
        setUserEmail(userEmail);
        setUserPassword(userPassword);
    }

    public User(UUID userId, String userName, String userEmail, String userPassword, UserRole userRole) {
        super(userId);
        setUserName(userName);
        setUserEmail(userEmail);
        setUserRole(userRole);
        setUserPassword(userPassword);
    }



    private void setUserRole(UserRole userRole) {
        if (userRole == null) {
            throw new IllegalUserException("user Role을 지정해주세요.");
        }

        this.userRole = userRole;
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
            throw new UserNotFoundException("이름을 작성해주세요.");
        }
        return true;
    }

    private boolean checkUserEmail(String userEmail) {
        final String EMAIL_REGEX = "^[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-zA-Z0-9]+$";

        if (userEmail == null || userEmail.isEmpty()) {
            throw new UserNotFoundException("이메일을 적어주세요.");
        } else if (!userEmail.matches(EMAIL_REGEX)) {
            throw new UserNotFoundException("이메일 형식이 잘못되었습니다.");
        }

        return true;
    }

    private boolean checkUserPassword(String userPassword) {
        if (userPassword == null || userPassword.length() < 6) {
            throw new UserNotFoundException("비밀번호가 잘못되었습니다.");
        }
        return true;
    }

    public String updateName(String updateName) {
        setUserName(updateName);
        return this.userName;
    }

    public UserRole updateUserRole(UserRole userRole) {
        setUserRole(userRole);
        return userRole;
    }

    public String updateEmail(String updateEmail) {
        setUserEmail(updateEmail);
        return this.userEmail;
    }

    public String updatePassword(String updatePassword) {
        setUserPassword(updatePassword);
        return this.userPassword;
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
        return Objects.equals(getId(), user.getId()) &&
                Objects.equals(userName, user.userName) &&
                Objects.equals(userEmail, user.userEmail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), userName, userEmail);
    }
}
