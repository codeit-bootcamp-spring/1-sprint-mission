package com.sprint.mission.discodeit.common;

// TODO: 아무래도 비밀번호 넣을 때는 암호화가 필요함!!
public class Password {
    private String passwd;

    public Password(String passwd) {
        this.passwd = passwd;
    }

    public String getPasswd() {
        return passwd;
    }

}
