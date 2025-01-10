package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {
    private final Map<UUID, User> data;
    private final MessageService messageService;

    public JCFUserService(Map<UUID, User> data, MessageService messageService) {
        this.data = data;
        this.messageService = messageService;
    }

    @Override
    public User createUser(User user){
        if (data.containsKey(user.getId())) {
            throw new IllegalArgumentException("User ID already exists: " + user.getId());
        }
        data.put(user.getId(), user);

        System.out.println("[" + user.getUsername()+" 유저 등록 완료] ");
        return user;
    }

    @Override
    public Optional<User> readUser(User user) {
        return Optional.ofNullable(data.get(user.getId()));
    }

    @Override
    public List<User> readAllUsers() {
        return new ArrayList<>(data.values());
    }

    @Override
    public User updateUser(User existUser, User updateUser){
        if (!data.containsKey(existUser.getId())) {
            throw new NoSuchElementException("User not found");
        }
        // 기존 객체의 값을 업데이트
        existUser.updateTime();
        existUser.updateUserid(updateUser.getUserid());
        existUser.updateUsername(updateUser.getUsername());
        existUser.updatePassword(updateUser.getPassword());
        existUser.updateUserEmail(updateUser.getEmail());

        data.put(existUser.getId(), existUser);

        return existUser;
    }

    @Override
    public boolean deleteUser(User user){
        if (!data.containsKey(user.getId())) {
            return false;
        }
        //사용자 삭제 시 관련 메세지도 삭제
        messageService.deleteMessageByUser(user);
        data.remove(user.getId());
        return true;
    }
}
