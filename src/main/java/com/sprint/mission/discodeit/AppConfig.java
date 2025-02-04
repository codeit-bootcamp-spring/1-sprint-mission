package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;

public class AppConfig {

    public ChannelService basicChannelService() {
        return new BasicChannelService(channelRepository(), new BasicMessageService(messageRepository()));
        //return new BasicChannelService(new FileChannelRepository());
    }

    // 채널 레포지토리
    private static ChannelRepository channelRepository() {
        return new FileChannelRepository();
    }

    public UserService basicUserService() {
        return new BasicUserService(userRepository());
    }

    // 유저 레포지토리
    private static UserRepository userRepository() {
        return new FileUserRepository();
    }

    public MessageService basicMessageService() {
        return new BasicMessageService(messageRepository());
    }


    // 메세지 레포지토리
    private static MessageRepository messageRepository() {
        return new FileMessageRepository();
    }
}
