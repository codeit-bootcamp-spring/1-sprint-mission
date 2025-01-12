package com.sprint.mission.discodeit.common;

import java.util.regex.Pattern;

public class Nickname implements Name {
    private String name;

    public Nickname(String name) {
        if(validate(name)) {
            // 크게 닉네임은 잡을 거 없음
            this.name = name;
        }else{
            throw new IllegalArgumentException("잘못된 Nickname 형식입니다.");
        }
    }

    public boolean setNickname(String nickname){
        if(validate(nickname)){
            this.name = nickname;
            return true;
        }else{
            return false;
        }
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean validate(String name) {
        // 1글자 이상, 32글자 미만
        // TODO: 가능하다면 단어 제한까지 해보고 싶다!
        String regex = "^.{1,31}$";
        return Pattern.matches(regex, name);
    }
}
