package com.sprint.mission.discodit.Service.JCF;

import com.sprint.mission.discodit.Entity.Channel;
import com.sprint.mission.discodit.Entity.Message;
import com.sprint.mission.discodit.Service.ChannelService;

import java.util.*;
import java.util.stream.Collectors;

public class JcfChannelService implements ChannelService {
    private final Map<UUID, Channel> list; //채널 리스트
    private final Map<UUID, List<UUID>> messages = new HashMap<>(); //채널에서 보유 중인 메시지들 ( 변경 가능성 없음 )
    private JcfMessageService jcfMessageService;
    private static volatile JcfChannelService instance;

    public static JcfChannelService getInstance() {
        if (instance == null) {
            synchronized (JcfUserService.class) {
                if (instance == null) {
                    instance = new JcfChannelService(new HashMap<>());
                }
            }
        }
        return instance;
    }
    public void setJcfMessageService(JcfMessageService jcfMessageService) {
        this.jcfMessageService = jcfMessageService;
    }

    public UUID addMessage_By_Channel(UUID channelId, UUID sender, String content) {
        if(!validationUUID(channelId)||sender==null){ return null; }
        UUID messageId = jcfMessageService.createMessage(sender, content);
        messages.get(channelId).add(messageId); //검증 필요
        return messageId;
    }

    private boolean validationUUID(UUID channelId) {
        return list.containsKey(channelId);
    }

    public List<Message> messages(UUID channelId) {
        if(!validationUUID(channelId)){ return null; }
        List<Message> collect = messages.get(channelId).stream().map(s -> jcfMessageService.getMessage(s)).collect(Collectors.toList());
        return new ArrayList<>(collect);
    }

    public JcfChannelService(Map<UUID, Channel> list) {
        this.list = list;
    }

    @Override
    public UUID createChannel(String userName) {
        Channel channel = new Channel(userName);
        list.put(channel.getChannel(), channel);
        messages.put(channel.getChannel(), new ArrayList<>());
        return channel.getChannel();
    }

    @Override
    public Channel getChannel(UUID id) {
        Channel channel = list.get(id);
        return channel;
    }

    @Override
    public List<Channel> getChannels() {
        List<Channel> collect = list.entrySet().stream().map(s -> s.getValue()).collect(Collectors.toList());
        return new ArrayList<>(collect);
    }

    @Override
    public void setChannel(UUID id, String name) {
        Channel channel = getChannel(id);
        channel.update(name);
        //리턴해서 출력하는 방안 고려
    }

    @Override
    public void deleteChannel(UUID channelId) {
        if (list.containsKey(channelId)) {
            List<Message> collect = messages.get(channelId).stream().map(s -> jcfMessageService.getMessage(s)).collect(Collectors.toList());
            for (Message message : collect) {
                try {
                    jcfMessageService.deleteMessage(message.getMessageId());
                } catch (NullPointerException e) {
                } //삭제라서 nullPointException 무시했습니다.
            }
            list.remove(channelId);
        } else {
            System.out.println("채널을 찾을 수 없습니다.");
        }
    }

}
