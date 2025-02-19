package com.sprint.mission.service.jcf.addOn;

import com.sprint.mission.entity.addOn.UserStatus;
import com.sprint.mission.entity.main.User;
import com.sprint.mission.repository.jcf.addOn.UserStatusRepository;
import com.sprint.mission.service.exception.NotFoundId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserStatusService {

    private final UserStatusRepository userStatusRepository;

    // DTO로 파라미터 그룹화...??? 필요없다
    public void create(UUID userId){
        if(userStatusRepository.isExistById(userId)){
            throw new RuntimeException("cannot create userStatus : already exist userStatus");
        } else {
            userStatusRepository.save(new UserStatus(userId));
        }
    }

    public UserStatus findById(UUID userId){
        return userStatusRepository.findById(userId).orElseThrow(() -> new NotFoundId("userId에 맞는 userstatus가 존재하지 않습니다"));
        // 이걸 그냥 빈 Userstatus로 반환할지 오류 터트릴지 고민
    }

    public List<UserStatus> findAll(){
        return userStatusRepository.findAll();
    }

    public Map<User, UserStatus> findStatusMapByUserList(List<User> userList){
        return userStatusRepository.findStatusMapByUser(userList);
    }

    // 이건 DTO가 필요없는거 같은데
    //[ ] userId 로 특정 User의 객체를 업데이트합니다.
    // ??? 오타인걸로 생각 userstatus 업데이트
    public void update(UUID userId){
        userStatusRepository.findById(userId).ifPresentOrElse((updatingUserStatus) -> {
            updatingUserStatus.join();
            userStatusRepository.save(updatingUserStatus);
        }, NotFoundId::new);
    }

    public void delete(UUID userId){
        if (userStatusRepository.isExistById(userId)) throw new NotFoundId();
        else userStatusRepository.delete(userId);
    }
}
