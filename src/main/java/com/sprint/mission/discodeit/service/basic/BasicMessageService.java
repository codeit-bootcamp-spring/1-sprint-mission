package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BasicMessageService implements MessageService {
    private static volatile BasicMessageService instance;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    public BasicMessageService(MessageRepository messageRepository, UserRepository userRepository, ChannelRepository channelRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.channelRepository = channelRepository;
    }

    protected static BasicMessageService getInstance(MessageRepository messageRepository, UserRepository userRepository, ChannelRepository channelRepository) {
        if (instance == null) {
            synchronized (BasicMessageService.class) {
                if (instance == null) {
                    instance = new BasicMessageService(messageRepository, userRepository, channelRepository);
                }
            }
        }
        return instance;
    }

    @Override
    public Message sendMessage(Message newMessage) {
        validateChannel(newMessage.getChannel().getId());
        validateUser(newMessage.getAuthor().getId());
        messageRepository.save(newMessage);
        // mentions 인식 및 알림
        List<User> mentionedUsers = parseMentions(newMessage).stream()
                .map(userRepository::findByName)
                .flatMap(Optional::stream)
                .toList();

        newMessage.getMentions().clear();
        newMessage.getMentions().addAll(mentionedUsers);

        notifyMentions(newMessage.getAuthor(), mentionedUsers);
        return newMessage;
    }

    @Override
    public Message sendMessage(Message message, List<User> allUsers) {
        return sendMessage(message);
    }

    @Override
    public List<Message> getChannelMessages(UUID channelId) {
        validateChannel(channelId);

        return messageRepository.findAll().stream().filter(message -> message.getChannel().getId().equals(channelId)).toList();
    }


    @Override
    public List<Message> getUserMessages(User author) {
        validateUser(author.getId());
        return messageRepository.findAll().stream().filter(m -> m.getAuthor().equals(author)).toList();
    }

    @Override
    public boolean editMessage(UUID id, String updatedContent) {
        return messageRepository.update(id, updatedContent);
    }

    @Override
    public boolean deleteMessage(UUID id) {
        return messageRepository.delete(id);
    }

    //맨션된 유저들 리스트에 추가
    private Set<String> parseMentions(Message message) {
        Pattern pattern = Pattern.compile("@(\\w+)"); // 닉네임 후보(@뒤에 연속되는 문자열)
        Matcher matcher = pattern.matcher(message.getContent());
        Set<String> mentionedNames = new HashSet<>();
        while (matcher.find()) {
            String name = matcher.group(1);
            mentionedNames.add(name);
        }

        return mentionedNames;
    }

    private void notifyMentions(User sender, List<User> mentions) {
        for (User user : mentions) {
            // 콘솔로 맨션된 유저들에게 알림 출력
            System.out.println("[" + sender.getName() + "]님이 [" + user.getName() + "]님을 멘션했어요!");
        }
    }

    private void validateChannel(UUID channelId) {
        if (channelRepository.findById(channelId).isEmpty()) {
            throw new IllegalArgumentException("채널 id : " + channelId + " **존재하지 않는 채널입니다!**");
        }
    }

    private void validateUser(UUID userId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new IllegalArgumentException("채널 id : " + userId + " **존재하지 않는 채널입니다!**");
        }
    }
}
