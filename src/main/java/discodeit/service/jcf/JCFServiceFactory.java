package discodeit.service.jcf;

import discodeit.service.ChannelService;
import discodeit.service.MessageService;
import discodeit.service.ServiceFactory;
import discodeit.service.UserService;

public class JCFServiceFactory implements ServiceFactory {

    private static class JCFServiceFactoryHolder {
        private static final ServiceFactory INSTANCE = new JCFServiceFactory();
    }

    private final UserService jcfUserService;
    private final ChannelService jcfChannelService;
    private final MessageService jcfMessageService;

    private JCFServiceFactory() {
        jcfUserService = JCFUserService.getInstance();
        jcfChannelService = JCFChannelService.getInstance();
        jcfMessageService = JCFMessageService.getInstance();

        setDependencies();
    }

    public static ServiceFactory getInstance() {
        return  JCFServiceFactoryHolder.INSTANCE;
    }

    private void setDependencies() {
        jcfUserService.updateChannelService(jcfChannelService);

        jcfChannelService.updateUserService(jcfUserService);
        jcfChannelService.updateMessageService(jcfMessageService);

        jcfMessageService.updateUserService(jcfUserService);
        jcfMessageService.updateChannelService(jcfChannelService);
    }

    @Override
    public UserService getUserService() {
        return jcfUserService;
    }

    @Override
    public ChannelService getChannelService() {
        return jcfChannelService;
    }

    @Override
    public MessageService getMessageService() {
        return jcfMessageService;
    }
}
