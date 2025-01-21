package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileChannelRepository implements ChannelRepository {

    private final HashMap<UUID, Channel> data = new HashMap<>();
    private static final Logger LOGGER = Logger.getLogger(FileChannelRepository.class.getName());
    private final Path directory = Paths.get(System.getProperty("user.dir"), "Data/channel_data");
    private final String fileName = "channel_data.ser";

    public FileChannelRepository(){
        init(directory);
        loadDataFromFile();
    }

    @Override
    public void save(Channel channel) {
        data.put(channel.getuuId(), channel);
        saveDataToFile();
    }

    @Override
    public Channel findById(UUID uuid) {
        return data.get(uuid);
    }

    @Override
    public HashMap<UUID, Channel> findAll() {
        return new HashMap<>(data);
    }

    @Override
    public void delete(UUID uuid) {
        data.remove(uuid);
        saveDataToFile();
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

    private void saveDataToFile(){
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
                Object readObject = ois.readObject();
                if (readObject instanceof Map) {
                    @SuppressWarnings("unchecked")
                    Map<UUID, Channel> loadedData = (Map<UUID, Channel>) readObject;
                    data.putAll(loadedData);
                } else {
                    throw new RuntimeException("잘못된 데이터 형식");
                }
            } catch (IOException | ClassNotFoundException e) {
                LOGGER.log(Level.SEVERE, "파일 로드 실패", e);
            }
        }
    }
}
