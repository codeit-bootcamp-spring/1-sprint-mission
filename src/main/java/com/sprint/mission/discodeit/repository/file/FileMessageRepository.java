package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.file.FileMessageService;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Repository
public class FileMessageRepository implements MessageRepository {
    //< 채널 UUID < 메시지 UUID, 매시지 객체 >>
    private final Map<UUID, Map<UUID, Message>> data = new HashMap<>();
    private final Path directory = Paths.get(System.getProperty("user.dir"),"Data/message_data");
    private final String fileName = "message_data.ser";
    private static final Logger LOGGER = Logger.getLogger(FileMessageService.class.getName());

    public FileMessageRepository(){
        init(directory);
        loadDataFromFile();
    }

    public void save(Message message) {
        data.putIfAbsent(message.getDestinationChannel().getuuId(), new HashMap<>());
        data.get(message.getDestinationChannel().getuuId()).put(message.getMsguuId(), message);
        saveDataToFile();
    }

    @Override
    public Message findById(UUID uuid) {
        for (Map<UUID, Message> channelMessages : data.values()) {
            if (channelMessages.containsKey(uuid)) {
                return channelMessages.get(uuid);
            }
        }
        System.out.println("Message with ID " + uuid + " not found.");
        return null;
    }

    @Override
    public Map<UUID, Map<UUID, Message>> findAll() {
        return new HashMap<>(data);
    }

    @Override
    public void delete(UUID msgUuid) {
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
    public void deleteAllMessagesForChannel(UUID channelUuid){
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
