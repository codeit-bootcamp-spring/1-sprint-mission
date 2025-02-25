package com.sprint.mission.discodeit.repository.file.interfacepac;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.io.*;
import java.util.UUID;

public interface FileChannelRepositoryInterface {
    public Channel createChannel(String channelName, User user) throws IOException, ClassNotFoundException;
    public void joinChannel(UUID channelId, UUID userId, String userName) throws IOException, ClassNotFoundException;
    public void deleteChannel(UUID deleteChannelId) throws IOException, ClassNotFoundException;
    public void modifyChannelUserName(UUID checkingUserId, String newName) throws IOException, ClassNotFoundException;
    public boolean getChannelId(UUID id) throws IOException, ClassNotFoundException;
    public void modifyChannel(UUID channelId, String name) throws IOException, ClassNotFoundException;

}
