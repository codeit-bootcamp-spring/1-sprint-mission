//package com.sprint.mission.discodeit.service.file;
//
//import com.sprint.mission.discodeit.entity.Channel;
//import com.sprint.mission.discodeit.entity.Message;
//import com.sprint.mission.discodeit.entity.User;
//import com.sprint.mission.discodeit.service.ChannelService;
//import com.sprint.mission.discodeit.service.MessageService;
//import com.sprint.mission.discodeit.service.UserService;
//
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.ObjectOutputStream;
//import java.io.File;
//import java.util.List;
//import java.util.ArrayList;
//import java.util.Map;
//import java.util.HashMap;
//import java.util.UUID;
//import java.io.ObjectInputStream;
//import java.io.FileInputStream;
///*
//todo
//    1. 예외, 오류 처리 할 것
//    2. 마스터 임포트 해결
//    3. equals 재정의 필요?
//    4. 114줄? //이 차이가 무엇일까나???
// */
//
//
//public class FileMessageService implements MessageService {
//    private static final String FILE_PATH = "files/messages.ser";
//
//    private List<Message> messages;
//
//    private final ChannelService channelService;
//    private final UserService userService;
//
//
//    public FileMessageService(ChannelService channelService, UserService userService) {
//        File directory = new File("files");
//        if (!directory.exists()) {
//            directory.mkdir();
//        }
//        this.channelService = channelService;
//        this.userService = userService;
//        this.messages = loadFromFile();
//    }
//
//    @Override
//    public Message createMessage(UUID senderId, UUID channelId, String content) {
//        Channel channel = channelService.getChannelById(channelId);
//        if (channel == null) {
//            System.out.println("Channel을 찾을 수 없습니다!");
//            return null;
//        }
//
//        User user = userService.getUserById(senderId);
//        if (user == null) {
//            System.out.println("유저를 찾을 수 없습니다");
//            return null;
//        }
//
//        Message message = new Message(senderId, channelId, content);
//        messages.add(message);
//        saveToFile();
//        return message;
//    }
//
//
//    @Override
//    public Message getMessageById(UUID messageId) {
//        for (Message message : messages) {
//            if (message.getId().equals(messageId)) {
//                return message;
//            }
//        }
//        return null;
//    }
//
//    @Override
//    public List<Message> getAllMessages() {
//        return new ArrayList<>(messages);
//    }
//
//    @Override
//    public List<Message> getMessagesByChannel(UUID channelId) {
//        List<Message> channelMessages = new ArrayList<>();
//        for (Message message : messages) {
//            if (message.getChannelId().equals(channelId)) {
//                channelMessages.add(message);
//            }
//        }
//        return channelMessages;
//    }
//
//    @Override
//    public List<Message> getMessagesBySender(UUID senderId) {
//        List<Message> senderMessages = new ArrayList<>();
//        for (Message message : messages) {
//            if (message.getSenderId().equals(senderId)) {
//                senderMessages.add(message);
//            }
//        }
//        return senderMessages;
//    }
//
//    @Override
//    public Message updateMessageContent(UUID messageId, String newContent) {
//        Message message = getMessageById(messageId);
//        if (message != null) {
//            message.updateContent(newContent);
//            saveToFile();
//        }
//        return message;
//    }
//
//    @Override
//    public boolean deleteMessage(UUID messageId) {
//        Message message = getMessageById(messageId);
//        if (message != null) {
//            messages.remove(message);
//            saveToFile();
//            return true;
//        }
//        return false;
//    }
//
//    private void saveToFile() {
//        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
//            oos.writeObject(messages);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private List<Message> loadFromFile() {
//        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
//            return (List<Message>) ois.readObject();
//        } catch (Exception e) {
//            return new ArrayList<>();
//        }
//    }
//
//}
