package com.sprint.mission.discodeit.service.basic;


import com.sprint.mission.discodeit.dto.request.UserRequest;
import com.sprint.mission.discodeit.dto.response.UserResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.ValidationException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import com.sprint.mission.discodeit.validation.Impl.ValidatorImpl;
import com.sprint.mission.discodeit.validation.Validator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final Validator validator = new ValidatorImpl();
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentStorage binaryContentStorage;

    @Override
    public UserResponse create(UserRequest request) {
        return null;
    }

    @Transactional
    @Override
    public UserResponse create(UserRequest request, MultipartFile file) throws IOException {
        log.debug("유저 생성 시도");

        if(!validator.isValidEmail(request.email())){
            throw new ValidationException("Invalid email format : " + request.email());
        }

        if(!validator.isValidPhoneNumber(request.phoneNumber())){
            throw new ValidationException("Invalid phoneNumber format(000-0000-0000) : " + request.phoneNumber());
        }

        User user = UserMapper.INSTANCE.toEntity(request);
        userRepository.save(user);
        log.info("유저 등록 성공 - username: {}, id: {}", request.username(), user.getId());

        BinaryContent profileImage = null;
        if(file != null && !file.isEmpty()){
            log.debug("유저 프로필 이미지 등록 시도 - username: {}, file: {}", request.username(), file);
            profileImage = new BinaryContent(user.getId(), file.getOriginalFilename(), file.getSize(), file.getContentType());
            //, file.getBytes()

            binaryContentRepository.save(profileImage);
            user.setProfileImageId(profileImage.getId());

            binaryContentStorage.put(profileImage.getId(), file.getBytes());
            log.info("유저 프로필 이미지 등록 완료 - username: {}, id: {}", request.username(), profileImage.getId());
        }

        log.debug("읽음 상태 생성 시도");
        UserStatus userStatus = new UserStatus(user.getId());
        userStatusRepository.save(userStatus);
        log.info("읽은 상태 생성 성공 - userStatus: {}", userStatus);

        log.info("유저 등록 완료 - username: {}, id: {}, profileImageId: {}", request.username(), user.getId(), user.getProfileImageId());
        return UserMapper.INSTANCE.toDto(user, userStatus);
    }

    @Override
    public UserResponse readOne(UUID id) {
        log.debug("유저 단건 조회 : id : {} ", id);

        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> {
                        log.warn("유저 단건 조회 실패 - 저장되지 않았거나, 삭제된 id : {}", id);
                        return new UserNotFoundException(id);
                    });

            log.info("유저 단건 조회 완료 - id : {} ", id);
            return UserMapper.INSTANCE.toDto(user, userStatusRepository.findByUserid(user.getId()));
        } catch (NullPointerException e){
            throw new UserNotFoundException(id);
            // log.warn("유저 단건 조회 실패- id 를 찾을 수 없음 : {} ", id);
            // throw new NullPointerException("ID를 찾을 수 없습니다." + e.getMessage());
        }
    }

    @Override
    public List<UserResponse> readAll() {
        log.debug("유저 전체 조회 시도");

        List<User> users = userRepository.findAll();
        log.info("조회된 유저 개수 : {}", users.size());

        List<UserResponse> responses = users.stream().map(user -> {
                    try {
                        return UserMapper.INSTANCE.toDto(user, userStatusRepository.findByUserid(user.getId()));
                    } catch (NullPointerException e){
                        throw new NullPointerException("user id 값이 null 입니다." + e.getMessage());
                    }
                })
                .collect(Collectors.toList());

        log.info("유저 조회 완료 - 총 {}개", responses.size());
        return responses;
    }

    @Override
    public UserResponse update(UUID id, UserRequest updatedUserReq) {
        log.debug("유저 수정 요청 - id: {}, updateUser: {}", id, updatedUserReq);

        try {
            if(!validator.isValidEmail(updatedUserReq.email())){
                throw new ValidationException("Invalid email format : " + updatedUserReq.email());
            }

            if(!validator.isValidPhoneNumber(updatedUserReq.phoneNumber())){
                throw new ValidationException("Invalid phoneNumber format(000-0000-0000) : " + updatedUserReq.phoneNumber());
            }

            User user = userRepository.findById(id)
                    .orElseThrow(() -> {
                        log.warn("유저 조회 실패 - 저장되지 않았거나, 삭제된 ID : {}", id);
                        return new UserNotFoundException(id);
                    });

            user.setUsername(updatedUserReq.username());
            user.setPassword(updatedUserReq.password());
            user.setEmail(updatedUserReq.email());
            user.setPhoneNumber(updatedUserReq.phoneNumber());

            userRepository.save(user);

            log.info("유저 수정 성공 - id: {}", id);
            return UserMapper.INSTANCE.toDto(user, userStatusRepository.findByUserid(id));

        } catch (UserNotFoundException e){
            throw new UserNotFoundException(id);
        }
    }

    @Override
    public boolean delete(UUID id) {
        log.debug("유저 삭제 요청 - id: {}", id);

        if (!userRepository.existsById(id)) {
            log.warn("유저 삭제 실패 - 없거나 삭제된 ID : {}", id);
            throw new UserNotFoundException(id);
        }

        userRepository.deleteById(id);
        log.info("유저 삭제 완료 - id: {}", id);
        return true;
    }
}
