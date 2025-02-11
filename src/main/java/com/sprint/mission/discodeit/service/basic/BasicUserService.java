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

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final Validator validator = new ValidatorImpl();
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public UserResponse create(UserRequest request) {

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

        if(request.profileImageId() != null){
            BinaryContent profileImage = binaryContentRepository.findById(request.profileImageId());
            user.setProfileImage(profileImage);
        }

        userStatusRepository.save(new UserStatus(user.getId()));

        return new UserResponse(user.getId(), user.getUsername(), user.getEmail(), false);
    }

    @Override
    public UserResponse readOne(UUID id) {
        User user = userRepository.findById(id);
        // TODO user readOne -> isOnline
        return new UserResponse(user.getId(), user.getUsername(), user.getEmail(), user.geti)
    }

    @Override
    public List<User> readAll() {
        return repository.readAll();
    }

    @Override
    public User update(UUID id, User updatedUser) {
        try {
            if(!validator.isValidEmail(updatedUser.getEmail())){
                System.out.println(updatedUser.getUsername() + "님의 사용자 수정이 완료되지 않았습니다.");
                System.out.println(new ValidationException("Invalid email format : " + updatedUser.getEmail()));
                return null;
            }

            if(!validator.isValidPhoneNumber(updatedUser.getPhoneNumber())){
                System.out.println(updatedUser.getUsername() + "님의 사용자 수정이 완료되지 않았습니다.");
                System.out.println(new ValidationException("Invalid phoneNumber format(000-0000-0000) : " + updatedUser.getPhoneNumber()));
                return null;
            }

            return repository.modify(id, updatedUser);
        } catch (DataNotFoundException e){
            throw new DataNotFoundException("저장되지 않았거나, 삭제된 아이디 입니다." + id);
        }
    }

    @Override
    public boolean delete(UUID id) {
        return repository.deleteById(id);
    }
}
