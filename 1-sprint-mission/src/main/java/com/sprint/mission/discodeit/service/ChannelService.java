package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.DTO.ChannelDto;
import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    void createNewChannel(String name);
    <T> List<ChannelDto> readChannel(T user);
    List<ChannelDto> readChannelAll();
    boolean updateChannelName(UUID id, String name);
    boolean updateChannelName(String ChannelName, String name);
    boolean deleteChannel(UUID id);
    boolean deleteChannel(String Name);
    <T,K>ReadStatus addUserToChannel(T channelName, K userName);


    String readChannelInUser(String channelName);
    String readChannelInMessage(String channelName);
    List<ChannelDto> findAllByUserId(UUID userId);
    List<ChannelDto> findAllByUserName(String userName);
    boolean updateChannel(ChannelDto channelDto);
    boolean addMessageToChannel(String channelName,String title);
    <T,K,C,Q> boolean sendMessageInUser(T channel, K sender, C reciver,Q message);
    void createPrivateChannel( );
    void deleteUserToChannel(String channelName, String userName);
}
