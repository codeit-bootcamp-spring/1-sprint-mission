package com.sprint.mission.service.jcf;

import com.sprint.mission.entity.UserStatus;
import com.sprint.mission.repository.jcf.UserStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserStatusService {

    private final UserStatusRepository statusRepository;
    private final UserStatusRepository userStatusRepository;

    // DTO로 파라미터 그룹화...???
    public void create(UUID userId){
        if (statusRepository.isExistById(userId)){
            throw new RuntimeException();
        } else {
            statusRepository.save(new UserStatus(userId));
        }
    }


    public UserStatus findById(UUID userId){
        return statusRepository.findById(userId);
    }

    public List<UserStatus> findAll(){
        return statusRepository.findAll();
    }

    // 이건 DTO가 필요없는거 같은데
    public void update(UUID userId){
        UserStatus updatingUserStatus = statusRepository.findById(userId);
        updatingUserStatus.join();
        statusRepository.save(updatingUserStatus);
    }

    public void delete(UUID userId){
        userStatusRepository.delete(userId);
    }


}
