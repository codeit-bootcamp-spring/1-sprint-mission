package discodeit.service.file;

import discodeit.service.ChannelService;
import discodeit.service.MessageService;
import discodeit.service.ServiceFactory;
import discodeit.service.UserService;

public class FileServiceFactory implements ServiceFactory {
    private final FileUserService fileUserService;
    private final FileChannelService filechannelService;
    private final FileMessageService fileMessageService;


    public FileServiceFactory() {
        fileUserService = FileUserService.getInstance();
        filechannelService = FileChannelService.getInstance();
        fileMessageService = FileMessageService.getInstance();

        //의존성 주입
        fileUserService.setService();
        filechannelService.setService();
        fileMessageService.setService();
    }


    //Getter
    @Override
    public UserService getUserService() {
        return fileUserService.getInstance();
    }

    @Override
    public ChannelService getChannelService() {
        return filechannelService.getInstance();
    }

    @Override
    public MessageService getMessageService() {
        return fileMessageService.getInstance();
    }
}
