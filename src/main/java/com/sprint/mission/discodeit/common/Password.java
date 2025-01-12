package com.sprint.mission.discodeit.common;

import java.util.regex.Pattern;

// TODO: 아무래도 비밀번호 넣을 때는 암호화가 필요함!!
public class Password {
    private String passwd;

    public Password(String passwd){
        if(validate(passwd)){
            this.passwd = passwd;
        }else{
            throw new IllegalArgumentException("Invalid Password");
        }
    }

    public String getPasswd() {
        return passwd;
    }

    public boolean validate(String passwd){
        String regex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@!%*#?&])[A-Za-z\\d@!%*#?&]{8,}$";
        return Pattern.matches(regex, passwd);
    }
}
