package com.spirnt.mission.discodeit.exception.User;

import com.spirnt.mission.discodeit.exception.ErrorCode;
import java.util.Map;

/**
 * User를 생성하거나 수정할 때 email이나 username이 중복되는 경우 발생하는 예외
 */
public class UserAlreadyExistException extends UserException {

    public UserAlreadyExistException(Map<String, Object> details) {
        super(ErrorCode.DUPLICATE_USER, details);
    }
}
