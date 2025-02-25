package com.sprint.mission.discodeit.repository.file.interfacepac;

import com.sprint.mission.discodeit.entity.Message;

import java.io.*;
import java.util.List;
import java.util.UUID;

public interface FileMessageRepositoryInterface {
    public Message createMessage(String text, UUID userId, String userName , UUID channelId, String channelName) throws IOException, ClassNotFoundException;
    public void modifyMessage(UUID Userid, UUID messageId ,String text) throws IOException, ClassNotFoundException;
    public List<Message> getMessagesOfChannel(UUID channelId) throws IOException, ClassNotFoundException;
    public void deleteMessage(UUID messageId) throws IOException, ClassNotFoundException;
    public boolean checkMessageContains(UUID id) throws IOException, ClassNotFoundException;
    public void modifyUserName(UUID userId, String newUserName) throws IOException, ClassNotFoundException;
    public void modifyChannelName(UUID channelId,String newChannelId) throws IOException, ClassNotFoundException;
    public void deletedChannelMessage(UUID channelId) throws IOException, ClassNotFoundException;
    public Message getMessage(Message message) throws IOException, ClassNotFoundException;
    public List<Message> getMessages() throws IOException, ClassNotFoundException;
}
