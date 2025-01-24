package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMassageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import com.sprint.mission.discodeit.service.file.FileChannelService;
import com.sprint.mission.discodeit.service.file.FileMessageService;
import com.sprint.mission.discodeit.service.file.FileUserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

public class ServiceFactory {
    //factory 패턴 : 의존성 주입 한 곳에서
    private static final JCFChannelService channelService = JCFChannelService.getInstance();
    private static final JCFMessageService messageService = JCFMessageService.getInstance();
    private static final JCFUserService userService = JCFUserService.getInstance();

    private static final FileChannelService channelService_f = FileChannelService.getInstance();
    private static final FileMessageService messageService_f = FileMessageService.getInstance();
    private static final FileUserService userService_f = FileUserService.getInstance();

    public static FileChannelService getChannelService_f() {return channelService_f;}

    public static FileMessageService getMessageService_f() {
        return messageService_f;
    }

    public static FileUserService getUserService_f() {
        return userService_f;
    }

    public static ChannelService getChannelService() {
        return channelService;
    }

    public static MessageService getMessageService() {
        return messageService;
    }

    public static UserService getUserService() {
        return userService;
    }
}
