package com.sprint.mission.discodeit.repository.file;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.*;

@Repository("fileMessageRepository")
@Primary
public class FileMessageRepository implements MessageRepository {

    private static final String FILE_PATH = "messages.json";
    private final ObjectMapper objectMapper;

    public FileMessageRepository() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule()); // ✅ Java Time Module 추가
        ensureFileExists();
    }

    @Override
    public void save(Message message) {
        List<Message> messages = loadFromFile();
        messages.removeIf(m -> m.getId().equals(message.getId())); // 기존 메시지 삭제 후 추가
        messages.add(message);
        saveToFile(messages);
    }

    @Override
    public Optional<Message> findById(UUID id) {
        return loadFromFile().stream()
                .filter(message -> message.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Message> findAll() {
        return loadFromFile();
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        return loadFromFile().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .toList();
    }

    @Override
    public Optional<Message> findTopByChannelIdOrderByCreatedAtDesc(UUID channelId) {
        return loadFromFile().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .max(Comparator.comparing(Message::getCreatedAt));
    }

    @Override
    public void deleteById(UUID id) {
        List<Message> messages = loadFromFile();
        messages.removeIf(message -> message.getId().equals(id));
        saveToFile(messages);
    }

    /** ✅ JSON 파일 저장 */
    private void saveToFile(List<Message> messages) {
        try {
            objectMapper.writeValue(new File(FILE_PATH), messages);
        } catch (IOException e) {
            throw new RuntimeException("메시지 저장 실패: " + e.getMessage());
        }
    }

    /** ✅ JSON 파일에서 데이터 로드 */
    private List<Message> loadFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return new ArrayList<>();

        try {
            List<Message> messages = objectMapper.readValue(file, new TypeReference<>() {});
            // ✅ createdAt 필드가 null이 되지 않도록 기본값 설정
            messages.forEach(message -> {
                if (message.getCreatedAt() == null) {
                    message.setCreatedAt(Instant.now());
                }
            });
            return messages;
        } catch (IOException e) {
            throw new RuntimeException("메시지 로딩 실패: " + e.getMessage());
        }
    }

    /** ✅ JSON 파일 없으면 생성 */
    private void ensureFileExists() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            saveToFile(new ArrayList<>());
        }
    }
}
