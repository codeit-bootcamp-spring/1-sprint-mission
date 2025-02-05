package com.sprint.mission.discodeit.domain.user;

import com.sprint.mission.discodeit.domain.user.validation.EmailValidator;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode(of = {"value"})
@ToString(of = {"value"})
public class Email {

    private final String value;

    public Email(String email) {
        EmailValidator.valid(email);
        this.value = email;
    }
}
