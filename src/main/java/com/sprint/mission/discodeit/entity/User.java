package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class User {
    private final UUID id; // 유저 식별을 위한 UUID
    private final Long createdAt; // 생성 시점 시간
    private static Long updatedAt; // 정보 변경 시점 시간

    // 추가적으로 필요한 필드값들은 메소드 만들면서 추가하도록 하자.
    // 지금 넣어봤자 계속 바뀔 것 같다.
    private String userNickName; // 유저의 닉네임.
    private String userEmail; // 유저의 이메일. 로그인할때 사용하도록 하자.
    private String password; // 로그인시 필요한 비밀번호

    private User(String userEmail, String userNickName, String password) {
        id = UUID.randomUUID();
        createdAt = System.currentTimeMillis();
        this.userNickName = userNickName;
        this.userEmail = userEmail;
        this.password = password;
    }

    private User(String userEmail, String userNickName, String password, long updatedTimeStamp) {
        id = UUID.randomUUID();
        createdAt = System.currentTimeMillis();
        this.userNickName = userNickName;
        this.userEmail = userEmail;
        this.password = password;
        this.updatedAt = System.currentTimeMillis();
    }

    public static User createUser(String userEmail, String userNickName, String password, boolean isUpdated) {
        if (isUpdated) {
            return new User(userEmail, userNickName, password, System.currentTimeMillis());
        }
        return new User(userEmail, userNickName, password);
    }

    public UUID getId() {
        return id;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getPassword() {
        return password;
    }

    public void updatePassword(String newpassword) {
        password = newpassword;
    }

    public void updateUserNickName(String newnickname) {
        userNickName = newnickname;
    }

    public void updateUserEmail(String newemail) {
        userEmail = newemail;
    }

    public static String updateUpdatedAt(long newupdatedAt) {
        updatedAt = newupdatedAt;
        return updatedAt.toString();
    }
}
