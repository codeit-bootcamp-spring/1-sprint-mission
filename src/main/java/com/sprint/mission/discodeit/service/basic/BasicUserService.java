package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentResponseDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.user.UserSignupRequestDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.binarycontent.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.exception.data.DataUpdateFailedException;
import com.sprint.mission.discodeit.exception.user.UserAlreadyExistsException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserUpdateDataNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.util.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BasicUserService implements UserService {

    @Qualifier("userValidator")
    private final Validator<User, UserSignupRequestDto, UserUpdateDto> userValidator;

    private final UserMapper userMapper;

    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentService binaryContentService;

    // TODO: LoadData Entity Name Magic Number를 어떻게 하면 좋을까?

    @Transactional
    @Override
    public UserResponseDto create(UserSignupRequestDto userReqDto, MultipartFile profile) throws IOException {
        // 유저 생성 로직
        log.debug("사용자 생성 요청 - 요청 데이터: {}", userReqDto);
        // 유효성 검사
        userValidator.validateCreate(userReqDto);

        // 중복 검사
        if (userRepository.existsUserByEmail(userReqDto.email()) || userRepository.existsUserByUsername(userReqDto.username())) {
            log.warn("사용자가 이미 존재합니다. - email: {}, username: {}", userReqDto.email(), userReqDto.username());
            throw new UserAlreadyExistsException(userReqDto.email(), userReqDto.username());
        }

        // user 생성
        User user = userMapper.toEntity(userReqDto);
        UserStatus userStatus = new UserStatus(Instant.now(), user);
        user.updateUserStatus(userStatus);

        // 프로필 이미지 존재 시 생성
        if (profile != null) {
            BinaryContentResponseDto profileDto = binaryContentService.create(profile);
            BinaryContent loadProfile = binaryContentRepository.findById(profileDto.id()).orElseThrow(() -> new BinaryContentNotFoundException(profileDto.id()));
            user.updateProfile(loadProfile);
            log.info("프로필 이미지가 등록되었습니다. - id: {}", loadProfile.getId());
        }
        UUID savedUser = userRepository.save(user).getId();
        User loadUser = userRepository.findById(savedUser).orElseThrow(() -> new UserNotFoundException(savedUser));

        log.info("사용자가 생성되었습니다. - id: {}", loadUser.getId());
        return userMapper.toResponseDto(loadUser);
    }

    @Override
    public UserResponseDto find(UUID userId) {
        log.debug("사용자 조회 요청 - id: {}", userId);
        User user = userRepository.findByIdWithDetails(userId).orElseThrow(() -> new UserNotFoundException(userId));
        if (user == null) {
            log.warn("해당 사용자가 존재하지 않습니다. - id: {}", user.getId());
            throw new UserNotFoundException(userId);
        }

        log.info("사용자 조회 성공 - id: {}", user.getId());
        return userMapper.toResponseDto(user);
    }

    @Override
    public List<UserResponseDto> findAll() {
        log.debug("전체 사용자 조회 요청");
        List<UserResponseDto> userList = userRepository.findAllWithDetails().stream()
                .map(userMapper::toResponseDto)
                .collect(Collectors.toList());
        log.info("전체 사용자 조회 성공 - 전체 사용자 수: {}", userList.size());
        return userList;
    }

    @Transactional
    @Override
    public UserResponseDto update(UUID userId, UserUpdateDto userUpdateDto, MultipartFile updateProfile) {
        log.debug("사용자 수정 요청 - 수정 대상 id: {}, 수정 요청 데이터: {}", userId, userUpdateDto);

        try {
            User current = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
            User updatedUser = userValidator.validateUpdate(current, userUpdateDto);
            if (updatedUser == null) {
                throw new UserUpdateDataNotFoundException(userId);
            }

            if (updateProfile != null) {
                // 프로필 데이터 존재 여부
                if (updatedUser.getProfile() != null) {
                    binaryContentRepository.delete(updatedUser.getProfile());
                }

                BinaryContentResponseDto updateFile = binaryContentService.create(updateProfile);
                BinaryContent update = binaryContentRepository.findById(updateFile.id()).orElseThrow(() -> new BinaryContentNotFoundException(updateFile.id()));
                updatedUser.updateProfile(update);
                log.info("사용자 프로필 이미지가 업데이트되었습니다. - id: {}, profileId: {}", updatedUser.getId(), updateFile.id());
            }

            userRepository.save(updatedUser); // DB에 반영

            log.info("사용자 정보가 수정되었습니다. - id: {}", updatedUser.getId());

            return userMapper.toResponseDto(updatedUser);
        } catch (UserUpdateDataNotFoundException ue) {
            log.warn("수정할 사용자 데이터가 없습니다. - 수정 대상 id: {}, 수정 요청 데이터: {}", userId, userUpdateDto);
            throw ue;
        } catch (Exception e) {
            log.error("사용자 수정 중 예외 발생 - id: {}, message: {}", userId, e.getMessage(), e);
            throw new DataUpdateFailedException("User", userId, e);
        }
    }

    @Transactional
    @Override
    public UserResponseDto delete(UUID userId) {
        log.debug("사용자 삭제 요청 - 삭제 대상 id: {}", userId);
        User deleteUser = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        userRepository.delete(deleteUser);

        log.info("사용자가 삭제되었습니다. - id: {}", deleteUser.getId());
        return userMapper.toResponseDto(deleteUser);
    }
}
