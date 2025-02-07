package com.sprint.mission.discodeit.entity;
import lombok.Getter;

import java.io.Serializable;
import java.util.UUID;

public class User implements Serializable, Entity {
    private static final long serialVersionUID = 1L;

    // 일부 속성 세터 추가 가능성 있어 속성마다 @Getter 명시적으로 작성.
    @Getter private final long createdAt;
    @Getter private long updatedAt;
    @Getter private UUID id;
    @Getter private String email;
    @Getter private String userName;
    @Getter private String password;

    public User(String userName, String email, String password) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.userName = userName;
    }


    //업데이트시간 수정
    public void setUpdatedAt(){this.updatedAt = System.currentTimeMillis();}
    //유저이름 변경
    public void setUserName(String userName) {
        this.userName = userName;
        this.setUpdatedAt();
    }
    //유저 이메일 변경
    public void setEmail(String email) {
        this.email = email;
    }
    //유저 비밀번호 변경
    public void setPassword(String password) {
        this.password = password;
    }
}
