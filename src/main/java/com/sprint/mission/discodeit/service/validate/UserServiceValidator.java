package com.sprint.mission.discodeit.service.validate;

import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;

import java.util.Optional;

public class UserServiceValidator implements ServiceValidator<User> {
    @Override
    public User entityValidate(User user) {
        return Optional.ofNullable(user)
                .orElseThrow(UserNotFoundException::new);
    }

}
