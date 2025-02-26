package com.sprint.mission.discodeit.repository.file;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Repository("fileChannelRepository")
@Primary
public class FileChannelRepository implements ChannelRepository {

    private static final String FILE_PATH = "channels.json";
    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    public FileChannelRepository() {
        ensureFileExists();
    }

    /** ✅ 채널 저장 (기존 데이터 제거 후 저장) */
    @Override
    public void save(Channel channel) {
        List<Channel> channels = loadFromFile();
        channels.removeIf(c -> c.getId().equals(channel.getId())); // 기존 데이터 제거
        channels.add(channel);
        saveToFile(channels);
    }

    /** ✅ ID로 채널 찾기 */
    @Override
    public Optional<Channel> findById(UUID channelId) {
        return loadFromFile().stream()
                .filter(channel -> channel.getId().equals(channelId))
                .findFirst();
    }

    /** ✅ 모든 채널 조회 */
    @Override
    public List<Channel> findAll() {
        return loadFromFile();
    }

    /** ✅ 특정 사용자의 비공개 채널 조회 */
    @Override
    public List<Channel> findAllPrivateChannelsByUserId(UUID userId) {
        return loadFromFile().stream()
                .filter(channel -> channel.isPrivate() && channel.getCreatorId().equals(userId))
                .toList();
    }

    /** ✅ ID로 채널 삭제 */
    @Override
    public void deleteById(UUID channelId) {
        List<Channel> channels = loadFromFile();
        channels.removeIf(channel -> channel.getId().equals(channelId));
        saveToFile(channels);
    }

    /** ✅ JSON 파일에 채널 리스트 저장 */
    private void saveToFile(List<Channel> channels) {
        try {
            objectMapper.writeValue(new File(FILE_PATH), channels);
        } catch (IOException e) {
            throw new RuntimeException("❌ 채널 데이터 저장 실패: " + e.getMessage());
        }
    }

    /** ✅ JSON 파일에서 채널 리스트 불러오기 */
    private List<Channel> loadFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists() || file.length() == 0) {
            System.out.println("⚠️ channels.json 파일이 존재하지 않거나 비어 있습니다. 초기화합니다.");
            saveToFile(new ArrayList<>()); // 빈 파일 생성
            return new ArrayList<>();
        }

        try {
            return objectMapper.readValue(file, new TypeReference<List<Channel>>() {});
        } catch (IOException e) {
            throw new RuntimeException("❌ 채널 데이터 로딩 실패: " + e.getMessage());
        }
    }

    /** ✅ JSON 파일이 없으면 자동 생성 */
    private void ensureFileExists() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            saveToFile(new ArrayList<>()); // 빈 리스트로 초기화
        }
    }
}
