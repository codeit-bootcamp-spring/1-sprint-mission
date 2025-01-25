package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;

public class JavaApplication {
    static User setupUser(UserService userService) {
        User user = userService.createUser("woody", "woody@codeit.com");
        return user;
    }

    static Channel setupChannel(ChannelService channelService) {
        Channel channel = channelService.createChannel("공지", "공지 채널입니다.");
        return channel;
    }

    static void messageCreateTest(MessageService messageService, Channel channel, User author) {
        Message message = messageService.createMessage(author.getId(), channel.getId(), "안녕하세요.");
        System.out.println("메시지 생성: " + message.getId());
    }

    public static void main(String[] args) {
        // JCF Repository를 사용하는 경우
        UserRepository userJcfRepo = new JCFUserRepository();
        ChannelRepository channelJcfRepo = new JCFChannelRepository();
        MessageRepository messageJcfRepo = new JCFMessageRepository();

        UserService userService = new BasicUserService(userJcfRepo);
        ChannelService channelService = new BasicChannelService(channelJcfRepo);
        MessageService messageService = new BasicMessageService(messageJcfRepo, userService, channelService);

        // File Repository를 사용하는 경우
        UserRepository userFileRepo = new FileUserRepository();
        ChannelRepository channelFileRepo = new FileChannelRepository();
        MessageRepository messageFileRepo = new FileMessageRepository();

        UserService userFileService = new BasicUserService(userFileRepo);
        ChannelService channelFileService = new BasicChannelService(channelFileRepo);
        MessageService messageFileService = new BasicMessageService(messageFileRepo, userFileService, channelFileService);

        // 테스트 실행
        User user = setupUser(userService);
        Channel channel = setupChannel(channelService);
        messageCreateTest(messageService, channel, user);
    }
}
