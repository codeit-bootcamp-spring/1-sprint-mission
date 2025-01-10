package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;

public class JavaApplication {
    public static void main(String[] args) {
        // 서비스 초기화
        UserService userService = new JCFUserService();
        ChannelService channelService = new JCFChannelService();
        MessageService messageService = new JCFMessageService(userService, channelService);

        // Step 1: User 생성
        User alice = new User("Alice");
        userService.create(alice);
        System.out.println("Created User: " + alice);


        // Step 2: Channel 생성
        Channel general = new Channel("General");
        channelService.create(general);
        System.out.println("Created Channel: " + general);


        // Step 3: Message 생성 (정상적인 경우)
        try {
            Message message = new Message("Hello, world!", alice.getId().toString(), general.getId().toString());
            messageService.create(message);
            System.out.println("Messages: " + messageService.findAll());
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
