package com.sprint.mission.discodeit.validation;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.status.UserStatus;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ExceptionText;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserStatusValidator {

    private final UserStatusRepository userStatusRepository;

    public void CheckUserStatus(UUID id){
       if(userStatusRepository.findById(id) == null){
           throw new CustomException(ExceptionText.USER_NOT_FOUND);
       }
    }

    public void nullCheckUserHasUserStatus(UUID userId){
        if(userStatusRepository.findUserStatusIdByUserId(userId) == null){
            throw new CustomException(ExceptionText.STATUS_NOT_FOUND);
        }
    }

    public void CheckUserHasUserStatus(UUID userId){
        if(userStatusRepository.findUserStatusIdByUserId(userId) !=null){
            throw new CustomException(ExceptionText.STATUS_DUPLICATE);
        }
    }
//    각각의 메서드에서 불리안 값의 리턴이 아닌 직접 예외를 던져주는 로직으로
//    User에 해당하는 UserStatus의 존재 여부베대한 메서드가 하나가 아닌 2개로 늘어나게 됨... 우야지...


}
