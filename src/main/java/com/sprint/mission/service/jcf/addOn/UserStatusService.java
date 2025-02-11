package com.sprint.mission.service.jcf.addOn;

import com.sprint.mission.entity.addOn.UserStatus;
import com.sprint.mission.repository.jcf.addOn.UserStatusRepository;
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

    public Optional<UserStatus> findById(UUID userId){
        return userStatusRepository.findById(userId);
    }

    public List<UserStatus> findAll(){
        return userStatusRepository.findAll();
    }

    // 이건 DTO가 필요없는거 같은데
    public void update(UUID userId){
        userStatusRepository.findById(userId).ifPresent((updatingUserStatus) -> {
            updatingUserStatus.join();
            userStatusRepository.save(updatingUserStatus);
        });
    }

    public void delete(UUID userId){
        userStatusRepository.delete(userId);
    }
}
