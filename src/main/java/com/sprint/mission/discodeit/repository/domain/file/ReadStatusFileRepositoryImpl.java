package com.sprint.mission.discodeit.repository.domain.file;

import com.sprint.mission.discodeit.domain.BinaryContent;
import com.sprint.mission.discodeit.domain.ReadStatus;
import com.sprint.mission.discodeit.dto.ReadStatusDto;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

//사용자가 언제 마지막으로 채널에 접속했는 지 기록
@Repository
@RequiredArgsConstructor
@Profile("File")
public class ReadStatusFileRepositoryImpl implements ReadStatusRepository {
    private final String FILE_NAME = "C:\\Users\\ypd06\\codit\\files\\ReadStatus.ser";

    @Override
    public void save(ReadStatusDto readStatusDto) {
        Map<UUID, ReadStatus> temp = loadFromSer(FILE_NAME);
        ReadStatus readStatus = new ReadStatus(readStatusDto.userId(), readStatusDto.channelId());
        temp.put(readStatus.getId(), readStatus);
        saveToSer(FILE_NAME, temp);
    }

    @Override
    public ReadStatus findById(UUID id) {
        Map<UUID, ReadStatus> readStatusMap = loadFromSer(FILE_NAME);
        return readStatusMap.get(id);
    }

    @Override
    public List<ReadStatus> findAll() {
        List<ReadStatus> collect = loadFromSer(FILE_NAME).values().stream().toList();
        return new ArrayList<>(collect);
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        List<ReadStatus> collect = loadFromSer(FILE_NAME).values().stream().toList();
        return collect.stream().filter(r -> r.getUserId().equals(userId)).collect(Collectors.toList());
    }

    @Override
    public List<ReadStatus> findAllByChannelId(UUID channelId) {
        List<ReadStatus> collect = loadFromSer(FILE_NAME).values().stream().toList();
        return collect.stream().filter(r -> r.getChannelId().equals(channelId)).collect(Collectors.toList());
    }

    @Override
    public void delete(UUID id) {
        Map<UUID, ReadStatus> readStatusMap = loadFromSer(FILE_NAME);
        readStatusMap.remove(id);
        saveToSer(FILE_NAME, readStatusMap);
    }

    @Override
    public void deleteByUserId(UUID userId) {
        List<ReadStatus> readStatuses = findAllByUserId(userId);
        for (ReadStatus readStatus : readStatuses) {
            delete(readStatus.getId());
        }
    }

    @Override
    public void deleteByChannelId(UUID id) {
        List<ReadStatus> readStatuses = findAllByChannelId(id);
        for (ReadStatus readStatus : readStatuses) {
            delete(readStatus.getId());
        }
    }

    @Override
    public void update(ReadStatusDto before, ReadStatusDto after) {
        Map<UUID, ReadStatus> readStatusMap = loadFromSer(FILE_NAME);

        ReadStatus readStatus = findById(before.id());
        System.out.println("before = " + readStatus);
        readStatus = readStatus.update(after);
        System.out.println("after = " + readStatus);
        readStatusMap.replace(readStatus.getId(), readStatus);

        saveToSer(FILE_NAME, readStatusMap);

    }

    private void saveToSer(String fileName, Map<UUID, ReadStatus> readStatusMap) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(readStatusMap); // 직렬화하여 파일에 저장
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<UUID, ReadStatus> loadFromSer(String fileName) {
        Map<UUID, ReadStatus> map = new HashMap<>();
        File file = new File(fileName);

        if (!file.exists() || file.length() == 0) {
            // 파일이 없거나 크기가 0이면 빈 Map 반환
            return map;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            map = (Map<UUID, ReadStatus>) ois.readObject(); // 역직렬화
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return map;
    }

}
