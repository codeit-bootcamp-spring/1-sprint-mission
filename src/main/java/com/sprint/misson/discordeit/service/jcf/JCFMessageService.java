package com.sprint.misson.discordeit.service.jcf;

import com.sprint.misson.discordeit.entity.Channel;
import com.sprint.misson.discordeit.entity.Message;
import com.sprint.misson.discordeit.entity.User;
import com.sprint.misson.discordeit.service.MessageService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class JCFMessageService implements MessageService {

    final HashMap<UUID, Message> data;

    public JCFMessageService() {
        this.data= new HashMap<>();
    }

    //생성
    @Override
    public boolean createMessage(User user, String content, Channel channel) {
        //todo
        //존재하는 유저? 존재하는 채널?
        Message message = new Message(user, content, channel);
        data.put( message.getId(), message );
        return false;
    }

    //모두 읽기
    @Override
    public List<Message> getMessages() {
        return new ArrayList<>(data.values());
    }

    //단일 조회 - uuid
    @Override
    public Message getMessageByUUID(String messageId) {
        return data.get(UUID.fromString( messageId ));
    }

    //다건 조회 - 내용
    @Override
    public List<Message> getMessageByContent(String content) {
        return data.values().stream()
                .filter( m -> m.getContent().contains( content ) )
                .collect( Collectors.toList());
    }

    //다건 조회 - 특정 작성자
    @Override
    public List<Message> getMessageBySender(User sender) {
        return data.values().stream()
                .filter( m -> m.getSender().equals( sender ) )
                .collect( Collectors.toList());
    }

    //다건 조회 - 생성 날짜
    @Override
    public List<Message> getMessageByCreatedAt(Long createdAt) {
        return List.of();
    }

    //다건 조회 - 특정 채널
    @Override
    public List<Message> getMessagesByChannel(Channel channel) {
        return data.values().stream()
                .filter( m -> m.getChannel().equals( channel ) )
                .collect( Collectors.toList());
    }

    @Override
    public boolean updateMessage(Message message) {
        //TODO - UserService Update 와 마찬가지.
        if(message == null){
            return false;
        }
        data.put( message.getId(), message );
        return true;
    }

    @Override
    public boolean deleteMessage(Message message) {
        return data.remove( message.getId() ) != null;
    }
}
