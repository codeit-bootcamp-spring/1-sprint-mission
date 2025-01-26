package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.io.*;



public class FileMessageService implements MessageService {
    private static final String FILE_PATH = "files/messages.ser";
    private List<Message> messages;
    private final ChannelService channelService;
    private final UserService userService;


    public FileMessageService(ChannelService channelService, UserService userService) {
        ensureDirectoryExists("files");
        this.channelService = channelService;
        this.userService = userService;
        this.messages = loadFromFile(); // 생성자에서 로드
    }
    //+++++++
    @Override
    public List<Message> getMessagesByChannel(UUID channelId) {
        List<Message> filteredMessages = new ArrayList<>();
        for (Message message : messages) {
            if (message.getChannelId().equals(channelId)) {
                filteredMessages.add(message);
            }
        }
        return filteredMessages;
    }

    @Override
    public Message createMessage(String username, UUID channelId, String content){
        Channel channel = channelService.getChannelById(channelId);

        //채널 확인
        if (channel == null) {
            System.out.println("Channel을 찾을 수 없습니다!");
            return null;
        }

        User user = findUserByUsername(username);
        if (user == null) {
            System.out.println("유저를 찾을 수 없습니다");
            return null;
        }

        if (!channel.isUserInChannel(user.getId())) {
            System.out.println("해당 유저는 채널에 존재하지 않습니다!");
            return null;
        }

        Message message = new Message(user.getUsername(), channelId, content);
        messages.add(message);
        saveToFile();
        return message;
    }

    @Override
    public Message getMessageById(UUID id) {
        for (Message message : messages) {
            if (message.getId().equals(id)) {
                return message;
            }
        }
        return null;
    }

    @Override
    public List<Message> getAllMessages() {
        return new ArrayList<>(messages);
    }

    private User findUserByUsername(String username){
        for (User user : userService.getAllUsers()) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        System.out.println("유저를 찾을 수 없습니다: " + username);
        return null;
    }

    @Override
    public Message updateMessageContent(UUID id, String content) {
        Message message = getMessageById(id);
        if (message != null) {
            message.updateContent(content);
            saveToFile();
        }
        return message;
    }

    @Override
    public boolean deleteMessage(UUID id) {
        for (int i = 0; i < messages.size(); i++) {
            if (messages.get(i).getId().equals(id)) {
                messages.remove(i);
                saveToFile();
                return true;
            }
        }
        return false;
    }

    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(messages);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //++++ 테스트
    @SuppressWarnings("unchecked")
    private List<Message> loadFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            return (List<Message>) ois.readObject(); // Message 타입으로 캐스팅
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private void ensureDirectoryExists(String directoryPath) {
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            if (directory.mkdirs()) {
                System.out.println("디렉토리가 생성되었습니다: " + directory.getAbsolutePath());
            } else {
                System.out.println("디렉토리 생성에 실패했습니다.");
            }
        }
    }

}
