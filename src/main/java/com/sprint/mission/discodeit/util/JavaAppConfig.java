package com.sprint.mission.discodeit.util;


import com.sprint.mission.discodeit.repository.jcf.JcfBinaryContentRepository;
import com.sprint.mission.discodeit.repository.jcf.JcfChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JcfMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JcfParticipantRepository;
import com.sprint.mission.discodeit.repository.jcf.JcfReadStatusRepository;
import com.sprint.mission.discodeit.repository.jcf.JcfUserRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicBinaryContentService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JavaAppConfig {
    public UserService userService(){
        return new BasicUserService(new JcfUserRepository());
//        return new FileUserService(new FileUserRespository());
    }
    public MessageService messageService(){
        return new BasicMessageService(new JcfMessageRepository(),new JcfBinaryContentRepository());
    }
    public ChannelService channelService(){
        return new BasicChannelService(new JcfChannelRepository(), new JcfMessageRepository(), new JcfReadStatusRepository(), new JcfParticipantRepository());
    }
    public BinaryContentService binaryContentService(){
        return new BasicBinaryContentService(new JcfBinaryContentRepository());
    }
}
