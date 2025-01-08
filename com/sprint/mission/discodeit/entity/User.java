package sprint.mission.discodeit.entity;

import java.util.Objects;

public class User {
    private final Common common;
    private final String name;

    private User() {
        this(Common.createCommon(), "");
    }
    private User(Common common) {
        this(common, "");
    }
    private User(String name) {
        this(Common.createCommon(), name);
    }
    private User(Common common, String name) {
        this.common = common;
        this.name   = name;
    }

    public static User createUser(String name) {
        return new User(name);
    }
    public static User createUser(Common common, String name) {
        return new User(common, name);
    }
    public static User createEmptyUser() {
        return new User(Common.createEmptyCommon());
    }

    public Common getCommon() {
        return common;
    }
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(common, user.common) && Objects.equals(name, user.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(common, name);
    }

    @Override
    public String toString() {
        return "User{" +
                "common=" + common +
                ", name='" + name + '\'' +
                '}';
    }
}
