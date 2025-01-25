package mission.repository.jcf;

import mission.entity.Channel;
import mission.entity.User;
import mission.repository.ChannelRepository;

import java.util.*;

public class JCFChannelRepository implements ChannelRepository {

    private final Map<UUID, Channel> data = new HashMap<>();

    public Channel create(Channel channel) {
        return data.put(channel.getId(), channel);
    }

    @Override
    public Set<Channel> findAll() {
        return new HashSet<>(data.values());
    }

    @Override
    public Optional<Channel> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
//        } catch (NullPointerException e) {
//            throw new NullPointerException("채널 id가 잘못됐습니다.");
//        }  // 필요한 이유: 다른 곳에서 findById 이용할 때 ex. Message 만들 때 사전에 오류 터트리기
    }

    @Override
    public void deleteById(UUID id) {
        //Channel deletingChannel = findById(id).ifPresent();
        findById(id).ifPresent(Channel::removeAllUser);

        // 그 채널에서 생겼던 메시지도 삭제해야될지, 그래도 기록으로 보관하니 삭제하지 말지
        //List<Message> messagesInChannel = findMessageInChannel(channelId);
        data.remove(id);
    }
}
