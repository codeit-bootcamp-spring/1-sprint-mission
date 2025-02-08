package com.sprint.mission.discodeit.fake.domain.user;

import com.sprint.mission.discodeit.application.dto.user.JoinUserReqeustDto;
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
        return User.builder()
            .username(new Username(USER_NAME))
            .nickname(new Nickname(NICK_NAME))
            .email(new Email(EMAIL))
            .password(new Password(PASSWORD))
            .birthDate(new BirthDate(BIRTH_DATE))
            .emailSubscriptionStatus(EmailSubscriptionStatus.SUBSCRIBED)
            .build();
    }

    public static JoinUserReqeustDto generateJoinRequestDto() {
        return new JoinUserReqeustDto(NICK_NAME, USER_NAME, EMAIL, PASSWORD, BIRTH_DATE);
    }

    public static JoinUserReqeustDto generateJoinRequestByUsername(String username) {
        return new JoinUserReqeustDto(NICK_NAME, username, EMAIL, PASSWORD, BIRTH_DATE);
    }

    public static JoinUserReqeustDto generateJoinRequestByEmail(String email) {
        return new JoinUserReqeustDto(NICK_NAME, USER_NAME, email, PASSWORD, BIRTH_DATE);
    }
}
