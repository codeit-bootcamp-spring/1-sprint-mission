package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.message.Message;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    @Override
    public Message sendMessage(Message newMessage) {
        validateChannel(newMessage.getChannelId());
        validateUser(newMessage.getAuthorId());
        // messageRepository.save(newMessage);
        Set<UUID> mentionedIds = parseMentions(newMessage.getContent());

        //불변성 보장
        Message messageToSave = new Message(
                newMessage.getContent(),
                newMessage.getAuthorId(),
                newMessage.getChannelId()
        );

        messageToSave.addMentions(mentionedIds);

        notifyMentions(newMessage.getAuthorId(), mentionedIds);

        return messageRepository.save(messageToSave);
    }

    /**
     * @deprecated 새로운 sendMessage(Message newMessage) 사용을 권장합니다.
     * 메시지 전송 시, List<User> allUsers를 매번 파라미터로 넘겨주는 불필요함 개선
     */
    @Deprecated
    public Message sendMessage(Message message, List<User> allUsers) {
        return sendMessage(message);
    }

    @Override
    public List<Message> getChannelMessages(UUID channelId) {
        validateChannel(channelId);

        return messageRepository.findAll().stream().filter(message -> message.getChannelId().equals(channelId)).toList();
    }


    @Override
    public List<Message> getUserMessages(UUID authorId) {
        validateUser(authorId);
        return messageRepository.findAll().stream().filter(m -> m.getAuthorId().equals(authorId)).toList();
    }

    @Override
    public boolean editMessage(UUID id, String updatedContent) {
        Optional<Message> byId = messageRepository.findById(id);
        if (byId.isPresent()) {
            Message message = byId.get();
            message.update(updatedContent);
            messageRepository.save(message);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteMessage(UUID id) {
        return messageRepository.delete(id);
    }

    //맨션된 유저들 리스트에 추가
    private Set<UUID> parseMentions(String content) {
        Pattern pattern = Pattern.compile("@(\\w+)"); // 닉네임 후보(@뒤에 연속되는 문자열)
        Matcher matcher = pattern.matcher(content);
        Set<UUID> mentionedIds = new HashSet<>();
        while (matcher.find()) {
            String name = matcher.group(1);
            userRepository.findByName(name)
                    .ifPresent(user ->
                            mentionedIds.add(user.getId()));
        }

        return mentionedIds;
    }

    private void notifyMentions(UUID senderId, Set<UUID> mentionIds) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(NoSuchElementException::new);

        mentionIds.stream()
                .map(userRepository::findById)
                .flatMap(Optional::stream)// Optional이 비어있으면 해당 요소 제외
                .forEach(user ->
                        System.out.printf("[%s]님이 [%s]님을 멘션했어요!%n",
                                sender.getName(), user.getName())
                );
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
