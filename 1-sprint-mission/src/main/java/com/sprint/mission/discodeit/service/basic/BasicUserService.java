package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.user.UserCreateDTO;
import com.sprint.mission.discodeit.dto.request.user.UserUpdateDTO;
import com.sprint.mission.discodeit.dto.response.user.UserDTO;
import com.sprint.mission.discodeit.dto.request.user.UserProfileImageDTO;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.interfacepac.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.interfacepac.UserRepository;
import com.sprint.mission.discodeit.repository.interfacepac.UserStatusRepository;
import com.sprint.mission.discodeit.service.interfacepac.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final UserStatusRepository userStatusRepository;


    @Override
    public UserDTO create(UserCreateDTO userCreateDTO, UserProfileImageDTO userProfileImageDTO) {
        if (userRepository.existsByEmail(userCreateDTO.email()) ||
                userRepository.existsByUsername(userCreateDTO.username())) {
            throw new IllegalArgumentException("User with email " + userCreateDTO.email() + " already exists");
        }

        User newUser = new User(userCreateDTO.username(),userCreateDTO.email() ,userCreateDTO.password() );
        userRepository.save(newUser);

        assert userProfileImageDTO != null;
        if(userProfileImageDTO.imageData() != null) {
            BinaryContent binaryContent = new BinaryContent(newUser, userProfileImageDTO.fileName(),userProfileImageDTO.imageData());
            binaryContentRepository.save(binaryContent); // 레포지토리 미구현 나중에 수정 해야함.
        }

        UserStatus userStatus = new UserStatus(newUser);
        System.out.println("User created: " + userCreateDTO.username() + " (email: " + userCreateDTO.email() + ")");
        userStatusRepository.save(userStatus); // 레포지토리 미구현 나중에 수정해야함.
        boolean isOnline = userStatus.isOnline();
        return new UserDTO(newUser.getId(), newUser.getUsername(), newUser.getEmail(), newUser.getCreatedAt(), newUser.getUpdatedAt(), isOnline);
    }

    @Override
    public UserDTO find(UUID userId) {
       //사용자 찾고
        User user = userRepository.findById(userId)
               .orElseThrow(()-> new IllegalArgumentException("User not found"));
       // 온라인 상태 확인
        boolean isOnline = userStatusRepository.findByUser(user)
                .map(UserStatus::isOnline)
                .orElse(false);
        //변환 반환
        return new UserDTO(user.getId(), user.getUsername(), user.getEmail(), user.getCreatedAt(), user.getUpdatedAt(), isOnline);
    }

    @Override
    public List<UserDTO> findAll() {
        return userRepository.findAll().stream()
                .map(user -> {
                    boolean isOnline = userStatusRepository.findByUser(user)
                            .map(UserStatus::isOnline)
                            .orElse(false);
                    return new UserDTO(user.getId(), user.getUsername(), user.getEmail(), user.getCreatedAt(), user.getUpdatedAt(), isOnline);
                })
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO update(UserUpdateDTO userUpdateDTO, UserProfileImageDTO userProfileImageDTO) {
        try {
            //사용자 찾고
            User user = userRepository.findById(userUpdateDTO.id())
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            //중복체크
            if(userRepository.existsByEmail(userUpdateDTO.newEmail())){
                throw new IllegalArgumentException("Email already exits " + userUpdateDTO.newEmail());
            }
            //업데이트
            user.update(userUpdateDTO.newUsername(), userUpdateDTO.newEmail(), userUpdateDTO.newPassword());
            userRepository.save(user);
            //프로필 이미지 저장 (선택적으로)
            assert userProfileImageDTO != null;
            if(userProfileImageDTO.imageData() != null) {
                binaryContentRepository.findByUserId(user.getId()) // 레포지토리 미구현 수정해야함.
                        .ifPrenset(binaryContentRepository::delete);
                BinaryContent binaryContent = new BinaryContent(user, userProfileImageDTO.fileName(),userProfileImageDTO.imageData());
                binaryContentRepository.save(binaryContent);
            }
            boolean isOnline = userStatusRepository.findByUser(user)
                    .map(UserStatus::isOnline)
                    .orElse(false);
            return new UserDTO(user.getId(), user.getUsername(), user.getEmail(), user.getCreatedAt(), user.getUpdatedAt(), isOnline);
        }catch (IllegalArgumentException e){
            System.out.println("Failed to update user " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void delete(UUID userId) {
        try {
            //사용자 찾고
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            //관련 데이터 삭제
            binaryContentRepository.findByUserId(user.getId()).ifPresent(binaryContentRepository::delete); // 레포지터리 미구현 확인
            userStatusRepository.findByUser(user).ifPresent(userStatusRepository::delete); // 미구현 확인 해야함
            //user 삭제
            userRepository.deleteById(userId);
            System.out.println("User deleted: " + user.getEmail());
        } catch (IllegalArgumentException e) {
            System.out.println("Failed to delete user: " + e.getMessage());
        }
    }

}
