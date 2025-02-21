package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.*;

@Repository
public class FileBinaryContentRepository implements BinaryContentRepository {
    private final String filePath = "binary_content.dat";

    @Override
    public BinaryContent save(BinaryContent content) {
        Map<String, BinaryContent> contentMap = readFromFile();
        contentMap.put(content.getId(), content);
        writeToFile(contentMap);
        return content;
    }

    @Override
    public void deleteById(String id) {
        Map<String, BinaryContent> contentMap = readFromFile();
        contentMap.remove(id);
        writeToFile(contentMap);
    }

    @Override
    public Optional<BinaryContent> findById(String id) {
        Map<String, BinaryContent> contentMap = readFromFile();
        return Optional.ofNullable(contentMap.get(id));
    }

    @Override
    public List<BinaryContent> findAll() {
        return new ArrayList<>(readFromFile().values());
    }

    private Map<String, BinaryContent> readFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return (Map<String, BinaryContent>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new HashMap<>();
        }
    }

    private void writeToFile(Map<String, BinaryContent> contentMap) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(contentMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}