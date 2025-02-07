package com.sprint.mission.discodeit.domain.user;

import lombok.ToString;

@ToString(of = "value")
public class Password {

    private String value;

    public Password(String value) {
        this.value = value;
    }

    public void changePassword(String encodedPassword) {
        setValue(encodedPassword);
    }

    private void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
