package some_path._1sprintmission.discodeit.repository.jcf;

import some_path._1sprintmission.discodeit.entiry.Channel;

import java.util.*;

public class JCFChannelRepository {
    private final Map<UUID, Channel> data = new HashMap<>();

    // 채널 저장
    public void save(Channel channel) {
        data.put(channel.getId(), channel);
    }

    // ID로 채널 찾기
    public Optional<Channel> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    // 모든 채널 가져오기
    public List<Channel> findAll() {
        return new ArrayList<>(data.values());
    }

    // 채널 삭제
    public void delete(UUID id) {
        data.remove(id);
    }
}
