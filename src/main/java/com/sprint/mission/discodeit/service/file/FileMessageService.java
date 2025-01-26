package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.UUID;

public class FileMessageService implements MessageService {
    private static volatile FileMessageService instance;
    private final String FILE_NAME = "src/main/java/serialized/messages.ser";
    private final Map<UUID, Message> data;
    private final UserService userService;
    private final ChannelService channelService;

    private FileMessageService(UserService userService, ChannelService channelService) {
        this.data = loadData();
        this.userService = userService;
        this.channelService = channelService;
    }

    protected static FileMessageService getInstance(UserService userService, ChannelService channelService) {
        if (instance == null) {
            synchronized (FileMessageService.class) {
                if (instance == null) {
                    instance = new FileMessageService(userService, channelService);
                }
            }
        }
        return instance;
    }

    @Override
    public Message sendMessage(Message newMessage, List<User> allUsers) {
        validateChannel(newMessage.getChannel().getId());
        validateUser(newMessage.getAuthor().getId());
        data.put(newMessage.getId(), newMessage);

        // 맨션된 사용자 파싱 및 알림
        List<User> mentionedUsers = parseMentions(newMessage, allUsers);
        notifyMentions(newMessage.getAuthor(), mentionedUsers);

        saveData();
        return newMessage;
    }

    @Override
    public List<Message> getChannelMessages(UUID channelId) {
        validateChannel(channelId);
        return data.values().stream()
                .filter(msg -> msg.getChannel().getId().equals(channelId))
                .toList();
    }

    @Override
    public List<Message> getUserMessages(User author) {
        validateUser(author.getId());
        return data.values().stream()
                .filter(msg -> msg.getAuthor().equals(author))
                .toList();
    }

    @Override
    public boolean editMessage(UUID id, String updatedContent) {
        boolean updated = data.computeIfPresent(id, (key, message) -> {
            message.update(updatedContent);
            return message;
        }) != null;
        if (updated) saveData();
        return updated;
    }

    @Override
    public boolean deleteMessage(UUID id) {
        boolean deleted = (data.remove(id) != null);
        if (deleted) saveData();
        return deleted;
    }

    private void validateChannel(UUID channelId) {
        channelService.getChannelDetails(channelId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "채널 id : " + channelId + " **존재하지 않는 채널입니다!**"));
    }

    private void validateUser(UUID userId) {
        userService.getUserDetails(userId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "유저 id : " + userId + " **존재하지 않는 유저입니다!**"));
    }

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
            System.out.println("[" + sender.getName() + "]님이 ["
                    + user.getName() + "]님을 멘션했어요!");
        }
    }

    private Map<UUID, Message> loadData() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return new ConcurrentHashMap<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Map<UUID, Message>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ConcurrentHashMap<>();
        }
    }

    private void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
