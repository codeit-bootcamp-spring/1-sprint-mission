package com.sprint.mission.discodeit.fake.domain.user;

import com.sprint.mission.discodeit.application.dto.user.joinUserRequestDto;
import com.sprint.mission.discodeit.domain.user.BirthDate;
import com.sprint.mission.discodeit.domain.user.Email;
import com.sprint.mission.discodeit.domain.user.Nickname;
import com.sprint.mission.discodeit.domain.user.Password;
import com.sprint.mission.discodeit.domain.user.User;
import com.sprint.mission.discodeit.domain.user.Username;
import com.sprint.mission.discodeit.domain.user.enums.EmailSubscriptionStatus;
import java.time.LocalDate;

public class StubUser {

    private static final String USER_NAME = "user123";
    private static final String NICK_NAME = "User1";
    private static final String EMAIL = "test@example.com";
    private static final String PASSWORD = "A1b@5678";
    private static final LocalDate BIRTH_DATE = LocalDate.of(2000, 1, 1);

    public static User generateUser() {
        return new User(
                new Nickname(USER_NAME),
                new Username(USER_NAME),
                new Email(EMAIL),
                new Password(PASSWORD),
                new BirthDate(BIRTH_DATE),
                EmailSubscriptionStatus.UNSUBSCRIBED
        );
    }

    public static joinUserRequestDto generateJoinRequestDto() {
        return new joinUserRequestDto(NICK_NAME, USER_NAME, EMAIL, PASSWORD, BIRTH_DATE);
    }

    public static joinUserRequestDto generateJoinRequestByUsername(String username) {
        return new joinUserRequestDto(NICK_NAME, username, EMAIL, PASSWORD, BIRTH_DATE);
    }

    public static joinUserRequestDto generateJoinRequestByEmail(String email) {
        return new joinUserRequestDto(NICK_NAME, USER_NAME, email, PASSWORD, BIRTH_DATE);
    }
}
