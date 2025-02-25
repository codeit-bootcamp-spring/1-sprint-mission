package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class BasicUserStatusService implements UserStatusService {
    @Autowired
    UserStatusRepository userStatusRepository;
    UserRepository userRepository;

    @Override
    public UserStatus create(UserStatusCreateRequest request){
        // 파라미터 예외 처리 -> 레포지토리.save (변수에 dto 파라미터 할당, 객체 생성, 레포지토리.save(객체 전달))
        if(!userRepository.existsById(request.userId())){
            throw new NoSuchElementException("아이디가 " + request.userId() + "인 회원이 존재하지 않습니다.");
        }
        List<UserStatus> userStatusList = userStatusRepository.findAllByUserId(request.userId());
        for (UserStatus status : userStatusList){
            if (status.getUserId().equals(request.userId())){
                throw new IllegalArgumentException("해당 User Status가 이미 존재합니다.");
            }
        }
        UUID userId = request.userId();
        Instant lastActiveAt = request.lastActiveAt();
        UserStatus userStatus = new UserStatus(userId, lastActiveAt); // TODO : 롬복 @RequiredArgsConstructor는 final과 not null 필드에 대한 생성자를 생성하므로 이 필드에 대해 파라미터로 전달하면 된다. -> userId, lastActiveAt!
        return userStatusRepository.save(userStatus);
    }

    @Override
    public UserStatus find(UUID userStatusId){
        return userStatusRepository.findById(userStatusId)
                .orElseThrow(() -> new NoSuchElementException("아이디가" + userStatusId + "인 회원 상태가 존재하지 않습니다."));
    }

    @Override
    public List<UserStatus> findAllByUserId(UUID userId){
        if(!userRepository.existsById(userId)) {
            throw new NoSuchElementException("아이디가" + userId + "인 회원이 존재하지 않습니다.");
        }
        return userStatusRepository.findAllByUserId(userId);
    }

    @Override
    public UserStatus update(UUID userStatusId, UserStatusUpdateRequest request){
        Instant lastActiveAt = request.newLastActiveAt();
        UserStatus userStatus = userStatusRepository.findById(userStatusId)
                .orElseThrow(() -> new NoSuchElementException("아이디가 " + userStatusId + "인 회원 상태가 존재하지 않습니다."));
        userStatus.update(lastActiveAt);
        return userStatusRepository.save(userStatus);
    }

    @Override
    public UserStatus updateByUserId(UUID userId, UserStatusUpdateRequest request){
        if(!userRepository.existsById(userId)) {
            throw new NoSuchElementException("아이디가" + userId + "인 회원이 존재하지 않습니다.");
        }
        Instant lastActiveAt = request.newLastActiveAt();
        UserStatus userStatus = userStatusRepository.findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException("회원 상태가 존재하지 않습니다."));

        userStatus.update(lastActiveAt);
        return userStatusRepository.save(userStatus);
    }

    @Override
    public void delete(UUID userStatusId){
        if(!userRepository.existsById(userStatusId)){
            throw new NoSuchElementException("아이디가" + userStatusId + "인 회원 상태가 존재하지 않습니다.");
        }
        userStatusRepository.deleteById(userStatusId);
    }
}

