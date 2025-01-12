package com.sprint.mission.discodeit.exception;

public class UserValidationException extends Exception{
    public UserValidationException(){
        super("사용자 생성에 실패했습니다.");
    }

    public UserValidationException(String message){
        super(message);
    }
}
