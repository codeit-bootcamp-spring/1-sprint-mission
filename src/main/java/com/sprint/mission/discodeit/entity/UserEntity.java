package com.sprint.mission.discodeit.entity;

public class UserEntity extends CommonEntity {
    // 유저명
    private String username;
    // 이메일
    private String email;
    // 유저 삭제 플래그 userDelFlg ( 0 : default, 1 : deleted )
    private String userDelFlg;
    // 유저 권한 (Normal : 허용된 Channel 에서만 메시지 작성 가능(CRUD), Admin : 모든 Channel 에서 메시지 작성 가능 (CRUD))
    private String authority;

    public UserEntity(String username, String email){
        super();
        this.username = username;
        this.email = email;
        this.authority = "Normal";
        this.userDelFlg = "0";
    }

    public String getUsername() {
        return username;
    }

    public void updateUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void updateEmail(String email){
        this.email = email;
    }

    public String getAuthority() {
        return authority;
    }

    public void updateAuthority(String authority){
        this.authority = authority;
    }

    public String getUserDelFlg() {
        return userDelFlg;
    }

    public void updateUserDelFlg(String flg) {
        this.userDelFlg = flg;
    }

}
