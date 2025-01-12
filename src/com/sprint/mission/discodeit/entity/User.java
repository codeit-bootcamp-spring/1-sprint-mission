package com.sprint.mission.discodeit.entity;

import static com.sprint.mission.discodeit.constant.StringConstant.EMPTY_STRING;

public class User extends BaseEntity {
    private static final User EMPTY_USER = new User();

    private final String name;
    private final String email;
    private final String phoneNumber;

    private User() {
        super(EMPTY_BASE_ENTITY);
        name = EMPTY_STRING.getValue();
        email = EMPTY_STRING.getValue();
        phoneNumber = EMPTY_STRING.getValue();
    }
    private User(Builder builder) {
        super(builder.baseEntity);
        name        = builder.name;
        email       = builder.email;
        phoneNumber = builder.phoneNumber;
    }

    public static User createEmptyUser() {
        return EMPTY_USER;
    }

    public static final class Builder {
        private BaseEntity baseEntity = BaseEntity.createBaseEntity();
        private final String name;
        private final String email;
        private String phoneNumber = EMPTY_STRING.getValue();

        public Builder(String name, String email) {
            this.name  = name;
            this.email = email;
        }

        public Builder baseEntity(BaseEntity baseEntity) {
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
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                "} " + System.lineSeparator() +
                super.toString();
    }
}
