package com.sprint.mission.discodeit.common;

import java.util.regex.Pattern;

public class Email {
    private String email;
    private boolean verified;

    public Email(String email) {
        if(validate(email)){
            this.email = email;
            this.verified = false; // 인증 확인 로직
        }else {
            throw new IllegalArgumentException("잘못된 Email 형식입니다.");
        }
    }

    public void changeVerified() {
        // 이메일 인증 상태 변경(인증 완료)을 위함
        this.verified = true;
    }

    public boolean validate(String email){
        return Pattern.matches("^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$", email);
    }

    public String getEmailAddress() {
        return email;
    }

    public boolean getEmailVerified(){
        return verified;
    }
}
