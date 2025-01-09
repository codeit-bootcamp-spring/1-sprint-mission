package com.sprint.mission.discodeit.serviece;


import com.sprint.mission.discodeit.entity.Channel;

import java.util.ArrayList;
import java.util.UUID;

public interface ChannelService {
    void CreateChaneel(String name);
    String ReadChannel(UUID id);
    String ReadChannel(String name);
    String ReadChannelAll();
    void UpdateChannelName(UUID id, String name);
    void UpdateChannelName(String ChannelName, String name);
    void DeleteChannel(UUID id);
    void DeleteChannel(String Name);

}
