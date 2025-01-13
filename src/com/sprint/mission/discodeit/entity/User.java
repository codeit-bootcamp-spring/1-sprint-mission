package com.sprint.mission.discodeit.entity;

import java.util.UUID;

import static com.sprint.mission.discodeit.constant.IntegerConstant.EMPTY_TIME;
import static com.sprint.mission.discodeit.constant.StringConstant.EMPTY_STRING;
import static com.sprint.mission.discodeit.constant.StringConstant.EMPTY_UUID;

public class User extends BaseEntity {
    private static final User EMPTY_USER;
    static {
        EMPTY_USER = new Builder(
                EMPTY_STRING.getValue(), EMPTY_STRING.getValue())
                .id(UUID.fromString(EMPTY_UUID.getValue()))
                .createAt((long) EMPTY_TIME.getValue())
                .updateAt((long) EMPTY_TIME.getValue())
                .phoneNumber(EMPTY_STRING.getValue())
                .build();
    }

    private final String name;
    private final String email;
    private final String phoneNumber;

    private User(Builder builder) {
        super(
                builder.id,
                builder.createAt,
                builder.updateAt
        );
        name        = builder.name;
        email       = builder.email;
        phoneNumber = builder.phoneNumber;
    }

    public static User createEmptyUser() {
        return EMPTY_USER;
    }

    public static final class Builder {
        private UUID id       = UUID.randomUUID();
        private Long createAt = System.currentTimeMillis();
        private Long updateAt = System.currentTimeMillis();
        private final String name;
        private final String email;
        private String phoneNumber = EMPTY_STRING.getValue();

        public Builder(String name, String email) {
            this.name  = name;
            this.email = email;
        }

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }
        public Builder createAt(Long createAt) {
            this.createAt = createAt;
            return this;
        }
        public Builder updateAt(Long updateAt) {
            this.updateAt = updateAt;
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
