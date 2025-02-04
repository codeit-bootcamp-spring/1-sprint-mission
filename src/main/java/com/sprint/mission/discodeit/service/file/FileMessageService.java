package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.service.MessageService;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class FileMessageService implements MessageService {
    //private final String FILE_NAME = "C:\\Users\\ypd06\\codit\\files\\message.ser";
    private static volatile FileMessageService instance;
    private FileChannelService fileChannelService;
    private FileMessageRepository fileMessageRepository;

    public FileMessageService() {
        this.fileMessageRepository = new FileMessageRepository();
    }

    public void setFileChannelService(FileChannelService fileChannelService) {
        this.fileChannelService = fileChannelService;
    }

    public static FileMessageService getInstance() {
        if (instance == null) {
            synchronized (FileUserService.class) {
                if (instance == null) {
                    instance = new FileMessageService();
                }
            }
        }
        return instance;
    }

    @Override
    public UUID createMessage(UUID sender, String content) { //단순 메시지 생성
        return fileMessageRepository.save(sender, content);
        /*Map<UUID, Message> userMap = loadFromSer(FILE_NAME);
        Message message = new Message(sender, content);
        userMap.put(message.getMessageId(), message);
        saveToSer(FILE_NAME, userMap);
        return message.getMessageId();*/
    }

    //public void initializeMessage(Message message){message = new Message();}

    @Override
    public Message getMessage(UUID id) {
        return fileMessageRepository.findMessageById(id);
        //return loadFromSer(FILE_NAME).get(id);
    }
    @Override
    public List<Message> getMessagesByUserId(UUID userId) {
        List<Message> collect = getMessages().stream().filter(s -> s.getSenderId().equals(userId)).collect(Collectors.toList());
        //return fileMessageRepository.findMessagesById(userId);
        System.out.println("collect = " + collect.size());
        return collect;
    }


    @Override
    public List<Message> getMessages() {
        //List<Message> collect = loadFromSer(FILE_NAME).values().stream().toList();
        List<Message> collect = fileMessageRepository.findAll();
        return new ArrayList<>(collect);
    }

    @Override
    public void updateMessage(UUID id, String content) {
        fileMessageRepository.update(id, content);
        /*Map<UUID, Message> messageMap = loadFromSer(FILE_NAME);
        if(messageMap.containsKey(id)){
            Message message = getMessage(id);
            message.update(content);
            messageMap.replace(id, message);
            saveToSer(FILE_NAME, messageMap);
        }else {
            System.out.println("메시지를 찾을 수 없습니다.");
        }*/
        //리턴해서 출력하는 방안 고려
    }

    @Override
    public void deleteMessage(UUID id) {
        List<Message> messages = fileMessageRepository.findAll();
        //Map<UUID, Message> messageMap = loadFromSer(FILE_NAME);
        if(messages.stream().map(Message::getId).toList().contains(id)) {
            //Message message = getMessage(id);
            fileMessageRepository.delete(id);
            fileChannelService.deleteMessage_in_Channel(id);
            //initializeMessage(message);
            //messageMap.remove(id);

            //saveToSer(FILE_NAME, messageMap);
        }else {
            System.out.println("유저를 찾을 수 없습니다.");
        }
    }

    private static void saveToSer(String fileName, Map<UUID, Message> messageData) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(messageData); // 직렬화하여 파일에 저장
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Map<UUID, Message> loadFromSer(String fileName) {
        Map<UUID, Message> map = new HashMap<>();
        File file = new File(fileName);

        if (!file.exists() || file.length() == 0) {
            // 파일이 없거나 크기가 0이면 빈 Map 반환
            return map;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            map = (Map<UUID, Message>) ois.readObject(); // 역직렬화
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return map;
    }

}
