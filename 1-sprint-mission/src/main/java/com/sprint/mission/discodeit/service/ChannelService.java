package com.sprint.mission.discodeit.service;


import java.util.UUID;

public interface ChannelService {
    void CreateChannelDefault(String name);
    <T> String ReadChannel(T user);
    String ReadChannelAll();
    boolean UpdateChannelName(UUID id, String name);
    boolean UpdateChannelName(String ChannelName, String name);
    boolean DeleteChannel(UUID id);
    boolean DeleteChannel(String Name);

}
