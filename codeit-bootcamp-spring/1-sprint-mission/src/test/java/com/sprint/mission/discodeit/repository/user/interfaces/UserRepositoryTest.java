package com.sprint.mission.discodeit.repository.user.interfaces;

import static org.junit.jupiter.api.Assertions.*;

import com.sprint.mission.discodeit.domain.user.Email;
import com.sprint.mission.discodeit.domain.user.User;
import com.sprint.mission.discodeit.fake.FakeFactory;
import com.sprint.mission.discodeit.fake.domain.user.StubUser;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class UserRepositoryTest {

    private final UserRepository userRepository = FakeFactory.getUserRepository();
    private final User user = StubUser.generateUser();

    @Test
    void 사용중인_이메일_검증() {
        // given
        userRepository.save(user);
        String existedEmailValue = user.getEmailValue();
        // when
        boolean result = userRepository.isExistByEmail(new Email(existedEmailValue));
        // then
        Assertions.assertThat(result).isTrue();
    }
}
