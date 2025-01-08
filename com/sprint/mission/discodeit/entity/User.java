package sprint.mission.discodeit.entity;

import java.util.Objects;

public class User {
    private static final User EMPTY_USER =
            new User(BaseEntity.createEmptyBasicEntity());

    private final BaseEntity baseEntity;
    private String name;
    private String email;
    private String phoneNumber;

    private User(BaseEntity baseEntity) {
        this.baseEntity = baseEntity;
    }
    private User(Builder builder) {
        baseEntity  = builder.baseEntity;
        name        = builder.name;
        email       = builder.email;
        phoneNumber = builder.phoneNumber;
    }

    public static User createEmptyUser() {
        return EMPTY_USER;
    }

    public static final class Builder {
        private BaseEntity baseEntity = BaseEntity.createBasicEntity();
        private final String name;
        private final String email;
        private String phoneNumber = "";

        public Builder(String name, String email) {
            this.name  = name;
            this.email = email;
        }

        public Builder common(BaseEntity baseEntity) {
            this.baseEntity = baseEntity;
            return this;
        }
        public Builder phoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }

    public BaseEntity getCommon() {
        return baseEntity;
    }
    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(baseEntity, user.baseEntity) && Objects.equals(name, user.name) && Objects.equals(email, user.email) && Objects.equals(phoneNumber, user.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(baseEntity, name, email, phoneNumber);
    }

    @Override
    public String toString() {
        return "User{" +
                "common=" + baseEntity +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
