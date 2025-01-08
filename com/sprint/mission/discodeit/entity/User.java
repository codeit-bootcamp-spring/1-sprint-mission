package discodeit.entity;

import java.util.Objects;

public class User {
    private final Common common;

    private User() {
        common = Common.createCommon();
    }

    public static User createUser() {
        return new User();
    }
    public static User createEmptyUser() {
        return new User();
    }

    public Common getCommon() {
        return common;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(common, user.common);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(common);
    }
}
