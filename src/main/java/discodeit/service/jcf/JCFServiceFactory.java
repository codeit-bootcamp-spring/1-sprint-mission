package discodeit.service.jcf;

import discodeit.service.ChannelService;
import discodeit.service.MessageService;
import discodeit.service.ServiceFactory;
import discodeit.service.UserService;

public class JCFServiceFactory implements ServiceFactory {

    private static class JCFServiceFactoryHolder {
        private static final ServiceFactory INSTANCE = new JCFServiceFactory();
    }

    private final UserService userService;
    private final ChannelService channelService;
    private final MessageService messageService;

    private JCFServiceFactory() {
        userService = JCFUserService.getInstance();
        channelService = JCFChannelService.getInstance();
        messageService = JCFMessageService.getInstance();

        setDependencies();
    }

    public static ServiceFactory getInstance() {
        return  JCFServiceFactoryHolder.INSTANCE;
    }

    private void setDependencies() {
        messageService.updateUserService(userService);
        messageService.updateChannelService(channelService);
    }

    @Override
    public UserService getUserService() {
        return userService;
    }

    @Override
    public ChannelService getChannelService() {
        return channelService;
    }

    @Override
    public MessageService getMessageService() {
        return messageService;
    }
}
