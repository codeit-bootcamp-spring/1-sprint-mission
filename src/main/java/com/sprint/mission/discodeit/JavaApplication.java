package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannel;
import com.sprint.mission.discodeit.service.jcf.JCFMessage;
import com.sprint.mission.discodeit.service.jcf.JCFUser;

public class JavaApplication {
    public static void main(String[] args) {
        UserService userService = new JCFUser();
        ChannelService channelService = new JCFChannel();
        MessageService messageService = new JCFMessage();

        // 유저 등록
        User user1 = userService.createUser("Bob", "qwe123@google.com");
        User user2 = userService.createUser("Jackson", "Jackson@google.com");
        // 다건 조회
        userService.getAllUserList();
        // 단건 조회
        userService.getUserInfo(user1);

    }
}
