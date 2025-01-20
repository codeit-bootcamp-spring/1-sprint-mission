package com.sprint.mission.discodeit.entity.user.entity;


import com.google.common.base.Preconditions;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class UserName implements Serializable {

    public static final int NAME_MIN_LENGTH = 3;
    public static final int NAME_MAX_LENGTH = 20;
    @Serial
    private static final long serialVersionUID = 8759111145208252347L;

    @Size(
            min = NAME_MIN_LENGTH, max = NAME_MAX_LENGTH,
            message = "'${validatedValue}' must be between {min} and {max} characters long"
    )
    @NotNull    //TODO : => NotBlank , 테스트 코드 수정 후 변경
    private final String name;

    public UserName(String name) {
        this.name = name;
    }

    public static UserName createFrom(String username) {
        Preconditions.checkNotNull(username);
        return new UserName(username);
    }

    public UserName changeName(String name) {
        Preconditions.checkNotNull(name);
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
