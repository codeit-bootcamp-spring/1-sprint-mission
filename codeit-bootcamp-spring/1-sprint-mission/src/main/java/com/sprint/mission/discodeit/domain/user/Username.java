package com.sprint.mission.discodeit.domain.user;

import com.sprint.mission.discodeit.domain.user.validation.UsernameValidator;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode(of = "value")
@ToString(of = "value")
public class Username {

    private final String value;

    public Username(String value) {
        UsernameValidator.validate(value);
        this.value = value.trim();
    }

}
