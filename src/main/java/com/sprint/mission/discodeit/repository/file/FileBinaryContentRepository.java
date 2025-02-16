package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
//@Primary
public class FileBinaryContentRepository implements BinaryContentRepository {
    private static final String FILE_PATH = "tmp/binary_content.ser";
    private final FileManager<BinaryContent> fileManager =  new FileManager<>(FILE_PATH);

    @Override
    public BinaryContent save(BinaryContent binaryContent) {
        Map<UUID, BinaryContent> savedBinaryContentMap = loadBinaryContentMapToFile();
        savedBinaryContentMap.put(binaryContent.getId(), binaryContent);
        saveBinaryContentMapToFile(savedBinaryContentMap);
        return binaryContent;
    }

    @Override
    public Optional<BinaryContent> findById(UUID id) {
        return Optional.ofNullable(loadBinaryContentMapToFile().get(id));
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> ids) {
        return fileManager.loadListToFile().stream()
                .filter(binaryContent -> ids.contains(binaryContent.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        Map<UUID, BinaryContent> savedBinaryContentMap = loadBinaryContentMapToFile();
        savedBinaryContentMap.remove(id);
        saveBinaryContentMapToFile(savedBinaryContentMap);
    }

    @Override
    public void deleteByUserId(UUID userId) {
        Map<UUID, BinaryContent> savedBinaryContentMap = loadBinaryContentMapToFile();
        for (UUID id : savedBinaryContentMap.keySet()) {
            if (savedBinaryContentMap.get(id).getUserId().equals(userId)) {
                savedBinaryContentMap.remove(id);
                break;
            }
        }
        saveBinaryContentMapToFile(savedBinaryContentMap);
    }

    @Override
    public void deleteAllByMessageId(UUID messageId) {
        Map<UUID, BinaryContent> savedBinaryContentMap = loadBinaryContentMapToFile();
        for (UUID id : savedBinaryContentMap.keySet()) {
            if (savedBinaryContentMap.get(id).getMessageId().equals(messageId)) {
                savedBinaryContentMap.remove(id);
                break;
            }
        }
        saveBinaryContentMapToFile(savedBinaryContentMap);
    }

    private void saveBinaryContentMapToFile(Map<UUID, BinaryContent> saveBinaryContentMap) {
        List<BinaryContent> saveBinaryContentList = saveBinaryContentMap.values().stream().collect(Collectors.toList());
        fileManager.saveListToFile(saveBinaryContentList);
    }

    private Map<UUID, BinaryContent> loadBinaryContentMapToFile() {
        List<BinaryContent> loadBinaryContentList = fileManager.loadListToFile();
        if (loadBinaryContentList.isEmpty()) {
            return new HashMap<>();
        }
        return loadBinaryContentList.stream()
                .collect(Collectors.toMap(BinaryContent::getId, Function.identity()));
    }
}
