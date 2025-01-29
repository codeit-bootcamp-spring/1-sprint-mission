package discodeit.service.jcf;

import discodeit.service.ChannelService;
import discodeit.service.MessageService;
import discodeit.service.ServiceFactory;
import discodeit.service.UserService;

// JCFServiceFactory
public class JCFServiceFactory implements ServiceFactory {

    private final JCFUserService jcfUserService;
    private final JCFChannelService jcfchannelService;
    private final JCFMessageService jcfMessageService;


    public JCFServiceFactory() {
        jcfUserService = JCFUserService.getInstance();
        jcfchannelService = JCFChannelService.getInstance();
        jcfMessageService = JCFMessageService.getInstance();

        //의존성 주입
        jcfUserService.setService();
        jcfchannelService.setService();
        jcfMessageService.setService();
    }


    //Getter
    @Override
    public UserService getUserService() {
        return jcfUserService.getInstance();
    }

    @Override
    public ChannelService getChannelService() {
        return jcfchannelService.getInstance();
    }

    @Override
    public MessageService getMessageService() {
        return jcfMessageService.getInstance();
    }

}
