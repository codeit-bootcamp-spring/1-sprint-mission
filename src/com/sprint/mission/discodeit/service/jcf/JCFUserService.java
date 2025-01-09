package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class JCFUserService implements UserService {

    final List<User> userData;

    public JCFUserService() {
        this.userData = new ArrayList<>();
    }

    @Override
    public void createUser(User user) { //유저 추가
        this.userData.add(user);
        System.out.println(Utils.transTime(user.getCreatedAt()) + " " + user.getUserName() + " 유저가 생성되었습니다.");
    }

    @Override
    public User readUser(String id) {
        return
                this.userData.stream()
                        .filter(user -> user.getUserId().equals(id))
                        .findFirst()
                        .orElse(null);
    }

    @Override
    public List<User> readAllUser() {
        return this.userData;
    }

    @Override
    public void modifyUser(String userID, String newName) {
        readUser(userID).updateUsername(newName);

    }

    @Override
    public void deleteUser(String id) {

        String name= readUser(id).getUserName();
        boolean isDeleted = this.userData.removeIf(user -> user.getUserId().equals(id));

        if(isDeleted) {
            System.out.println(name + " 유저가 삭제되었습니다.");
        }else{
            System.out.println(name + " 유저 삭제 실패하였습니다.");
        }
    }

    @Override
    public void addMessage(User user, Message message) {
        user.up(user, message);
    }
}
