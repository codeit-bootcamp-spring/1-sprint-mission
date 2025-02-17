package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userstatus.UserIdDTO;
import com.sprint.mission.discodeit.dto.userstatus.CurrentTimeDTO;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusIdDTO;
import com.sprint.mission.discodeit.entity.status.UserStatus;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.validation.UserStatusValidator;
import com.sprint.mission.discodeit.validation.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {

    private final UserValidator userValidator;
    private final UserStatusRepository userStatusRepository;
    private final UserStatusValidator userStatusValidator;


    @Override
    public UserStatus created(UserIdDTO idData) {
        try {
            userValidator.CheckUser(idData.userId());
            userStatusValidator.CheckUserHasUserStatus(idData.userId());
            UserStatus user = new UserStatus(idData.userId());
            userStatusRepository.save(user);
            return user;
        }catch (CustomException e){
            System.out.println("userStatus 생성 실패 -> "+ e.getMessage());
            return null;
        }
    }

    @Override
    public UserStatus find(UUID id) {
        userStatusValidator.CheckUserStatus(id);
        return userStatusRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("UsetStatus with is " + id + "not found"));
    }

    @Override
    public List<UserStatus> findAll() {
        return userStatusRepository.findAll();
    }
    @Override
    public void update(UserStatusIdDTO idData, CurrentTimeDTO timeData) {
        UserStatus userStatus = find(idData.id());
        userStatus.updateLastAccessed(timeData.currentTime());
    }
    @Override
    //updateByUserId : userId 로 특정 User의 객체를 업데이트합니다.
    public void updateByUserId(UserIdDTO idData, CurrentTimeDTO timeData) {
        try {
            userValidator.CheckUser(idData.userId());
            userStatusValidator.nullCheckUserHasUserStatus(idData.userId());
            UUID userStatusId = userStatusRepository.findUserStatusIdByUserId(idData.userId());
            UserStatus userStatus = userStatusRepository.findById(userStatusId)
                            .orElseThrow(() -> new NoSuchElementException("UsetStatus with is " + idData.userId()+ "not found"));
            userStatus.updateLastAccessed(timeData.currentTime());
        }catch (CustomException e){
            System.out.println("userStatus Update 실패 -> "+ e.getMessage());
        }
    }

    @Override
    public void delete(UUID id){
        userStatusValidator.CheckUserStatus(id);
        userStatusRepository.delete(id);
    }
}
