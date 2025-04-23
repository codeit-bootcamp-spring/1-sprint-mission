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

    public void setUpJfcChannel(){
        this.channelService=new JCFChannelService();
    }

    public void setUpFileChannel(){
        this.channelService=new FileChannelService();
    }


    @Override
    public void createNewChannel(String name) {
        channelService.createNewChannel(name);
    }

    @Override
    public <T> String readChannel(T user) {
        return channelService.readChannel(user);
    }

    @Override
    public String readChannelAll() {
        return channelService.readChannelAll();
    }

    @Override
    public boolean updateChannelName(UUID id, String name) {
        return channelService.updateChannelName(id,name);
    }

    @Override
    public boolean updateChannelName(String ChannelName, String name) {
        return channelService.updateChannelName(ChannelName,name);
    }

    @Override
    public boolean deleteChannel(UUID id) {
        return channelService.deleteChannel(id);
    }

    @Override
    public boolean deleteChannel(String Name) {
        return channelService.deleteChannel(Name);
    }
}
