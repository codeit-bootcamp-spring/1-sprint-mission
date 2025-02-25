package com.sprint.mission.discodeit.repository.file;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.interfacepac.ReadStatusRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public class FileReadStatusRepository implements ReadStatusRepository {

    private final String filePath;
    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private final Map<UUID, ReadStatus> readStatusData;

    public FileReadStatusRepository(@Value("${discodeit.repository.file-directory:.discodeit}") String fileDirectory) {
        if(!fileDirectory.endsWith("/")) {
            fileDirectory += "/";
        }
        this.filePath = fileDirectory + "read_status.json";
        ensureDirectoryExists(this.filePath);
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

    private void ensureDirectoryExists(String directoryPath) {
        File directory = new File(directoryPath);
        File parentDirectory = directory.getParentFile();
        if (!parentDirectory.exists()) {
            parentDirectory.mkdirs();
        }
    }

    private Map<UUID, ReadStatus> loadFromFile() {
        File file = new File(filePath);
        if (!file.exists()) {
            return new ConcurrentHashMap<>();
        }
        try {
            return objectMapper.readValue(file,
                    new TypeReference<Map<UUID, ReadStatus>>(){});
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return new ConcurrentHashMap<>();
        }
    }

    private void saveToFile(){
        File file = new File(filePath);
        try {
           objectMapper.writeValue(file, readStatusData);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save read status data to file.", e);
        }
    }



}
