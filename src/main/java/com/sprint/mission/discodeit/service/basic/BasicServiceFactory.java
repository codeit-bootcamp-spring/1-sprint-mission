//package com.sprint.mission.discodeit.service.basic;
//
//import com.sprint.mission.discodeit.repository.ChannelRepository;
//import com.sprint.mission.discodeit.repository.MessageRepository;
//import com.sprint.mission.discodeit.repository.UserRepository;
//import com.sprint.mission.discodeit.service.ChannelService;
//import com.sprint.mission.discodeit.service.MessageService;
//import com.sprint.mission.discodeit.service.ServiceFactory;
//import com.sprint.mission.discodeit.service.UserService;
//
//public class BasicServiceFactory implements ServiceFactory {
//    private final UserService userService;
//    private final ChannelService channelService;
//    private final MessageService messageService;
//
//    //생성자 주입
//    public BasicServiceFactory(
//            UserRepository userRepository,
//            ChannelRepository channelRepository,
//            MessageRepository messageRepository
//    ) {
//        this.userService = BasicUserService.getInstance(userRepository);
//        this.channelService = BasicChannelService.getInstance(channelRepository);
//        this.messageService = BasicMessageService.getInstance(messageRepository, userRepository, channelRepository);
//
//    }
//
//    @Override
//    public UserService getUserService() {
//        return this.userService;
//    }
//
//    @Override
//    public ChannelService getChannelService() {
//        return this.channelService;
//    }
//
//    @Override
//    public MessageService getMessageService() {
//        return this.messageService;
//    }
//}
