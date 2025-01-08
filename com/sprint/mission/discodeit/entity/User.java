package discodeit.entity;

public class User {
    private final Common common;

    private User() {
        common = Common.createCommon();
    }

    public static User createUser() {
        return new User();
    }

    public Common getCommon() {
        return common;
    }
}
