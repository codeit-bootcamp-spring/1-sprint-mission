package com.sprint.mission.discodeit.repository.file;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.interfacepac.ReadStatusRepository;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class FileReadStatusRepository implements ReadStatusRepository {
    private static final String FILE_PATH = "tmp/read_status.ser";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final Map<UUID, ReadStatus> readStatusData;

    public FileReadStatusRepository() {
        this.readStatusData = loadFromFile();
    }

    @Override
    public void save(ReadStatus readStatus) {
        readStatusData.put(readStatus.getId(), readStatus);
        saveToFile();
    }

    @Override
    public List<ReadStatus> findAllByChannel(Channel channel) {
        return readStatusData.values().stream()
                .filter(readStatus -> channel.equals(readStatus.getChannel()))
                .toList();
    }

    @Override
    public List<ReadStatus> findAllByUser(User user) {
        return readStatusData.values().stream()
                .filter(readStatus -> user.equals(readStatus.getUser()))
                .toList();
    }

    @Override
    public List<Channel> findChannelsByUser(User owner) {
        return readStatusData.values().stream()
                .filter(readStatus -> owner.equals(readStatus.getUser()))
                .map(ReadStatus::getChannel)
                .distinct()
                .toList();
    }

    @Override
    public List<UUID> findUserIdsByChannel(Channel channel) {
        return readStatusData.values().stream()
                .filter(readStatus -> channel.equals(readStatus.getChannel()))
                .map(readStatus -> readStatus.getUser().getId())
                .distinct()
                .toList();
    }

    @Override
    public Optional<ReadStatus> findById(UUID id) {
        return Optional.ofNullable(readStatusData.get(id));
    }

    @Override
    public Optional<ReadStatus> findByUserAndChannel(User user, Channel channel) {
        return readStatusData.values().stream()
                .filter(readStatus -> user.equals(readStatus.getUser()) && channel.equals(readStatus.getChannel()))
                .findFirst();
    }

    @Override
    public boolean existsByUserAndChannel(User user, Channel channel) {
        return readStatusData.values().stream()
                .anyMatch(readStatus -> user.equals(readStatus.getUser()) && channel.equals(readStatus.getChannel()));
    }

    @Override
    public void deleteByChannel(Channel channel) {
        readStatusData.values()
                .removeIf(readStatus -> channel.equals(readStatus.getChannel()));
        saveToFile();
    }

    @Override
    public void deleteById(UUID id) {
        readStatusData.remove(id);
        saveToFile();
    }



    private Map<UUID, ReadStatus> loadFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new ConcurrentHashMap<>();
        }

        try {
            return objectMapper.readValue(file, new TypeReference<Map<UUID, ReadStatus>>(){});
        }catch (IOException e){
            System.out.println(e.getMessage());
            return new ConcurrentHashMap<>();
        }
    }

    private void saveToFile(){
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(readStatusData);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save read status data to file.", e);
        }
    }




}
