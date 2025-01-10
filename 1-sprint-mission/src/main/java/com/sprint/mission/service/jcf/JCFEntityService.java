package com.sprint.mission.service.jcf;


import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.repository.ChannelRepository;
import com.sprint.mission.repository.MessageRepository;
import com.sprint.mission.repository.UserRepository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JCFEntityService implements EntityService {
    private static final UserRepository userRepository = UserRepository.getInstance();
    private static final MessageRepository messageRepository = MessageRepository.getInstance();
    private static final ChannelRepository channelRepository = ChannelRepository.getInstance();
    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy년 MM월 dd일 hh:mm:ss");

    private static final JCFEntityService INSTANCE = new JCFEntityService();
    private JCFEntityService(){}

    public static JCFEntityService getInstance() {
        return INSTANCE;
    }


    // User

    @Override
    public User creatUser(String nickName) {
        User user = userRepository.createUser(nickName);
        if(userRepository.checkUserId(user.getId())){
            while(!userRepository.checkUserId(user.getId())){
                user.setId(UUID.randomUUID());
            }
        }

        System.out.println("환영합니다!");
        return user;

    }

    @Override
    public void modifyUser(User user , String newName) {
        if (userRepository.checkUserId(user.getId())) {
            userRepository.update(user.getId(), newName);
            channelRepository.checkJoinChannel(user.getId(), newName);
            messageRepository.modifyUserName(user.getId(), newName);
            System.out.println("성공적으로 수정하였습니다");
            return;
        }

        System.out.println("존재하지 않는 계정입니다");
    }

    @Override
    public void deleteUser(User user) {
        if (userRepository.checkUserId(user.getId())) {
            userRepository.deleteUser(user.getId());
            channelRepository.deleteUser(user.getId());
            messageRepository.modifyUserName(user.getId(),"UNDEFINED");
            System.out.println("성공적으로 삭제하였습니다");
            return;
        }

        System.out.println("존재하지 않는 계정입니다");
    }

    @Override
    public void showInfoUser(User user) {
        System.out.println(user.getName() + "(" + user.getId() + ")"
                + "\n생성 일자 : " + format.format(user.getCreatedAt())
                + "\n최종 수정 일자 : " + format.format(user.getUpdatedAt()));
    }


    // Channel
    @Override
    public Channel saveChannel(String nickName, User user) {
        Channel channel = channelRepository.createChannel(nickName,user.getId(), user.getName());
        if(channelRepository.checkChannelContains(channel.getChannelId())){
            while(!channelRepository.checkChannelContains(channel.getChannelId())){
                channel.setChannelId(UUID.randomUUID());
            }
        }
        System.out.println("새 채널에 오신 것을 환영합니다!");

        return channel;
    }

    public void joinChannel(Channel channel, User user) {
        if (channelRepository.getChannelId(channel.getChannelId())) {
            channelRepository.joinChannel(channel.getChannelId(), user.getId(), user.getName());
            System.out.println("새 채널에 오신 것을 환영합니다!");
        }

        System.out.println("존재하지 않는 채널입니다");
    }

    @Override
    public void deleteChannel(Channel channel) {
        if (channelRepository.checkChannelContains(channel.getChannelId())) {
            System.out.println("존재하지 않는 채널입니다");
            return;
        }

        channelRepository.deleteChannel(channel.getChannelId());
        messageRepository.deletedChannelMessage(channel.getChannelId());
        System.out.println("채널 삭제에 성공하였습니다");
    }

    @Override
    public void modifyChannel(Channel channel, String newName) {
        if (channelRepository.getChannelId(channel.getChannelId())) {
            channelRepository.modifyChannel(channel.getChannelId(), newName);
            messageRepository.modifyChannelName(channel.getChannelId(),newName);
            System.out.println("채널명을 수정하였습니다");
            return;
        }

        System.out.println("존재하지 않는 채널입니다");
    }

    @Override
    public void showInfoChannel(Channel channel) {
        System.out.println(channel.getName() + "(" + channel.getChannelId() + ")"
                + "\n생성 일자 : " + format.format(channel.getCreatedAt())
                + "\n최종 수정 일자 : " + format.format(channel.getUpdatedAt()));
        System.out.println("참여 중인 유저 "+ channelRepository.joinUserList(channel.getChannelId()));
    }


    // message
    @Override
    public Message saveMessage(String content, User user, Channel channel) {
        Message message = messageRepository.createMessage(content,user.getId(),
                user.getName(),channel.getChannelId(), channel.getName());

        if(messageRepository.checkMessageContains(message.getMessageId())){
            while(!messageRepository.checkMessageContains(message.getMessageId())){
                message.setMessageId(UUID.randomUUID());
            }
        }
        System.out.println("메세지 전송 성공!");
        return message;
    }

    @Override
    public void deleteMessage(Message message) {
        if(messageRepository.checkMessageContains(message.getMessageId())){
            messageRepository.deleteMessage(message.getMessageId());
            System.out.println("메세지 삭제 성공!");
        }

        System.out.println("메세지 삭제에 실패하였습니다");
    }

    @Override
    public void modifyMessage(Message message, String content) {
        if(messageRepository.checkMessageContains(message.getMessageId())){
            messageRepository.modifyMessage(message.getUserId(),message.getMessageId(),content);
            System.out.println("메세지 수정 성공!");
            return;
        }

        System.out.println("메세지 수정에 실패하였습니다");
    }

    @Override
    public void showInfoMessage(Message message) {
        System.out.println(message.getText() + " (" + message.getUserName() + ") [" +
                format.format(message.getUpdatedAt()) + "]" + " - " + message.getChannelName());
    }
}