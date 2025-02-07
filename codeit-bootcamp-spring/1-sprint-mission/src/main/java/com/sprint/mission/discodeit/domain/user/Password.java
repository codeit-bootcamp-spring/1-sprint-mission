package com.sprint.mission.discodeit.domain.user;

import com.sprint.mission.discodeit.domain.user.validation.PasswordValidator;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(of = "value")
public class Password {

    private final String value;

    public Password(String value) {
        PasswordValidator.validate(value);
        this.value = value;
    }

}
