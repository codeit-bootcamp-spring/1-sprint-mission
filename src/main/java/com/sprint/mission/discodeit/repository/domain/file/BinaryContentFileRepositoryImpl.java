package com.sprint.mission.discodeit.repository.domain.file;

import com.sprint.mission.discodeit.domain.BinaryContent;
import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@Profile("File")
public class BinaryContentFileRepositoryImpl implements BinaryContentRepository {
    private final String FILE_NAME = "C:\\Users\\ypd06\\codit\\files\\BinaryContent.ser";


    @Override
    public BinaryContent save(BinaryContentDto binaryContentDto) {
        Map<UUID, BinaryContent> temp = loadFromSer(FILE_NAME);
        BinaryContent binaryContent = new BinaryContent(binaryContentDto.domainId(), binaryContentDto.file());
        temp.put(binaryContent.getId(), binaryContent);
        saveToSer(FILE_NAME, temp);

        return binaryContent;
    }

    @Override
    public BinaryContent findById(UUID id) {
        Map<UUID, BinaryContent> binaryContentMap = loadFromSer(FILE_NAME);
        return binaryContentMap.get(id);
    }

    @Override
    public List<BinaryContent> findByDomainId(UUID domainId) {
        Map<UUID, BinaryContent> binaryContentMap = loadFromSer(FILE_NAME);
        return binaryContentMap.values().stream().filter(s -> s.getDomainId().equals(domainId)).collect(Collectors.toList());
    }

    @Override
    public List<BinaryContent> findAll() {
        List<BinaryContent> collect = loadFromSer(FILE_NAME).values().stream().toList();
        return new ArrayList<>(collect);
    }

    @Override
    public void delete(UUID id) {
        Map<UUID, BinaryContent> binaryContentMap = loadFromSer(FILE_NAME);
        binaryContentMap.remove(id);
        saveToSer(FILE_NAME, binaryContentMap);
    }

    @Override
    public void deleteByDomainId(UUID domainId) {
        Map<UUID, BinaryContent> binaryContentMap = loadFromSer(FILE_NAME);
        List<BinaryContent> list = binaryContentMap.values().stream().filter(s -> s.getDomainId().equals(domainId)).toList();
        for (BinaryContent binaryContent : list) {
            binaryContentMap.remove(binaryContent.getId());
        }
        saveToSer(FILE_NAME, binaryContentMap);

    }

    @Override
    public void update(UUID id, BinaryContentDto binaryContentDto) {
        Map<UUID, BinaryContent> binaryContentMap = loadFromSer(FILE_NAME);
        BinaryContent existBinaryContent = binaryContentMap.get(id);
        if (existBinaryContent.getFile() != null) {
            existBinaryContent = existBinaryContent.update(binaryContentDto.file());
        }
        binaryContentMap.replace(id, existBinaryContent);
        saveToSer(FILE_NAME, binaryContentMap);

    }

    private void saveToSer(String fileName, Map<UUID, BinaryContent> binaryContentMap) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(binaryContentMap); // 직렬화하여 파일에 저장
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<UUID, BinaryContent> loadFromSer(String fileName) {
        Map<UUID, BinaryContent> map = new HashMap<>();
        File file = new File(fileName);

        if (!file.exists() || file.length() == 0) {
            // 파일이 없거나 크기가 0이면 빈 Map 반환
            return map;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            map = (Map<UUID, BinaryContent>) ois.readObject(); // 역직렬화
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return map;
    }
}
