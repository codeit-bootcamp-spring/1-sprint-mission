//package com.sprint.mission.discodeit.service.file;
//
//import com.sprint.mission.discodeit.common.ErrorMessage;
//import com.sprint.mission.discodeit.common.UtilMethod;
//import com.sprint.mission.discodeit.entity.User;
//import com.sprint.mission.discodeit.repository.file.FileUserRepository;
//import com.sprint.mission.discodeit.service.UserService;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//
//public class FileUserService implements UserService {
//    private final FileUserRepository fileUserRepository;
//
//    public FileUserService(FileUserRepository fileUserRepository) {
//        this.fileUserRepository = fileUserRepository;
//    }
//
//    @Override
//    public User createUser(String name, String nickname, String email, String password, String profileImageUrl) {
//        User user = User.of(name, nickname, email, password, profileImageUrl, true);
//
//        return fileUserRepository.saveUser(user);
//    }
//
//    @Override
//    public User findUserByIdOrThrow(UUID userId) {
//        return Optional.ofNullable(fileUserRepository.findUserById(userId))
//                .orElseThrow(() -> new RuntimeException(ErrorMessage.USER_NOT_FOUND.format(userId)));
//    }
//
//    @Override
//    public List<User> findAllUsers() {
//        return fileUserRepository.findAllUsers();
//    }
//
//    @Override
//    public User updateUser(UUID userId, String name, String nickname, String email, String password,
//                           String profileImageUrl) {
//        User foundUser = findUserByIdOrThrow(userId);
//
//        foundUser.updateName(name);
//        foundUser.updateNickname(nickname);
//        foundUser.updateEmail(email);
//        foundUser.updatePassword(password);
//        foundUser.updateProfileImageUrl(profileImageUrl);
//        foundUser.updateUpdatedAt(UtilMethod.getCurrentTime());
//
//        return fileUserRepository.saveUser(foundUser);
//    }
//
//    @Override
//    public void deleteUser(UUID userId) {
//        if (!fileUserRepository.existsUser(userId)) {
//            throw new RuntimeException(ErrorMessage.USER_NOT_FOUND.format(userId));
//        }
//        fileUserRepository.removeUser(userId);
//    }
//}
