package com.sprint.mission.discodeit.factory;

import com.sprint.mission.discodeit.service.jcf.JcfChannelService;
import com.sprint.mission.discodeit.service.jcf.JcfMessageService;
import com.sprint.mission.discodeit.service.jcf.JcfUserService;
import com.sprint.mission.discodeit.service.file.FileChannelService;
import com.sprint.mission.discodeit.service.file.FileUserService;

public class Factory {
    // 기존 Factory 코드
    private  JcfUserService jcfUserService = null;
    private  JcfChannelService jcfchannelService = null;
    private JcfMessageService jcfMessageService = null;

    private  FileUserService fileUserService = null;
    private  FileChannelService fileChannelService = null;
    private com.sprint.mission.discodeit.service.file.FileMessageService fileMessageService = null;

    public Factory(String name) {
        if(name.equals("jcf")){
            this.jcfUserService = JcfUserService.getInstance();
            this.jcfchannelService = JcfChannelService.getInstance();
            this.jcfMessageService = JcfMessageService.getInstance();
            this.jcfchannelService.setJcfMessageService(jcfMessageService);
            this.jcfUserService.setJcfMessageService(jcfMessageService);
        }
        if(name.equals("file")){
            this.fileUserService = FileUserService.getInstance();
            this.fileChannelService = FileChannelService.getInstance();
            this.fileMessageService = com.sprint.mission.discodeit.service.file.FileMessageService.getInstance();
            this.fileUserService.setFileMessageService(fileMessageService);
            this.fileChannelService.setFileMessageService(fileMessageService);
            this.fileMessageService.setFileChannelService(fileChannelService);
        }
    }
    public FileUserService getFileUserService() {
        return fileUserService;
    }
    public FileChannelService getFileChannelService() {
        return fileChannelService;
    }
    public com.sprint.mission.discodeit.service.file.FileMessageService getFileMessageService(){
        return fileMessageService;
    }

    public JcfUserService getJcfUserService() {
        return jcfUserService;
    }

    public JcfChannelService getJcfchannelService() {
        return jcfchannelService;
    }

    public JcfMessageService getJcfMessageService() {
        return jcfMessageService;
    }
}
