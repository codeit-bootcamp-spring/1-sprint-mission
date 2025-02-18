package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.common.validation.UserValidator;
import com.sprint.mission.discodeit.common.validation.Validator;
import com.sprint.mission.discodeit.dto.BinaryContentDTO;
import com.sprint.mission.discodeit.dto.UserDTO;
import com.sprint.mission.discodeit.dto.UserStatusDTO;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.enums.ContentType;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final Validator<User, UserDTO.request> userValidator = new UserValidator();
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final UserStatusService userStatusService;

    @Override
    public Long create(UserDTO.request userReqDto, MultipartFile profile) {
        // 유저 생성 로직
        try {
            // 유효성 검사
            userValidator.validateCreate(userReqDto);

            // 중복 검사
            if (userRepository.isExistByEmail(userReqDto.email()) || userRepository.isExistByUserName(userReqDto.userName())) {
                throw new CustomException(ErrorCode.USER_IS_ALREADY_EXIST);
            }

            // user 생성
            Long userId = userRepository.save(new User(userReqDto));

            // 프로필 이미지 존재 시 생성
            if (profile != null && !profile.isEmpty()) {
                BinaryContentDTO.request binaryContentReqDTO = BinaryContentDTO.request.builder()
                        .contentType(ContentType.PROFILE)
                        .referenceId(userRepository.load(userId).getId())
                        .file(profile.getBytes())
                        .mimeType(profile.getContentType())
                        .filename(profile.getOriginalFilename())
                        .build();
                binaryContentRepository.save(new BinaryContent(binaryContentReqDTO));
            }

            // userStatus 생성
            userStatusRepository.save(new UserStatus(
                    UserStatusDTO.request.builder()
                            .userId(userRepository.load(userId).getId())
                            .accessedAt(Instant.now())
                            .build())
            );

            return userId;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserDTO.response find(Long id) {
        User user = userRepository.load(id);
        if (user == null) throw new CustomException(ErrorCode.USER_NOT_FOUND);
        Map.Entry<Long, UserStatus> userStatus = userStatusRepository.findUserStatusByUserId(user.getId());

        return UserDTO.response.builder()
                .id(id)
                .uuid(user.getId())
                .username(user.getUserName())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .online(userStatus.getValue().isOnline())
                .build();
    }

    @Override
    public UserDTO.response find(UUID uuid) {
        Map.Entry<Long, User> userData = userRepository.load(uuid);
        User user = userData.getValue();

        if (user == null) throw new CustomException(ErrorCode.USER_NOT_FOUND);
        UserStatus userStatus = userStatusRepository.findUserStatusByUserId(user.getId()).getValue();

        return UserDTO.response.builder()
                .id(userData.getKey())
                .uuid(user.getId())
                .username(user.getUserName())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .online(userStatus.isOnline())
                .build();
    }

    @Override
    public List<UserDTO.response> findAll() {
        return userRepository.loadAll().entrySet().stream()
                .map(entry -> {
                            Map.Entry<Long, UserStatus> userStatus = userStatusRepository.findUserStatusByUserId(entry.getValue().getId());
                            return UserDTO.response.builder()
                                    .id(entry.getKey())
                                    .uuid(entry.getValue().getId())
                                    .username(entry.getValue().getUserName())
                                    .nickname(entry.getValue().getNickname())
                                    .email(entry.getValue().getEmail())
                                    .online(userStatus.getValue().isOnline())   // userStatus
                                    .build();
                        }
                )
                .collect(Collectors.toList());
    }

    @Override
    public boolean update(UserDTO.update updateDTO) {
        boolean isUpdated = false;
        try {
            User updatedUser = new UserValidator().validateUpdate(userRepository.load(updateDTO.id()), updateDTO.userReqDTO());
            if (updatedUser == null) {
                return isUpdated;
            }

            isUpdated = true;
            if (updateDTO.profileDTO() != null) {
                // 프로필 데이터 존재 여부
                if (binaryContentRepository.isBinaryContentExist(updatedUser.getId())) {
                    // 존재 시 삭제
                    Map.Entry<Long, BinaryContent> binaryContent = binaryContentRepository.findProfileImageByMessageId(updatedUser.getId());
                    binaryContentRepository.delete(binaryContent.getKey());
                }
                // 새로운 데이터 생성
                binaryContentRepository.save(new BinaryContent(updateDTO.profileDTO()));
            }

            userRepository.update(updateDTO.id(), updatedUser); // DB에 반영
            return isUpdated;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Long delete(Long id) {
        User deleteUser = userRepository.load(id);

        binaryContentRepository.deleteAllFileByReferenceId(deleteUser.getId());
        userStatusRepository.deleteByUserId(deleteUser.getId());

        userRepository.delete(id);
        return id;
    }

    @Override
    public Long delete(UUID uuid) {
        Map.Entry<Long, User> deleteUser = userRepository.load(uuid);

        binaryContentRepository.deleteAllFileByReferenceId(deleteUser.getValue().getId());
        userStatusRepository.deleteByUserId(deleteUser.getValue().getId());

        userRepository.delete(deleteUser.getKey());
        return deleteUser.getKey();
    }
}
