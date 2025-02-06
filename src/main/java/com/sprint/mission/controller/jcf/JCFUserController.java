package com.sprint.mission.controller.jcf;

import com.sprint.mission.controller.UserController;
import com.sprint.mission.entity.User;
import com.sprint.mission.service.jcf.JCFChannelService;
import com.sprint.mission.service.jcf.JCFUserService;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class JCFUserController implements UserController {

    private final JCFUserService userService = JCFUserService.getInstance();
    private final JCFChannelService channelService = JCFChannelService.getInstance();

    @Override
    public User create(String name, String password, String email) {
        return userService.create(new User(name, password, email));
    }

    @Override
    public User updateAll(UUID id, String newName, String password, String email) {
        User updatingUser = userService.findById(id);
        updatingUser.setAll(newName, password, email);
        return userService.update(updatingUser);
    }

    @Override
    public User findById(UUID id) {
        return userService.findById(id);
    }

    @Override
    public List<User> findAll() {
        return userService.findAll();
    }

    @Override
    public List<User> findUsersByName(String findName){
        return userService.findAll().stream()
                .filter(user -> user.getName().equals(findName))
                .collect(Collectors.toList());
    }

    @Override
    public User findUserByNamePW(String name, String password) {
        return userService.findAll().stream()
                .filter(user -> user.getName().equals(name) && user.getPassword().equals(password))
                .findAny().orElse(null);
    }

    @Override
    public void deleteUser(UUID id, String nickName, String password) {
        User deletingUser = userService.findById(id); // 이 메서드에서 id 검증
        if (!(deletingUser.getName().equals(nickName) && deletingUser.getPassword().equals(password))) {
            System.out.println("닉네임 or 비밀번호가 틀렸습니다.");
            // 이 메서드에서 닉넴, 비번 검증
        } else {
            userService.delete(deletingUser);
        }
    }

    @Override
    public void drops(UUID channel_Id, UUID droppingUser_Id) {
        findById(droppingUser_Id).removeChannel(channelService.findById(channel_Id));
        // removeChannel 양방향 설정
    }

    @Override  // 모든 채널 탈퇴
    public void dropsAllByUser(UUID droppingUser_Id){
        findById(droppingUser_Id).removeAllChannel();
    }

    @Override
    public void addChannelByUser(UUID channelId, UUID userId) {
        findById(userId).addChannel(channelService.findById(channelId));
    }

    @Override
    public void createUserDirectory(){}
    /**
     * 메시지와 연계
     */
}
