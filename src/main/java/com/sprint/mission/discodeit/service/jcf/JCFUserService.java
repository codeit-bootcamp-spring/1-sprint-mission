package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JCFUserService implements UserService {

    final List<User> userData;

    public JCFUserService() {
        this.userData = new ArrayList<>();
    }

    @Override
    public User createUser(String userName) { //유저 추가
        User user = new User(userName);
        this.userData.add(user);
        System.out.println(Utils.transTime(user.getCreatedAt()) + " " + user.getUserName() + " 유저가 생성되었습니다.");
        return user;
    }

    @Override
    public User readUser(UUID id) {
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
    public User modifyUser(UUID userID, String newName) {
        User user= readUser(userID);
        String oriName=user.getUserName();
        user.updateUsername(newName);
        user.updateUpdatedAt();
        System.out.println("유저이름 변경: " + oriName+ " -> " + newName );
        return user;

    }

    @Override
    public void deleteUser(UUID id) {

        String name= readUser(id).getUserName();
        boolean isDeleted = this.userData.removeIf(user -> user.getUserId().equals(id));

        if(isDeleted) {
            System.out.println(name + " 유저가 삭제되었습니다.");
        }else{
            System.out.println(name + " 유저 삭제 실패하였습니다.");
        }
    }

}
