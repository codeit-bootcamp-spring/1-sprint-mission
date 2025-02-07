package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.UserStatusCreateDTO;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateDTO;
import com.sprint.mission.discodeit.dto.response.UserStatusResponseDTO;
import com.sprint.mission.discodeit.dto.response.user.UserDTO;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.interfacepac.UserRepository;
import com.sprint.mission.discodeit.repository.interfacepac.UserStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserStatusService {

    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;

    public UserStatusResponseDTO create(UserStatusCreateDTO createDTO){
        //사용자 조회
        User user = userRepository.findById(createDTO.userId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        //중복 체크
        if(userStatusRepository.existsByUser(user)){
            throw new IllegalArgumentException("UserStatus already exists");
        }
        // 새로운 userStatus 생성, 저장
        UserStatus userStatus = new UserStatus(user, createDTO.lastActiveAt());
        userStatusRepository.save(userStatus);

        return new UserStatusResponseDTO(
                userStatus.getId(),
                userStatus.getUser().getId(),
                userStatus.getLastSeenAt()
        );
    }

    public UserDTO find(UUID userId){
        //사용자 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        //사용자 상태 조회(온라인 여부 확인)
        UserStatus userStatus = userStatusRepository.findByUser(user)
                .orElse(new UserStatus(user, Instant.EPOCH));
        boolean isOnline = userStatus.isOnline();
        // 응답 DTO 변환 후 반란
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                isOnline
        );
    }

    public List<UserDTO>findAll(){
        //모든 사용자 조회
        List<User> users = userRepository.findAll();
        //사용자 목록이 비어 있으면 빈 리스트 반환
        if(users.isEmpty()){
            return Collections.emptyList(); // 왜 arrayList?
        }
        //사용자 상태 조회, 온라인 여부 포함
        return users.stream()
                .map(user -> {
                    UserStatus userStatus = userStatusRepository.findByUser(user).orElse(null);
                    boolean isOnline = (userStatus != null) && userStatus.isOnline();

                    return new UserDTO(
                            user.getId(),
                            user.getUsername(),
                            user.getEmail(),
                            user.getCreatedAt(),
                            user.getUpdatedAt(),
                            isOnline
                    );
                })
                .toList();
    }

    public UserStatusResponseDTO update(UserStatusUpdateDTO updateDTO){
        //userStatus 조회
        UserStatus userStatus = userStatusRepository.findById(updateDTO.userStatusId())
                .orElseThrow(() -> new IllegalArgumentException("UserStatus not found"));
        //마지막 활동 시간 업데이트
        userStatus.updateLastSeenAt(updateDTO.lastActiveAt());
        //저장
        userStatusRepository.save(userStatus);
        // 응답 DTO 변환 후 반환
        return new UserStatusResponseDTO(
                userStatus.getId(),
                userStatus.getUser().getId(),
                userStatus.getLastSeenAt()
        );
    }

    public UserStatusResponseDTO updateByUserId(UUID userId, Instant lastActiveAt){
        //id로 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        //userStatus 조회 (없으면 새로 생성)
        UserStatus userStatus = userStatusRepository.findByUser(user)
                .orElseGet(() -> {
                    UserStatus newUserStatus = new UserStatus(user, Instant.EPOCH);
                    userStatusRepository.save(newUserStatus);
                    return newUserStatus;
                });
        //마지막 활동 시간 업데이트
        userStatus.updateLastSeenAt(lastActiveAt);
        // 저장
        userStatusRepository.save(userStatus);
        // DTO 변환, 반환
        return new UserStatusResponseDTO(
                userStatus.getId(),
                userStatus.getUser().getId(),
                userStatus.getLastSeenAt()
        );
    }

    public void delete(UUID userStatusId){
        //userStatus 조회
        if(!userStatusRepository.existsById(userStatusId)){
            throw new IllegalArgumentException("UserStatus not found");
        }
        //삭제
        userStatusRepository.deleteById(userStatusId);

        System.out.println("UserStatus deleted: " + userStatusId);
    }

}
