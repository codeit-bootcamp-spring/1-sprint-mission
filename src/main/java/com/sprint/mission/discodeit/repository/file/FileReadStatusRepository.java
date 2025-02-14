package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class FileReadStatusRepository implements ReadStatusRepository {
    private final String filePath = "read_status.dat";

    @Override
    public ReadStatus save(ReadStatus readStatus) {
        Map<String, ReadStatus> readStatusMap = readFromFile();
        String key = readStatus.getUserId() + "-" + readStatus.getChannelId();
        readStatusMap.put(key, readStatus);
        writeToFile(readStatusMap);
        return readStatus;
    }

    @Override
    public Optional<ReadStatus> findByUserIdAndChannelId(String userId, String channelId) {
        return Optional.ofNullable(readFromFile().get(userId + "-" + channelId));
    }

    @Override
    public void deleteByUserIdAndChannelId(String userId, String channelId) {
        Map<String, ReadStatus> readStatusMap = readFromFile();
        readStatusMap.remove(userId + "-" + channelId);
        writeToFile(readStatusMap);
    }

    private Map<String, ReadStatus> readFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return (Map<String, ReadStatus>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new HashMap<>();
        }
    }

    private void writeToFile(Map<String, ReadStatus> readStatusMap) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(readStatusMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<ReadStatus> findByUserId(String userId) {
        return Optional.empty();
    }
}