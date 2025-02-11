package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.Instant;
import java.util.*;

@Service("fileReadStatusService")
public class FileReadStatusService implements ReadStatusService {

    private final String filePath = "read_statuses.dat";
    private Map<UUID, ReadStatus> data = new HashMap<>();

    @PostConstruct
    public void init() {
        loadFromFile();
    }

    @Override
    public void markAsRead(UUID userId, UUID channelId, Instant timestamp) {
        ReadStatus status = new ReadStatus(userId, channelId, timestamp);
        data.put(UUID.randomUUID(), status);
        saveToFile();
    }

    @Override
    public Optional<ReadStatus> getLastRead(UUID userId, UUID channelId) {
        return data.values().stream()
                .filter(status -> status.getUserId().equals(userId) && status.getChannelId().equals(channelId))
                .findFirst();
    }

    @Override
    public List<ReadStatus> getUserReadStatuses(UUID userId) {
        return new ArrayList<>(data.values());
    }

    @Override
    public List<ReadStatus> getChannelReadStatuses(UUID channelId) {
        List<ReadStatus> channelStatuses = new ArrayList<>();
        for (ReadStatus status : data.values()) {
            if (status.getChannelId().equals(channelId)) {
                channelStatuses.add(status);
            }
        }
        return channelStatuses;
    }

    @Override
    public void deleteByUserId(UUID userId) {
        data.values().removeIf(status -> status.getUserId().equals(userId));
        saveToFile();
    }

    @Override
    public void deleteByChannelId(UUID channelId) {
        data.values().removeIf(status -> status.getChannelId().equals(channelId));
        saveToFile();
    }

    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadFromFile() {
        File file = new File(filePath);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                data = (Map<UUID, ReadStatus>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
