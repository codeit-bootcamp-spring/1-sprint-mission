package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;

import java.io.*;
import java.util.*;

public class FileReadStatusRepository implements ReadStatusRepository {

    private static final String FILE_PATH = "ReadStatusData.ser";
    private final Map<UUID, ReadStatus> data;

    public FileReadStatusRepository() {
        data = new HashMap<>();
    }


    // 데이터 파일 읽기
    @SuppressWarnings("unchecked")
    private Map<UUID, User> loadDataFromFile() {
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            return new HashMap<>();
        }
        try (FileInputStream fis = new FileInputStream(file);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (Map<UUID, User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    // 데이터 파일에 쓰기
    private void saveDataToFile() {
        try (FileOutputStream fos = new FileOutputStream(FILE_PATH);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 파일을 빈 파일로 만드는 메서드
    public void clearFile() {
        File file = new File(FILE_PATH);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            // 파일을 비우는 방법
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //파일 삭제
    public void deleteFile() {
        File file = new File(FILE_PATH);
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

}
