package com.sprint.mission.discodeit.domain.user;

import com.sprint.mission.discodeit.domain.user.validation.NicknameValidator;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode(of = {"value"})
@ToString(of = {"value"})
public class Nickname {

    private final String value;

    public Nickname(String value) {
        NicknameValidator.validate(value);
        this.value = value;
    }

}
