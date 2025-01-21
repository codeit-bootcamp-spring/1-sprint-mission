package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.repository.file.FileChannelService;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.UUID;

public class BasicChannelService implements ChannelService {

    private ChannelService channelService;

    public BasicChannelService(){
        this.channelService= new FileChannelService();
    }

    public void SetUpJfcChannel(){
        this.channelService=new JCFChannelService();
    }

    public void SetUpFileChannel(){
        this.channelService=new FileChannelService();
    }


    @Override
    public void CreateChannelDefault(String name) {
        channelService.CreateChannelDefault(name);
    }

    @Override
    public <T> String ReadChannel(T user) {
        return channelService.ReadChannel(user);
    }

    @Override
    public String ReadChannelAll() {
        return channelService.ReadChannelAll();
    }

    @Override
    public boolean UpdateChannelName(UUID id, String name) {
        return channelService.UpdateChannelName(id,name);
    }

    @Override
    public boolean UpdateChannelName(String ChannelName, String name) {
        return channelService.UpdateChannelName(ChannelName,name);
    }

    @Override
    public boolean DeleteChannel(UUID id) {
        return channelService.DeleteChannel(id);
    }

    @Override
    public boolean DeleteChannel(String Name) {
        return channelService.DeleteChannel(Name);
    }
}
