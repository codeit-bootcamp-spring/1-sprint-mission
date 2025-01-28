package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;


import com.sprint.mission.discodeit.service.file.FileUser;
import com.sprint.mission.discodeit.service.jcf.JCF_user;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;


public class JavaApplication {

    public static void main(String[] args) {
        FileUser fileUser = new FileUser();

        fileUser.creat("admin");
        System.out.println("asdsd");
        List<User> userId = fileUser.findByAll();
        System.out.println(userId);
        for(User item : userId) {
            System.out.println(item.getId());
        }

    }
}
