package com.sprint.mission.discodeit.common;

import lombok.Getter;

@Getter
public class Email {
    private String email;
    private boolean verified;   // 이메일 인증

    public Email(String email) {
        this.email = email;
        this.verified = false; // 인증 확인 로직
    }

    public void changeVerified() {
        // 이메일 인증 상태 변경(인증 완료)을 위함
        this.verified = true;
    }

    public void changeEmailAddr(String email) {
        this.email = email;
        this.verified = false;
    }
}
