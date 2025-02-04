package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ExceptionText;
import com.sprint.mission.discodeit.validation.MessageValidator;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileMessageService implements MessageService {

    private final MessageValidator validationService;
    private final Map<UUID, Map<UUID, Message>> data = new HashMap<>();
    private final Path directory = Paths.get(System.getProperty("user.dir"),"Data/message_data");
    private final String fileName = "message_data.ser";
    private static final Logger LOGGER = Logger.getLogger(FileMessageService.class.getName());

    public FileMessageService(MessageValidator messageValidator) {
        this.validationService = messageValidator;
        init(directory);
        loadDataFromFile();
    }

    @Override
    public Message createMsg(User user, Channel channel, String content) {
        if (validationService.validateMessage(user, channel, content)){
            Message msg = new Message(user, channel, content);
            data.putIfAbsent(msg.getDestinationCh().getChanneluuId(), new HashMap<>());
            data.get(msg.getDestinationCh().getChanneluuId()).put(msg.getUuId(), msg);
            saveDataToFile();
            return msg;
        }
        throw new CustomException(ExceptionText.MESSAGE_CREATION_FAILED);
    }

    @Override
    public Message getMessage(UUID msgUuid) {
        for (Map<UUID, Message> channelMessages : data.values()) {
            if (channelMessages.containsKey(msgUuid)) {
                return channelMessages.get(msgUuid);
            }
        }
        System.out.println("Message with ID " + msgUuid + " not found.");
        return null;
    }

    @Override
    public Map<UUID, Map<UUID, Message>> getAllMsg() {
        return  new HashMap<>(data);
    }

    @Override
    public void updateMsg(UUID msgUuid, String newContent) {
        Message msg = getMessage(msgUuid);
        msg.update(newContent);
        saveDataToFile();
        System.out.println("Message content has been updated --> ("+ newContent + ")");
    }

    @Override
    public void deleteMsg(UUID msgUuid) {
        for (Map<UUID, Message> channelMessages : data.values()) {
            if (channelMessages.containsKey(msgUuid)) {
                channelMessages.remove(msgUuid);
                saveDataToFile();
                System.out.println("Message " + msgUuid + " deleted.");
            }
        }
        System.out.println("Message not found: " + msgUuid);
    }

    @Override
    public void deleteAllMessagesForChannel(UUID channelUuid) {
        if (!data.containsKey(channelUuid)) {
            System.out.println("No messages found for channel ID");
        }
        data.remove(channelUuid);
        saveDataToFile();
        System.out.println("All messages for channel "+ "'" + channelUuid +"'" +" have been deleted.");
    }
    // 디렉토리 초기화
    private void init(Path directory) {
        if (!Files.exists(directory)) {
            try {
                Files.createDirectories(directory);
            } catch (IOException e) {
                throw new RuntimeException("디렉토리 생성 실패: " + e.getMessage());
            }
        }
    }

    //전체 HashMap 을 직렬화하여 파일에 저장
    private void saveDataToFile() {
        Path filePath = directory.resolve(fileName);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath.toFile()))) {
            oos.writeObject(data);
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패: " + e.getMessage());
        }
    }

    // 파일에서 직렬화된 객체를 불러오기
    private void loadDataFromFile() {
        Path filePath = directory.resolve(fileName);
        if (Files.exists(filePath)) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath.toFile()))) {
                // Unchecked cast 경고를 피하기 위해 Map<UUID, User>로 안전하게 캐스팅
                Object readObject = ois.readObject();
                if (readObject instanceof Map) {
                    @SuppressWarnings("unchecked")
                    Map<UUID, Map<UUID, Message>> loadedData = (Map<UUID, Map<UUID, Message>>) readObject;
                    data.putAll(loadedData);  // 직렬화된 Map<UUID, User>을 로드
                } else {
                    throw new RuntimeException("잘못된 데이터 형식");
                }
            } catch (IOException | ClassNotFoundException e) {
                LOGGER.log(Level.SEVERE, "파일 로드 실패", e);
            }
        }
    }

}