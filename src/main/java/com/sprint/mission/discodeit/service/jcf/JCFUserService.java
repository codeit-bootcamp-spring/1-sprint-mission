//package com.sprint.mission.discodeit.service.jcf;
//
//import com.sprint.mission.discodeit.entity.binarycontent.BinaryContent;
//import com.sprint.mission.discodeit.entity.binarycontent.BinaryContentCreateRequest;
//import com.sprint.mission.discodeit.entity.user.User;
//import com.sprint.mission.discodeit.entity.user.UserCommonRequest;
//import com.sprint.mission.discodeit.exception.user.IllegalUserException;
//import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
//import com.sprint.mission.discodeit.factory.EntityFactory;
//import com.sprint.mission.discodeit.repository.UserRepository;
//import com.sprint.mission.discodeit.service.BinaryContentService;
//import com.sprint.mission.discodeit.service.UserService;
//import com.sprint.mission.discodeit.service.validate.ServiceValidator;
//import com.sprint.mission.discodeit.service.validate.UserServiceValidator;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.Map;
//import java.util.UUID;
//
//@Service
//@RequiredArgsConstructor
//public class JCFUserService implements UserService {
//    private final UserRepository userRepository;
//    private final BinaryContentService binaryContentService;
//    private static EntityFactory entityFactory;
//
//    private final ServiceValidator<User> validator = new UserServiceValidator();
//
//    @Override
//    public User createUser(UserCommonRequest request) {
//        if(validator.isNullParam(request.userName(), request.userEmail(), request.userPassword())) {
//            throw new UserNotFoundException();
//        }
//
//        User user = validator.entityValidate(entityFactory.createUser((request.userName(), request.userEmail(), request.userPassword()));
//
//        userRepository.userSave(user);
//
//        return user;
//    }
//
//    @Override
//    public User createUserWithProfile(UserCommonRequest createDto, BinaryContent binaryContent) {
//        User basicUser = createUser(createDto);
//
//
//        if (binaryContent == null) {
//            throw new IllegalUserException("프로필 이미지 등록 오류: null");
//        }
//
//        BinaryContentCreateRequest binaryContentCreateRequest = new BinaryContentCreateRequest(basicUser.getId(), binaryContent.getMessageId(), binaryContent.getFileName(), binaryContent.getFilePath());
//        binaryContentService.create(binaryContentCreateRequest);
//
//        return basicUser;
//    }
//
//    @Override
//    public User getUserById(UUID userId) {
//        return validator.entityValidate(userRepository.findUserById(userId));
//    }
//
//
//
//    @Override
//    public Map<UUID, User> getAllUsers() {
//        return validator.entityValidate(userRepository.findAllUser());
//    }
//
//    @Override
//    public User updateUser(UUID userId, String newName, String newEmail, String newPassword) {
//        validator.isNullParam(newName, newEmail, newPassword);
//
//        User findUser = getUserById(userId);
//
//        if (findUser == null) {
//            throw new UserNotFoundException();
//        } else {
//            findUser.updateName(newName);
//            findUser.updateEmail(newEmail);
//            findUser.updatePassword(newPassword);
//        }
//
//        return findUser;
//    }
//
//    @Override
//    public void deleteUser(UUID userId) {
//        User findUser = validator.entityValidate(getUserById(userId));
//
//        userRepository.removeUserById(findUser.getId());
//    }
//}