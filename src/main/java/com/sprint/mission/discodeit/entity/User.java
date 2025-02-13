package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Getter
// 직렬화하려는 클래스가 Serializable 인터페이스를 구현해야 한다.
public class User extends BaseEntity implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;
        //
        private String username;
        private String email;
        private String password;
        private final UUID userStatusId;

        public User(String username
                , String email
                , String password
                , UUID userStatusId){
            this.username = username;
            this.email = email;
            this.password = password;
            this.userStatusId = userStatusId;
        }

    // lombok
//    public String getNickname(){
//        return nickname;
//    }

    // TODO email, password 필드 추가로 setter 더 추가해야 하고, Service 차원에서의 수정도 필요하다.

    // setter 의 이름 변경하기
    // BaseEntity 의 refreshUpdateAt() 를 도메인 차원에서 조절해야 하나?
    // Service 레이어에서 일일이 부르는 게 뭔가 "번거롭다", "빼먹을 수도 있겠다" 라는 생각이 들긴 하네
    public void setUsername(String username){
        if(username == null || username.trim().isEmpty()){
            throw new IllegalArgumentException("username 은 공백일 수 없습니다.");
        }
        this.username = username;
    }

    // ㅠㅠ : 이러면 또 검증이 조각조각인데... 다른 dto의 검증을 못쓰잖아
    // 검증 패키지는 언제 나눌 수 있을까...
    public void setUserEmail(String email){
        if(email == null || !email.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
            throw new IllegalArgumentException("email 이 잘못되었습니다.");
        }
        this.email = email;
    }

    public void setUserPassword(String password){
        if(password == null || password.trim().isEmpty()){
            throw new IllegalArgumentException("password 가 잘못되었습니다.");
        }
        this.password = password;
    }

    // DTO 를 통해 response 반환
//    @Override
//    public String toString(){
//        return "\n"
//                + "nickname : " + username
//                + "\n"
//                + "User id : " + getId()
//                + "\n"
//                + "create at : " + getCreatedAt()
//                + "\n"
//                + "updated at : " + getUpdatedAt()
//                + "\n";
//    }
}
