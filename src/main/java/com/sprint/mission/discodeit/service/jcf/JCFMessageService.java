package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class JCFMessageService implements MessageService {
    private static volatile JCFMessageService instance;
    private final Map<UUID, Message> data;
    private final UserService userService;
    private final ChannelService channelService;

    private JCFMessageService(UserService userService, ChannelService channelService) {
        this.data = new ConcurrentHashMap<>();
        this.userService = userService;
        this.channelService = channelService;
    }

    protected static JCFMessageService getInstance(UserService userService, ChannelService channelService) {
        if (instance == null) {
            synchronized (JCFMessageService.class) {
                if (instance == null) {
                    instance = new JCFMessageService(userService, channelService);
                }
            }
        }
        return instance;
    }

    @Override
    public Message sendMessage(Message message) {
        return sendMessage(message, new ArrayList<>());
    }

    @Override
    public Message sendMessage(Message newMessage, List<User> allUsers) {
        validateChannel(newMessage.getChannel().getId());
        validateUser(newMessage.getAuthor().getId());
        data.put(newMessage.getId(), newMessage);
        // mentions 인식 및 알림
        List<User> mentionedUsers = parseMentions(newMessage, allUsers);
        notifyMentions(newMessage.getAuthor(), mentionedUsers);
        return newMessage;
    }

    @Override
    public List<Message> getChannelMessages(UUID channelId) {
        validateChannel(channelId);

        return data.values().stream()
                .filter(message ->
                        message.getChannel().getId().equals(channelId))
                .toList();
    }


    @Override
    public List<Message> getUserMessages(User author) {
        validateUser(author.getId());
        return data.values().stream()
                .filter(message -> message.getAuthor().equals(author))
                .toList();
    }

    @Override
    public boolean editMessage(UUID id, String updatedContent) {
        return data.computeIfPresent(id, (key, message) -> {
            message.update(updatedContent);
            return message;
        }) != null;
    }

    @Override
    public boolean deleteMessage(UUID id) {
        return data.remove(id) != null;
    }

    //맨션된 유저들 리스트에 추가
    private List<User> parseMentions(Message message, List<User> allUsers) {
        List<User> mentions = allUsers.stream()
                .filter(user -> message.getContent().contains("@" + user.getName()))
                .toList();

        message.getMentions().clear();
        message.getMentions().addAll(mentions);
        return mentions;
    }

    private void notifyMentions(User sender, List<User> mentions) {
        for (User user : mentions) {
            // 콘솔로 맨션된 유저들에게 알림 출력
            System.out.println("[" + sender.getName() + "]님이 [" + user.getName() + "]님을 멘션했어요!");
        }
    }

    private void validateChannel(UUID channelId) {
        channelService.getChannelDetails(channelId)
                .orElseThrow(() -> new IllegalArgumentException("채널 id : " + channelId + " **존재하지 않는 채널입니다!**"));
    }

    private void validateUser(UUID userId) {
        userService.getUserDetails(userId)
                .orElseThrow(() -> new IllegalArgumentException("채널 id : " + userId + " **존재하지 않는 채널입니다!**"));
    }
}
