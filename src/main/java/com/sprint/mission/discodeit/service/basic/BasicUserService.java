package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.BinaryContentDTO;
import com.sprint.mission.discodeit.dto.CommonDTO;
import com.sprint.mission.discodeit.dto.UserDTO;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.util.EntryUtils;
import com.sprint.mission.discodeit.util.FileConverter;
import com.sprint.mission.discodeit.util.validation.UserValidator;
import com.sprint.mission.discodeit.util.validation.Validator;
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

    @Override
    public CommonDTO.idResponse create(UserDTO.request userReqDto, MultipartFile profile) {
        // 유저 생성 로직
        try {
            // 유효성 검사
            userValidator.validateCreate(userReqDto);

            // 중복 검사
            if (userRepository.isExistByEmail(userReqDto.email()) || userRepository.isExistByUserName(userReqDto.userName())) {
                throw new CustomException(ErrorCode.USER_IS_ALREADY_EXIST);
            }

            // user 생성
            Long userId = userRepository.save(new User(
                    userReqDto.userName(), userReqDto.nickname(), userReqDto.email(), userReqDto.password(),
                    userReqDto.phone(), userReqDto.regionCode(), userReqDto.userType(), userReqDto.introduce()
            ));
            UUID userUUID = userRepository.load(userId).getId();

            // 프로필 이미지 존재 시 생성
            if (profile != null && !profile.isEmpty()) {
                BinaryContentDTO.request convert = FileConverter.convertToBinaryContent(profile, userUUID, BinaryContent.ContentType.PROFILE);
                binaryContentRepository.save(new BinaryContent(
                        convert.referenceId(), convert.file(), convert.contentType(), convert.mimeType(), convert.filename()
                ));
            }

            // userStatus 생성
            userStatusRepository.save(new UserStatus(userUUID, Instant.now()));

            return CommonDTO.idResponse.from(userId, userUUID);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserDTO.response find(Long id) {
        User user = userRepository.load(id);
        if (user == null) throw new CustomException(ErrorCode.USER_NOT_FOUND);
        Map.Entry<Long, UserStatus> userStatus = userStatusRepository.findUserStatusByUserId(user.getId());

        return UserDTO.response.from(EntryUtils.of(id, user), userStatus.getValue().isOnline());
    }

    @Override
    public UserDTO.response find(UUID uuid) {
        Map.Entry<Long, User> userData = userRepository.load(uuid);
        User user = userData.getValue();

        if (user == null) throw new CustomException(ErrorCode.USER_NOT_FOUND);
        UserStatus userStatus = userStatusRepository.findUserStatusByUserId(user.getId()).getValue();

        return UserDTO.response.from(userData, userStatus.isOnline());
    }

    @Override
    public List<UserDTO.response> findAll() {
        return userRepository.loadAll().entrySet().stream()
                .map(entry -> {
                            Map.Entry<Long, UserStatus> userStatus = userStatusRepository.findUserStatusByUserId(entry.getValue().getId());
                            return UserDTO.response.from(entry, userStatus.getValue().isOnline());
                        }
                )
                .collect(Collectors.toList());
    }

    @Override
    public CommonDTO.idResponse update(Long userId, UserDTO.request request, MultipartFile updateProfile) {
        boolean isUpdated = false;
        UserDTO.update updateDTO = UserDTO.update.of(userId, request, updateProfile);
        try {
            User updatedUser = new UserValidator().validateUpdate(userRepository.load(updateDTO.id()), updateDTO.userReqDTO());
            if (updatedUser == null) {
                throw new CustomException(ErrorCode.USER_UPDATE_DATA_NOT_FOUND);
            }

            isUpdated = true;
            if (updateDTO.profile() != null) {
                // 프로필 데이터 존재 여부
                if (binaryContentRepository.hasBinaryContent(updatedUser.getId())) {
                    // 존재 시 삭제
                    Map.Entry<Long, BinaryContent> binaryContent = binaryContentRepository.findProfileImageByMessageId(updatedUser.getId());
                    binaryContentRepository.delete(binaryContent.getKey());
                }
                // 새로운 데이터 생성
                BinaryContentDTO.request file = FileConverter.convertToBinaryContent(updateDTO.profile(), userRepository.load(updateDTO.id()).getId(), BinaryContent.ContentType.PROFILE);
                binaryContentRepository.save(new BinaryContent(
                        file.referenceId(), file.file(), file.contentType(), file.mimeType(), file.filename()
                ));
            }
            userRepository.update(updateDTO.id(), updatedUser); // DB에 반영
            return CommonDTO.idResponse.from(updateDTO.id(), updatedUser.getId());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CommonDTO.idResponse delete(Long id) {
        User deleteUser = userRepository.load(id);

        binaryContentRepository.deleteAllFileByReferenceId(deleteUser.getId());
        userStatusRepository.deleteByUserId(deleteUser.getId());

        userRepository.delete(id);
        return CommonDTO.idResponse.from(id, deleteUser.getId());
    }

    @Override
    public CommonDTO.idResponse delete(UUID uuid) {
        Map.Entry<Long, User> deleteUser = userRepository.load(uuid);

        binaryContentRepository.deleteAllFileByReferenceId(deleteUser.getValue().getId());
        userStatusRepository.deleteByUserId(deleteUser.getValue().getId());

        userRepository.delete(deleteUser.getKey());
        return CommonDTO.idResponse.from(deleteUser.getKey(), deleteUser.getValue().getId());
    }
}
