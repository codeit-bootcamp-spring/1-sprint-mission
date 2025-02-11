package com.sprint.mission.discodeit.domain.user;

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

    @Override
    public String toString() {
        return "Password{" +
                "value='" + value + '\'' +
                '}';
    }
}
