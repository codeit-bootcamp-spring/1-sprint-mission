package com.sprint.mission.discodeit.service.interfaces;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.UserDto;

import java.io.IOException;

public interface ChannelService {
    public void showInfoChannel(ChannelDto channel) throws IOException, ClassNotFoundException;
    public void saveChannel(ChannelDto channelDto) throws IOException, ClassNotFoundException;
    public void deleteChannel(ChannelDto channel) throws IOException, ClassNotFoundException;
    public void modifyChannel(ChannelDto channel) throws IOException, ClassNotFoundException;

}
