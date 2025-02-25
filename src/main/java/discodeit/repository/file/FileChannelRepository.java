package discodeit.repository.file;

import discodeit.entity.Channel;
import discodeit.repository.ChannelRepository;
import discodeit.utils.FileUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@ConditionalOnProperty(value = "repository.type", havingValue = "file")
@Repository
public class FileChannelRepository implements ChannelRepository {
    private Map<String, Channel> channelData;
    private final Path path;

    public FileChannelRepository(@Value("${repository.channel-file-path}") Path path) {
        this.path = path;
        if (!Files.exists(this.path)) {
            try {
                Files.createFile(this.path);
                FileUtil.save(this.path, new HashMap<>());
            } catch (IOException e) {
                throw new RuntimeException("[error] 채널 파일을 초기화 불가능", e);
            }
        }
        FileUtil.init(this.path);
        this.channelData = FileUtil.load(this.path, Channel.class);  // 변경된 메서드로 파일 데이터 로딩
    }

    @Override
    public Channel save(Channel channel) {
        channelData.put(channel.getId().toString(), channel);
        FileUtil.save(path, channelData);
        return channel;
    }

    @Override
    public Optional<Channel> findById(UUID channelId) {
        if (!channelData.containsKey(channelId.toString())) {
            throw new IllegalArgumentException("[error] 존재하지 않는 channel ID입니다.");
        }
        return Optional.ofNullable(channelData.get(channelId.toString()));
    }

    @Override
    public List<Channel> findAll() {
        return channelData.values().stream().toList();
    }

    @Override
    public List<Channel> findAllByUserId(UUID userId) {
        return channelData.values().stream()
                .filter(channel -> channel.getParticipantIds().contains(userId)).toList();
    }

    @Override
    public boolean existsById(UUID channelId) {
        return channelData.containsKey(channelId.toString());
    }

    @Override
    public boolean existsByName(String channelName) {
        return channelData.values().stream().anyMatch(channel -> channel.getChannelName().equals(channelName));
    }

    @Override
    public void deleteById(UUID channelId) {
        channelData.remove(channelId.toString());
        FileUtil.save(path, channelData);
    }
}
