package com.sprint.mission.discodeit.util;


import com.sprint.mission.discodeit.repository.file.FileUserRespository;
import com.sprint.mission.discodeit.repository.jcf.JcfChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JcfMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JcfUserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.file.FileUserService;
import com.sprint.mission.discodeit.service.jcf.JcfChannelService;
import com.sprint.mission.discodeit.service.jcf.JcfMessageService;
import com.sprint.mission.discodeit.service.jcf.JcfUserService;

public class JavaAppConfig {
    public UserService userService(){
        return new JcfUserService(new JcfUserRepository());
//        return new FileUserService(new FileUserRespository());
    }
    public MessageService messageService(){
        return new JcfMessageService(new JcfMessageRepository());
    }
    public ChannelService channelService(){
        return new JcfChannelService(new JcfChannelRepository());
    }
}
