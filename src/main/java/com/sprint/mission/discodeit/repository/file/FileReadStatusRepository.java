package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Repository
public class FileReadStatusRepository implements ReadStatusRepository {

    private final Path DIRECTORY;
    private final Path FILE_PATH;
    private final Map<UUID, ReadStatus> data;

    public FileReadStatusRepository(@Value("${discodeit.repository.file.path}") String path ) {

        this.DIRECTORY = Paths.get(System.getProperty("user.dir"), path, "ReadStatus");
        this.FILE_PATH = DIRECTORY.resolve("readStatus.ser"); //두 경로 조합

        if (Files.notExists(DIRECTORY)) {
            try {
                Files.createDirectories(DIRECTORY);
            } catch (IOException e) {
                throw new RuntimeException("Failed to create directory: " + DIRECTORY, e);
            }
        }

        this.data = loadDataFromFile();
    }

    // 데이터 파일 읽기
    @SuppressWarnings("unchecked")
    private Map<UUID, ReadStatus> loadDataFromFile() {
        File file = FILE_PATH.toFile();

        if (!file.exists()) {
            return new HashMap<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Map<UUID, ReadStatus>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to load data"+e.getMessage());
        }
    }

    // 데이터 파일에 쓰기
    private void saveDataToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH.toFile()))) {
            oos.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 파일을 빈 파일로 만드는 메서드
    public void clearFile() {
        File file = FILE_PATH.toFile();
        try (FileOutputStream fos = new FileOutputStream(file)) {
            // 파일을 비우는 방법
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //파일 삭제
    public void deleteFile() {
        File file = FILE_PATH.toFile();
        if (file.exists()) {
            boolean deleted = file.delete();
            if (!deleted) {
                System.out.println("파일 삭제에 실패했습니다.");
            }
        }
    }


    @Override
    public UUID save(ReadStatus readStatus) {
        data.put(readStatus.getId(), readStatus);
        saveDataToFile();
        return readStatus.getId();
    }

    @Override
    public ReadStatus findOne(UUID id) {
        return data.get(id);
    }

    @Override
    public List<ReadStatus> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return data.values().stream()
                .filter(readStatus -> readStatus.getUserId().equals(userId))
                .toList();
    }

    @Override
    public List<ReadStatus> findAllByChannelId(UUID channelId) {
        return data.values().stream()
                .filter(readStatus -> readStatus.getChannelId().equals(channelId))
                .toList();
    }


    @Override
    public Optional<ReadStatus> findByUserIdAndChannlId(UUID userId, UUID channelId) {
        return data.values().stream()
                .filter(
                        readStatus -> readStatus.getUserId().equals(userId) &&
                                readStatus.getChannelId().equals(channelId))
                .findFirst();
    }

    @Override
    public UUID update(ReadStatus readStatus) {
        data.put(readStatus.getId(), readStatus);
        saveDataToFile();
        return readStatus.getId();
    }

    @Override
    public UUID delete(UUID id) {
        data.remove(id);
        saveDataToFile();
        return id;
    }

    @Override
    public void deleteByChannelId(UUID channelId){
        findAllByChannelId(channelId)
                .forEach(rS -> delete(rS.getId()));
        saveDataToFile();
    }
}
