package mission.repository.jcf;

import mission.entity.Channel;
import mission.repository.ChannelRepository;

import java.util.*;

public class JCFChannelRepository implements ChannelRepository {

    private final Map<UUID, Channel> data = new HashMap<>();

    // 보통 채널을 만들면 바로 그 채널로 들어가지기 때문에 등록과 동시에 반환
    public Channel register(Channel channel) {
        return data.put(channel.getId(), channel);
    }

    @Override
    public Set<Channel> findAll() {
        return new HashSet<>(data.values());
    }

    @Override
    public Channel findById(UUID id) {
        try {
            return data.get(id);
        } catch (NullPointerException e) {
            throw new NullPointerException("채널 id가 잘못됐습니다.");
        }  // 필요한 이유: 다른 곳에서 findById 이용할 떄 ex. Message 만들 때 사전에 오류 터트리기
    }

    @Override
    public void deleteById(UUID id) {
        data.remove(id);
    }
}
