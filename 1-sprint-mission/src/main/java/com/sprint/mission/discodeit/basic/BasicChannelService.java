package com.sprint.mission.discodeit.basic;

import com.sprint.mission.discodeit.DTO.ChannelDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.file.FileChannelService;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JFCUserStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service("BasicChannelService")
public class BasicChannelService implements ChannelService {

    private final JCFChannelService channelService;
    private final UserStatusService userStatusService;
    private final ReadStatusService readStatusService;

    @Autowired
    private BasicChannelService(@Qualifier("FileChannelService") JCFChannelService jcfChannelService,
                               @Qualifier("FileUserStatusService") UserStatusService userStatusService,
                               @Qualifier("FileReadStatusService") ReadStatusService readStatusService){
        this.channelService= jcfChannelService;
        this.userStatusService=userStatusService;
        this.readStatusService=readStatusService;
    }

    @Autowired
    public ChannelService setUpChannelService(@Qualifier("FileChannelService") JCFChannelService jcfChannelService,
                                              @Qualifier("FileUserStatusService") UserStatusService userStatusService,
                                              @Qualifier("FileReadStatusService") ReadStatusService readStatusService){
        return new BasicChannelService(jcfChannelService,userStatusService,readStatusService);
    }


    @Override
    public void createNewChannel(String name) {
        channelService.createNewChannel(name);

    }
    @Override
    public void createPrivateChannel() {
        channelService.createPrivateChannel();
    }

    @Override
    public <T> List<ChannelDto> readChannel(T user) {
        return channelService.readChannel(user);
    }

    @Override
    public List<ChannelDto> readChannelAll() {
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
        //채널의 모든 readStatusList 지우기
        readStatusService.delete(channelService.getReadStatusAll(id));
        userStatusService.delete (channelService.getUserStatusAll(id));
        return channelService.deleteChannel(id);
    }

    @Override
    public boolean deleteChannel(String Name) {
        readStatusService.delete(channelService.getReadStatusAll(Name));
        userStatusService.delete (channelService.getUserStatusAll(Name));
        return channelService.deleteChannel(Name);
    }

    @Override
    public <T, K> ReadStatus addUserToChannel(T channelName, K userName) {
        ReadStatus newReadStatus=channelService.addUserToChannel(channelName,userName);
        if(newReadStatus!=null)
            readStatusService.addReadStatus(newReadStatus);
        return newReadStatus;
    }



    @Override
    public void deleteUserToChannel(String channelName, String userName){
        channelService.deleteUserToChannel(channelName,userName);
    }



    @Override
    public boolean addMessageToChannel(String channelName,String title){
        return channelService.addMessageToChannel(channelName,title);
    }

    @Override
    public String readChannelInUser(String channelName) {
        return channelService.readChannelInUser(channelName);
    }

    @Override
    public String readChannelInMessage(String channelName) {
        return channelService.readChannelInMessage(channelName);
    }

    @Override
    public List<ChannelDto> findAllByUserId(UUID userId){
        return channelService.findAllByUserId(userId);
    }

    @Override
    public List<ChannelDto> findAllByUserName(String userName) {
        return channelService.findAllByUserName(userName);
    }

    @Override
    public boolean updateChannel(ChannelDto channelDto){
        return channelService.updateChannel(channelDto);
    }



}
