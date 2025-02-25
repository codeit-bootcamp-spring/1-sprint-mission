package com.sprint.mission.discodeit.repository.file;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Repository("fileChannelRepository")
@Primary
public class FileChannelRepository implements ChannelRepository {

    private static final String FILE_PATH = "channels.json";
    private final ObjectMapper objectMapper;

    @Autowired
    public FileChannelRepository(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        ensureFileExists();
    }

    @Override
    public void save(Channel channel) {
        List<Channel> channels = loadFromFile();
        channels.removeIf(ch -> ch.getId().equals(channel.getId())); // 기존 채널 삭제 후 추가
        channels.add(channel);
        saveToFile(channels);
    }

    @Override
    public Optional<Channel> findById(UUID id) {
        return loadFromFile().stream()
                .filter(channel -> channel.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Channel> findAll() {
        return loadFromFile();
    }

    @Override
    public void deleteById(UUID id) {
        List<Channel> channels = loadFromFile();
        channels.removeIf(channel -> channel.getId().equals(id));
        saveToFile(channels);
    }

    @Override
    public List<Channel> findAllPrivateChannelsByUserId(UUID userId) {
        return loadFromFile().stream()
                .filter(channel -> !channel.isPublic() && channel.getMembers().contains(userId))
                .toList();
    }

    /** JSON 파일 저장 */
    private void saveToFile(List<Channel> channels) {
        try {
            objectMapper.writeValue(new File(FILE_PATH), channels);
        } catch (IOException e) {
            throw new RuntimeException("채널 저장 실패: " + e.getMessage());
        }
    }

    /** JSON 파일에서 데이터 로드 */
    private List<Channel> loadFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return new ArrayList<>();

        try {
            return objectMapper.readValue(file, new TypeReference<List<Channel>>() {});
        } catch (IOException e) {
            throw new RuntimeException("채널 로딩 실패: " + e.getMessage());
        }
    }

    /** JSON 파일 없으면 생성 */
    private void ensureFileExists() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            saveToFile(new ArrayList<>());
        }
    }
}
