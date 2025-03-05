//package com.sprint.mission.discodeit.service.jcf;
//
//import com.sprint.mission.discodeit.common.ErrorMessage;
//import com.sprint.mission.discodeit.common.UtilMethod;
//import com.sprint.mission.discodeit.entity.User;
//import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
//import com.sprint.mission.discodeit.service.UserService;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//
//public class JCFUserService implements UserService {
//    private final JCFUserRepository jcfUserRepository;
//
//    public JCFUserService(JCFUserRepository jcfUserRepository) {
//        this.jcfUserRepository = jcfUserRepository;
//    }
//
//    @Override
//    public User createUser(String name, String nickname, String email, String password,
//                           String profileImageUrl) {
//        // 추후 중복 검사
//        User user = User.of(name, nickname, email, password, profileImageUrl, true);
//        // 비밀 번호 암호화
//        return jcfUserRepository.saveUser(user);
//    }
//
//    @Override
//    public User findUserByIdOrThrow(UUID userId) {
//        return Optional.ofNullable(jcfUserRepository.findUserById(userId))
//                .orElseThrow(() -> new RuntimeException(ErrorMessage.USER_NOT_FOUND.format(userId)));
//    }
//
//    @Override
//    public List<User> findAllUsers() {
//        return jcfUserRepository.findAllUsers();
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
//        foundUser.updatePassword(password); // 추후 비밀 번호 암호화
//        foundUser.updateProfileImageUrl(profileImageUrl);
//        foundUser.updateUpdatedAt(UtilMethod.getCurrentTime());
//
//        return jcfUserRepository.saveUser(foundUser);
//    }
//
//    @Override
//    public void deleteUser(UUID userId) {
//        if (!jcfUserRepository.existsUser(userId)) {
//            throw new RuntimeException(ErrorMessage.USER_NOT_FOUND.format(userId));
//        }
//        jcfUserRepository.removeUser(userId);
//    }
//}
