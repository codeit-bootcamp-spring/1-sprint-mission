package com.sprint.mission.discodeit.exception.user;


import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.HashMap;

public class UserNotFoundException extends UserException{
    // TODO : 여기에 전달되는 ErrorCode.USER_NOT_FOUND는 예외 처리 될 때 어디에 쓰이는지 궁금함 (ErrorCode Enum 클래스의 존재 의미가 궁금)
    public UserNotFoundException(HashMap<String, Object> details){
        super(ErrorCode.USER_NOT_FOUND, details);
    }
}
