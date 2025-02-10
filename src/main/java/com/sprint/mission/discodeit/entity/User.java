package com.sprint.mission.discodeit.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import static com.sprint.mission.discodeit.constant.IntegerConstant.EMPTY_TIME;
import static com.sprint.mission.discodeit.constant.StringConstant.EMPTY_STRING;
import static com.sprint.mission.discodeit.constant.StringConstant.EMPTY_UUID;

public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = -936608933295057318L;

    /** Field: {@code EMPTY_USER} is literally empty static User object */
    public static final User EMPTY_USER;
    private final UUID   id;
    private final Long   createAt;
    private final Long   updateAt;
    private final String name;
    private final String email;
    private final String phoneNumber;

    static {
        EMPTY_USER = new Builder(
                EMPTY_STRING.getValue(), EMPTY_STRING.getValue())
                .id(UUID.fromString(EMPTY_UUID.getValue()))
                .createAt((long) EMPTY_TIME.getValue())
                .updateAt((long) EMPTY_TIME.getValue())
                .phoneNumber(EMPTY_STRING.getValue())
                .build();
    }
    private User(Builder builder) {
        id          = builder.id;
        createAt    = builder.createAt;
        updateAt    = builder.updateAt;
        name        = builder.name;
        email       = builder.email;
        phoneNumber = builder.phoneNumber;
    }

    /** Returns: {@code EMPTY_USER} which is literally empty static User object */
    public static User createEmptyUser() {
        return EMPTY_USER;
    }

    public static final class Builder {
        private UUID id = UUID.randomUUID();
        private Long createAt = System.currentTimeMillis();
        private Long updateAt = System.currentTimeMillis();
        private final String name;
        private final String email;
        private String phoneNumber = EMPTY_STRING.getValue();

        public Builder(String name, String email) {
            this.name = name;
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

    public UUID getId() {
        return id;
    }
    public Long getCreateAt() {
        return createAt;
    }
    public Long getUpdateAt() {
        return updateAt;
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
        User that = (User) o;
        return Objects.equals(id, that.id);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", createAt=" + createAt +
                ", updateAt=" + updateAt +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
