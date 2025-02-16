package com.sprint.mission.discodeit.entity;

import static com.sprint.mission.discodeit.constant.StringConstant.EMPTY_STRING;
import static com.sprint.mission.discodeit.constant.StringConstant.EMPTY_UUID;
import static com.sprint.mission.discodeit.constant.StringConstant.PHONE_NUMBER_FORMAT;

import com.sprint.mission.discodeit.constant.StringConstant;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Builder
@Getter
@EqualsAndHashCode(of = "id")
@ToString
@Accessors(fluent = true)
public class User implements Serializable {

    @Getter(AccessLevel.NONE)
    @Serial
    private static final long serialVersionUID = -936608933295057318L;

    /**
     * Field: {@code EMPTY_USER} is literally empty static User object
     */
    public static final User EMPTY_USER;
    private final UUID id;
    private final Instant createAt;
    private final Instant updateAt;
    private final String name;
    private final String email;
    private final String phoneNumber;
    private final UserStatus userStatus;
    private final ReadStatus readStatus;
    private final BinaryContent image;

    static {
        EMPTY_USER = new User(
            UUID.fromString(EMPTY_UUID.getValue()),
            Instant.parse(StringConstant.EMPTY_TIME.getValue()),
            Instant.parse(StringConstant.EMPTY_TIME.getValue()),
            EMPTY_STRING.getValue(),
            EMPTY_STRING.getValue(),
            EMPTY_STRING.getValue(),
            UserStatus.EMPTY_USER_STATUS,
            ReadStatus.EMPTY_READ_STATUS,
            BinaryContent.EMPTY_BINARY_CONTENT
        );
    }

    @Builder
    private User(UUID id, Instant createAt, Instant updateAt, String name, String email,
        String phoneNumber, UserStatus userStatus, ReadStatus readStatus, BinaryContent image) {
        this.id = id;
        this.createAt = createAt;
        this.updateAt = updateAt;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.userStatus = userStatus;
        this.readStatus = readStatus;
        this.image = image;
    }

    public static UserBuilder builder(String name, String email) {
        UUID userId = UUID.randomUUID();
        return new UserBuilder().name(name).email(email)
            .id(userId)
            .createAt(Instant.now())
            .createAt(Instant.now())
            .phoneNumber(PHONE_NUMBER_FORMAT.getValue())
            .userStatus(UserStatus.createUserStatus(userId))
            .readStatus(ReadStatus.createReadStatus(userId))
            .image(BinaryContent.createBinaryContent(userId));
    }
}
