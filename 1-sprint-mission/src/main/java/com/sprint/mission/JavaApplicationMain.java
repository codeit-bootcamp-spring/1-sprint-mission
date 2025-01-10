package com.sprint.mission;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.service.jcf.JCFEntityService;

import java.util.UUID;

public class JavaApplicationMain {
    static JCFEntityService jcfEntityService = JCFEntityService.getInstance();
    public static void main(String[] args) {
        User user1 = jcfEntityService.creatUser("Name1");
        jcfEntityService.showInfoUser(user1);

        System.out.println(" ");

        jcfEntityService.modifyUser(user1,"ReName1");
        jcfEntityService.showInfoUser(user1);

        System.out.println(" ");
        Channel channel1 = jcfEntityService.saveChannel("Channel1",user1);
        jcfEntityService.showInfoChannel(channel1);

        System.out.println(" ");
        Message message1 = jcfEntityService.saveMessage("Hello World!", user1,channel1);
        jcfEntityService.showInfoMessage(message1);

        System.out.println(" ");
        jcfEntityService.modifyChannel(channel1,"ReNameChannel1");
        jcfEntityService.showInfoChannel(channel1);

        System.out.println(" ");
        jcfEntityService.showInfoMessage(message1);

        System.out.println(" ");
        jcfEntityService.modifyMessage(message1,"Hello World Hello");
        jcfEntityService.showInfoMessage(message1);
    }
}
