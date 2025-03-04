package com.spirnt.mission.discodeit.repository.file;

import com.spirnt.mission.discodeit.enity.Channel;
import com.spirnt.mission.discodeit.repository.ChannelRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileChannelRepository extends FileRepository implements ChannelRepository {

    public FileChannelRepository(@Value("${discodeit.repository.Channel}") String fileDirectory) {
        super(fileDirectory);
    }

    @Override
    public void save(Channel channel) {
        Path path = resolvePath(channel.getId());
        saveToFile(path, channel);
    }

    @Override
    public void delete(UUID channelId) {
        Path path = resolvePath(channelId);
        deleteFile(path);
    }

    @Override
    public Optional<Channel> findById(UUID channelId) {
        Path path = resolvePath(channelId);
        return loadFromFile(path);
    }

    public Map<UUID, Channel> findAll() {
        Map<UUID, Channel> channels = new HashMap<>();
        // 폴더 내 모든 .ser 파일을 찾음
        try (Stream<Path> paths = Files.walk(this.getDirectory())) {
            paths.filter(path -> path.toString().endsWith(".ser"))  // .ser 파일만 필터링
                    .forEach(path -> {
                        Optional<Channel> channelOptional = loadFromFile(path);
                        channelOptional.ifPresent(channel -> channels.put(channel.getId(), channel));
                    });
        } catch (IOException e) {
            System.out.println("파일을 읽는 중 오류가 발생했습니다: " + e.getMessage());
        }

        return channels;
    }

    @Override
    public boolean existsById(UUID userId) {
        Path path = resolvePath(userId);
        return Files.exists(path);
    }
}
