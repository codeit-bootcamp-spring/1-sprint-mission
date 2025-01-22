package discodeit.service;

import discodeit.service.jcf.JCFChannelService;
import discodeit.service.jcf.JCFMessageService;
import discodeit.service.jcf.JCFUserService;

public class ServiceFactory {

    private final JCFUserService jcfUserService;
    private final JCFChannelService jcfchannelService;
    private final JCFMessageService jcfMessageService;


    public ServiceFactory() {
        jcfUserService = JCFUserService.getInstance();
        jcfchannelService = JCFChannelService.getInstance();
        jcfMessageService = JCFMessageService.getInstance();
        
        //의존성 주입
        jcfMessageService.setService();
        jcfUserService.setService();
        jcfchannelService.setService();
    }


    //Getter
    public JCFUserService getJcfUserService() {
        return jcfUserService.getInstance();
    }

    public JCFChannelService getJcfchannelService() {
        return jcfchannelService.getInstance();
    }

    public JCFMessageService getJcfMessageService() {
        return jcfMessageService.getInstance();
    }

}
