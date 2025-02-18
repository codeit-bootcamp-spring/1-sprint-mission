package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BasicUserService implements UserService {

    private final UserRepository userRepository;

    public BasicUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    // User 생성(가입)
    @Override
    public void create(User user) {

        userRepository.save(user);
    }


    // 데이터 읽기
    // id 같은 사람 읽기
    @Override
    public User read(UUID id) {

        userIsExist(id);

        return userRepository.load().get(id);
    }

    // 모든 데이터 읽어오기
    @Override
    public List<User> readAll() {

        return changeList(userRepository.load());
    }

    // 데이터 수정
    // 이메일 수정
    @Override
    public void updateEmail(UUID id, String updateEmail) {

        User updateUser = read(id);
        updateUser.updateEmail(updateEmail);
        userRepository.save(updateUser);
    }

    // 비밀번호 수정
    public void updatePassword(UUID id, String updatePassword) {

        User updateUser = read(id);
        updateUser.updatePassword(updatePassword);
        userRepository.save(updateUser);
    }

    // 이름 수정
    @Override
    public void updateName(UUID id, String updateName) {

        User updateUser = read(id);
        updateUser.updateName(updateName);
        userRepository.save(updateUser);
    }

    // 닉네임 수정
    @Override
    public void updateNickname(UUID id, String updateNickname) {

        User updateUser = read(id);
        updateUser.updateNickname(updateNickname);
        userRepository.save(updateUser);
    }

    // 전화번호 수정
    @Override
    public void updatePhoneNumber(UUID id, String updatePhoneNumber) {

        User updateUser = read(id);
        updateUser.updatePhoneNumber(updatePhoneNumber);
        userRepository.save(updateUser);
    }


    @Override
    public void delete(UUID id) {

        userIsExist(id);
        userRepository.delete(id);
    }

    // 유저 존재 여부 확인
    @Override
    public void userIsExist(UUID id) {
        Map<UUID, User> users = userRepository.load();

        if (!users.containsKey(id)) {
            throw new IllegalArgumentException("존재하지 않는 유저입니다.");
        }
    }

    // 유저 삭제 (회원 탈퇴)
    // 불러온 데이터 List로 변환
    private List<User> changeList(Map<UUID, User> map) {

        return map.values().stream().toList();
    }
}