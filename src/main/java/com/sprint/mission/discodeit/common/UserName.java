package com.sprint.mission.discodeit.common;

import java.util.regex.Pattern;

public class UserName implements Name {
    private String name;

    public UserName(String name) {
        if(validate(name)) {
            // 대문자 입력 자체를 막지는 않지만, 아이디의 알파벳은 소문자로 전체 변환
            this.name = name.toLowerCase();
        }else{
            throw new IllegalArgumentException("잘못된 Username 형식입니다.");
        }
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean validate(String name) {
        // 2자 이상 32자 미만
        // 알파벳, 숫자, _, . 만 허용
        // 대소문자 구분 X
        String regex = "^(?!.*[.]{2})[a-zA-Z0-9](?:[a-zA-Z0-9_.]{0,30}[a-zA-Z0-9])?$";
        return Pattern.matches(regex, name);
    }
}
