package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.Channel;

public class JavaApplication {  // 클래스 선언

    public static void main(String[] args) {
        User user = new User("testuser", "test@example.com");
        System.out.println(user);

        Channel channel = new Channel("General");
        System.out.println(channel);

        Message message = new Message("Hello!", user.getId(), channel.getId());
        System.out.println(message);
    }

}
