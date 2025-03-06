package com.sprint.mission.discodeit.vo;

import com.sprint.mission.discodeit.repository.UserRepository;

import java.io.Serial;
import java.io.Serializable;

public class Email implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;


    // 이메일
    private String email;

    // 이메일 정규식
    public static final String EMAIL_REGEX =
            "^[a-zA-Z0-9_+&*-]+(?:\\." +
                    "[a-zA-Z0-9_+&*-]+)*@" +
                    "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                    "A-Z]{2,7}$";


    // 생성자
    public Email(String email){
        email = emailValidation(email);
        this.email = email;
    }

    // 이메일 유효성 검사
    public String emailValidation(String email) {

        email = checkNull(email);

        if (matchEmail(email)){
            throw new IllegalArgumentException("올바르지 않은 이메일 형식입니다.");
        }

        return email;
    }

    // 이메일이 null인지 확인하고 아니라면 trim()하여 반환
    public String checkNull(String email) {
        if (email == null || email.isBlank()){
            throw new IllegalArgumentException("이메일을 입력해주세요.");
        }

        return email.trim();
    }

    // 이메일 형식 확인
    public boolean matchEmail(String email) {
        
        // 이메일 정규식 충족하지 않을 때 true 리턴
        return !email.matches(EMAIL_REGEX);
    }


    @Override
    public String toString(){
        return email;
    }
}
