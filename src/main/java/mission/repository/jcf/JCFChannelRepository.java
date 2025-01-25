package mission.repository.jcf;

import mission.entity.Channel;
import mission.entity.User;
import mission.repository.ChannelRepository;
import mission.service.exception.NotFoundId;

import java.util.*;

public class JCFChannelRepository implements ChannelRepository {

    private final Map<UUID, Channel> data = new HashMap<>();

    @Override
    public Channel create(Channel channel) {
        // optional no
        return data.put(channel.getId(), channel);
    }

    @Override
    public Channel findById(UUID id) {
        // null 예외처리 여기서 확실히 잡기 : 다른 메서드가 findById 활용을 많이 함
        Channel findChannel = data.get(id);
        if (findChannel == null) throw new NotFoundId();
        else return findChannel;
    }

    @Override
    public Set<Channel> findAll() {
        return new HashSet<>(data.values());
    }

    @Override
    public void deleteById(UUID id) {
        Channel deletingChannel = findById(id);
        deletingChannel.removeAllUser();
        System.out.printf("채널명 %s는 사라집니다.", deletingChannel.getName());

        // 그 채널에서 생겼던 메시지도 삭제해야될지, 그래도 기록으로 보관하니 삭제하지 말지
        //List<Message> messagesInChannel = findMessageInChannel(channelId);
        data.remove(deletingChannel.getId());
    }
}
