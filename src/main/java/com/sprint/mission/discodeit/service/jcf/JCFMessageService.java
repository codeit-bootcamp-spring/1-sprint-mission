package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class JCFMessageService implements MessageService {
    private static volatile JCFMessageService instance; //싱글톤 패턴
    private final Map<UUID, Message> data;

    private JCFMessageService(){
        this.data = new ConcurrentHashMap<>();
    }

    //synchronized 키워드로 thread-safe 보장, lazy init, Double-check locking 기법 적용
    public static JCFMessageService getInstance(){
        if(instance == null){
            synchronized (JCFMessageService.class) {
                if(instance == null) {
                    instance = new JCFMessageService();
                }
            }
        }
        return instance;
    }

    @Override
    public Message sendMessage(Message newMessage, List<User> allUsers) {
        data.put(newMessage.getId(), newMessage);
        // mentions 인식 및 알림
        List<User> mentionedUsers = parseMentions(newMessage, allUsers);
        notifyMentions(newMessage.getAuthor(), mentionedUsers);
        return newMessage;
    }

    @Override
    public List<Message> getChannelMessages(UUID channelId) {
        return data.values().stream()
                .filter(message ->
                        message.getChannel().getId().equals(channelId))
                .toList();
    }


    @Override
    public List<Message> getUserMessage(User author) {
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
        List<User> mentions = new ArrayList<>();
        String content = message.getContent();
        if (content != null && !content.trim().isEmpty()) {
            for (User user : allUsers) {
                String mentionPattern = "@" + user.getName();
                if(content.contains(mentionPattern)){
                    mentions.add(user);
                }
            }
        }
        message.getMentions().clear();
        message.getMentions().addAll(mentions);
        return mentions;
    }

    private void notifyMentions(User sender, List<User> mentions) {
        for (User user : mentions) {
            // 콘솔 알림 출력
            System.out.println("[" + sender.getName() + "] 님이 [" + user.getName() + "] 님을 멘션했어요!");
        }
    }

}
