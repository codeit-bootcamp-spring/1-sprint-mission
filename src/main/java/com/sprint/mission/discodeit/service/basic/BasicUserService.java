package com.sprint.mission.discodeit.service.basic;


import com.sprint.mission.discodeit.dto.UserRequest;
import com.sprint.mission.discodeit.dto.UserResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.DataNotFoundException;
import com.sprint.mission.discodeit.exception.ValidationException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.validation.Impl.ValidatorImpl;
import com.sprint.mission.discodeit.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final Validator validator = new ValidatorImpl();
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public UserResponse create(UserRequest request) {
        return null;
    }

    @Override
    public UserResponse create(UserRequest request, MultipartFile file) {

        if(!validator.isValidEmail(request.email())){
            System.out.println(request.username() + "님의 사용자 등록이 완료되지 않았습니다.");
            System.out.println(new ValidationException("Invalid email format : " + request.email()));
        }

        if(!validator.isValidPhoneNumber(request.phoneNumber())){
            System.out.println(request.username() + "님의 사용자 등록이 완료되지 않았습니다.");
            System.out.println(new ValidationException("Invalid phoneNumber format(000-0000-0000) : " + request.phoneNumber()));
        }

        User user = new User(request.username(), request.password(), request.email(), request.phoneNumber());
        userRepository.save(user);

        BinaryContent profileImage = null;
        if(file != null && !file.isEmpty()){
            profileImage = binaryContentRepository.save(file, user.getId());
            System.out.println(profileImage.getId());
            user.setProfileImageId(profileImage.getId());
        }

        UserStatus userStatus = new UserStatus(user.getId());
        userStatusRepository.save(userStatus);

        return UserResponse.fromEntity(user , userStatus.isOnline());
    }

    @Override
    public UserResponse readOne(UUID id) {
        User user = userRepository.findById(id);
        boolean isOnline = userStatusRepository.findByUserId(user.getId()).isOnline();

        return UserResponse.fromEntity(user, isOnline);
    }

    @Override
    public List<UserResponse> readAll() {
        List<User> users = userRepository.readAll();

        List<UserResponse> responses = users.stream().map(user -> {
                    try {
                        boolean isOnline = userStatusRepository.findByUserId(user.getId()).isOnline();
                        return UserResponse.fromEntity(user, isOnline);
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

            User user = userRepository.modify(id, new User(updatedUserReq.username(), updatedUserReq.password(), updatedUserReq.email(), updatedUserReq.phoneNumber()));
            boolean isOnline = userStatusRepository.findByUserId(id).isOnline();

            System.out.println("업데이트가 완료되었습니다.");
            return UserResponse.fromEntity(user, isOnline);

        } catch (DataNotFoundException e){
            throw new DataNotFoundException("저장되지 않았거나, 삭제된 아이디 입니다." + id);
        }
    }

    @Override
    public boolean delete(UUID id) {
        return userRepository.deleteById(id);
    }
}
