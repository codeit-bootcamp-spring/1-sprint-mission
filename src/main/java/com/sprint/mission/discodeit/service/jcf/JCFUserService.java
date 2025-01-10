package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.global.ErrorMessage;
import com.sprint.mission.discodeit.global.UtilMethod;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class JCFUserService implements UserService {
    private final Map<UUID, User> userData;

    private JCFUserService() {
        this.userData = new HashMap<>();
    }

    public static JCFUserService getInstance() {
        return LazyHolder.INSTANCE;
    }

    private static class LazyHolder {
        private static final JCFUserService INSTANCE = new JCFUserService();
    }

    @Override
    public User createUser(String name, String nickname, String email, String password,
                           String profileImageUrl) {
        // 추후 중복 검사
        User newUser = new User(name, nickname, email, password, profileImageUrl, true);
        // 비밀 번호 암호화
        userData.put(newUser.getId(), newUser);

        return newUser;
    }

    @Override
    public User findUserById(UUID id) {
        return Optional.ofNullable(userData.get(id)).orElseThrow(() -> new RuntimeException(ErrorMessage.USER_NOT_FOUND));
    }

    @Override
    public List<User> findAllUsers() {
        return new ArrayList<>(userData.values());
    }

    @Override
    public void updateUser(UUID id, String name, String nickname, String email, String password, String profileImageUrl) {
        User findUser = findUserById(id);

        findUser.updateName(name);
        findUser.updateNickname(nickname);
        findUser.updateEmail(email);
        findUser.updatePassword(password); // 추후 비밀 번호 암호화
        findUser.updateProfileImageUrl(profileImageUrl);
        findUser.updateUpdatedAt(UtilMethod.getCurrentTime());

        userData.put(findUser.getId(), findUser);
    }

    @Override
    public void deleteUser(UUID id) {
        if(!userData.containsKey(id)){
            throw new RuntimeException(ErrorMessage.USER_NOT_FOUND);
        }
        userData.remove(id);
    }
}
