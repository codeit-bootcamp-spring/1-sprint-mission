package discodeit.service;

import discodeit.dto.binarycontent.AddBinaryContentRequest;
import discodeit.dto.user.CreateUserRequest;
import discodeit.dto.user.UpdatePasswordRequest;
import discodeit.dto.user.UpdatePhoneNumRequest;
import discodeit.dto.user.UpdateProfileRequest;
import discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    User create(CreateUserRequest createUserRequest, Optional<AddBinaryContentRequest> addBinaryContentRequest);
    User find(UUID userId);
    List<User> findAll();
    User updatePhoneNum(UpdatePhoneNumRequest updatePhoneNumRequestRequest);
    User updatePassword(UpdatePasswordRequest updatePasswordRequest);
    User updateProfile(UpdateProfileRequest updateProfileRequest);
    void delete(UUID userId);
}
