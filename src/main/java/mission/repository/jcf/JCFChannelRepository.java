package mission.repository.jcf;

import mission.entity.Channel;
import mission.entity.User;
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
        Channel deletingChannel = findById(id);
        deletingChannel.removeAllUser(); // 채널이 데이터에서 사라지면 user도 사라진다면 이 코드는 필요 없음
        for (User user : deletingChannel.getUsersImmutable()) {
            user.removeChannel(deletingChannel);
        }
        // 그 채널에서 생겼던 메시지도 삭제해야될지, 그래도 기록으로 보관하니 삭제하지 말지
        //List<Message> messagesInChannel = findMessageInChannel(channelId);
        data.remove(id);
    }
}
