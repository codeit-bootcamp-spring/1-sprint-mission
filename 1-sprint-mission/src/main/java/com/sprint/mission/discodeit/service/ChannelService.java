package com.sprint.mission.discodeit.service;


import java.util.UUID;

public interface ChannelService {
    void CreateChannelDefault(String name);
    <T> String ReadChannel(T user);
    String ReadChannelAll();
    void UpdateChannelName(UUID id, String name);
    void UpdateChannelName(String ChannelName, String name);
    void DeleteChannel(UUID id);
    void DeleteChannel(String Name);

}
