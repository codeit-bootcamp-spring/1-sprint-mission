package com.sprint.mission.discodeit.service;


import java.util.UUID;

public interface ChannelService {
    void createNewChannel(String name);
    <T> String readChannel(T user);
    String readChannelAll();
    boolean updateChannelName(UUID id, String name);
    boolean updateChannelName(String ChannelName, String name);
    boolean deleteChannel(UUID id);
    boolean deleteChannel(String Name);

}
