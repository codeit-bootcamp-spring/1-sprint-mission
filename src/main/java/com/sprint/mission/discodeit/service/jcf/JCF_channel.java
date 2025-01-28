package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class JCF_channel implements ChannelService {
    private final List<Channel> channelList;

    public JCF_channel() {
        this.channelList = new ArrayList<>();
    }
    @Override
    public void creat(String title) {
        Channel channel = new Channel(title);
        channelList.add(channel);
        System.out.println(channelList);
    }

    @Override
    public void addMessage(String messageContent, UUID channelId, UUID userId,
        JCF_message jcfMessage, JCF_user jcfUser) {
        UUID messageId = jcfMessage.creat(userId, messageContent, channelId);
        jcfUser.addMessage(messageId, userId);
        channelList.stream().filter(channel -> channel.getId().equals(channelId))
            .forEach(channel -> channel.addMessage(messageId));
    }

    @Override
    public void delete(UUID channelId, JCF_message jcfMessage) {
        Optional<Channel> getChannel = channelList.stream().filter(channel -> channel.getId().equals(channelId)).findFirst();
        if (getChannel.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }
        else {
            jcfMessage.DeleteMessageList(getChannel.get().getMessageIdList());
            channelList.remove(getChannel.get());
        }
    }
    //여기아님
    @Override
    public List<UUID> getMessageIdList(UUID channelId) {
        return channelList.stream()
            .filter(it -> it.getId().equals(channelId))
            .map(Channel::getMessageIdList)
            .findFirst()
            .orElse(null);
    }
    //여기아님
    @Override
    public List<String> getMessageList(UUID channelId, JCF_message jcfMessage, JCF_user jcfUser) {
        List<UUID> messageIdList = channelList.stream()
            .filter(it -> it.getId().equals(channelId))
            .findFirst()
            .map(Channel::getMessageIdList)
            .orElse(null);
        System.out.println(messageIdList);
        return jcfMessage.getMessageList(messageIdList, jcfUser);
    }

    @Override
    public void update(UUID channelId, String title) {
        channelList.stream().filter(channel -> channel.getId().equals(channelId))
            .forEach(channel -> channel.updateTitle(title));
    }

    @Override
    public UUID findByTitle(String title) {
        Optional<Channel> channel = channelList.stream().filter(channel_id -> channel_id.getTitle().equals(title)).findFirst();

        if(channel.isPresent()){
            return channel.get().getId();
        }
        else{
            return null;
        }
    }

}
