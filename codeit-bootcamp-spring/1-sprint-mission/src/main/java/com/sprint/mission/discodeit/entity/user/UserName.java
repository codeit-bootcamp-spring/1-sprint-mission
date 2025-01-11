package com.sprint.mission.discodeit.entity.user;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Objects;

public class UserName {

    public static final int NAME_MIN_LENGTH = 3;
    public static final int NAME_MAX_LENGTH = 20;

    @Size(min = NAME_MIN_LENGTH, max = NAME_MAX_LENGTH, message = "'${validatedValue}' must be between {min} and {max} characters long")
    @NotNull    //TODO : => NotBlank , 테스트 코드 수정 후 변경
    private final String name;

    public UserName(String name) {
        this.name = name;
    }

    public UserName changeName(String name) {
        return new UserName(name);
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserName userName = (UserName) o;
        return Objects.equals(name, userName.name);
    }


    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

}
