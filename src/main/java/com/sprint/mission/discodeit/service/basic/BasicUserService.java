package com.sprint.mission.discodeit.service.basic;


import com.sprint.mission.discodeit.dto.request.UserRequest;
import com.sprint.mission.discodeit.dto.response.UserResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.ResourceNotFoundException;
import com.sprint.mission.discodeit.exception.ValidationException;
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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service

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

        if(!validator.isValidEmail(request.email())){
            System.out.println(request.username() + "님의 사용자 등록이 완료되지 않았습니다.");
            throw new ValidationException("Invalid email format : " + request.email());
        }

        if(!validator.isValidPhoneNumber(request.phoneNumber())){
            System.out.println(request.username() + "님의 사용자 등록이 완료되지 않았습니다.");
            throw new ValidationException("Invalid phoneNumber format(000-0000-0000) : " + request.phoneNumber());
        }

//        User user = new User(request.username(), request.password(), request.email(), request.phoneNumber());
        User user = UserMapper.INSTANCE.toEntity(request);
        userRepository.save(user);
//        userRepository.flush();

        BinaryContent profileImage = null;
        if(file != null && !file.isEmpty()){
            profileImage = new BinaryContent(user.getId(), file.getOriginalFilename(), file.getSize(), file.getContentType());
            //, file.getBytes()

            binaryContentRepository.save(profileImage);
            user.setProfileImageId(profileImage.getId());

            binaryContentStorage.put(profileImage.getId(), file.getBytes());
        }

        UserStatus userStatus = new UserStatus(user.getId());
        userStatusRepository.save(userStatus);

        return UserMapper.INSTANCE.toDto(user, userStatus);
    }

    @Override
    public UserResponse readOne(UUID id) {
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("저장되지 않았거나, 삭제된 아이디입니다." + id));

            return UserMapper.INSTANCE.toDto(user, userStatusRepository.findByUserid(user.getId()));
        } catch (NullPointerException e){
            throw new NullPointerException("ID를 찾을 수 없습니다." + e.getMessage());
        }
    }

    @Override
    public List<UserResponse> readAll() {
        List<User> users = userRepository.findAll();

        List<UserResponse> responses = users.stream().map(user -> {
                    try {
                        return UserMapper.INSTANCE.toDto(user, userStatusRepository.findByUserid(user.getId()));
                    } catch (NullPointerException e){
                        throw new NullPointerException("user id 값이 null 입니다." + e.getMessage());
                    }
                })
                .collect(Collectors.toList());
        return responses;
    }

    @Override
    public UserResponse update(UUID id, UserRequest updatedUserReq) {
        try {
            if(!validator.isValidEmail(updatedUserReq.email())){
                System.out.println(updatedUserReq.username() + "님의 사용자 수정이 완료되지 않았습니다.");
                System.out.println(new ValidationException("Invalid email format : " + updatedUserReq.email()));
                return null;
            }

            if(!validator.isValidPhoneNumber(updatedUserReq.phoneNumber())){
                System.out.println(updatedUserReq.username() + "님의 사용자 수정이 완료되지 않았습니다.");
                System.out.println(new ValidationException("Invalid phoneNumber format(000-0000-0000) : " + updatedUserReq.phoneNumber()));
                return null;
            }

            User user = userRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("저장되지 않았거나, 삭제된 아이디입니다." + id));

            user.setUsername(updatedUserReq.username());
            user.setPassword(updatedUserReq.password());
            user.setEmail(updatedUserReq.email());
            user.setPhoneNumber(updatedUserReq.phoneNumber());

            userRepository.save(user);

            System.out.println("업데이트가 완료되었습니다.");
            return UserMapper.INSTANCE.toDto(user, userStatusRepository.findByUserid(id));

        } catch (ResourceNotFoundException e){
            throw new ResourceNotFoundException("저장되지 않았거나, 삭제된 아이디 입니다." + id);
        }
    }

    @Override
    public boolean delete(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("해당 ID의 사용자가 존재하지 않습니다 : " + id);
        }

        userRepository.deleteById(id);
        return true;
    }
}
