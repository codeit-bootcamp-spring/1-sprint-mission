package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;


import com.sprint.mission.discodeit.service.file.FileChannel;
import com.sprint.mission.discodeit.service.file.FileMessage;
import com.sprint.mission.discodeit.service.file.FileUser;
import java.util.List;
import java.util.UUID;


public class JavaApplication {

    public static void main(String[] args) {
        FileUser fileUser = new FileUser();
        FileMessage fileMessage = new FileMessage();
        FileChannel fileChannel = new FileChannel();


        fileUser.creat("admin");
        fileUser.creat("admin1");
        fileUser.creat("admin2");
        UUID user1 = fileUser.findByName("admin");
        List<User> userId = fileUser.findByAll();
        for(User item : userId) {
            System.out.println(item.getId());
        }
        fileUser.update(user1, "updateName");
        List<User> users = fileUser.findByAll();
        for(User item : users) {
            System.out.println(item);
        }

        fileUser.delete(user1);
        List<User> userList = fileUser.findByAll();
        for(User item : userList) {
            System.out.println(item);
        }

    }
}
