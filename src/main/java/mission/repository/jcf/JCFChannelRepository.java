package mission.repository.jcf;

import mission.entity.Channel;
import mission.repository.ChannelRepository;

import java.util.*;

public class JCFChannelRepository implements ChannelRepository {

    private final Map<UUID, Channel> data = new HashMap<>();
    private final Set<String> channelName = new HashSet<>();

    public void validateDuplicateName(String name){
        if (channelName.contains(name)){
            throw new IllegalArgumentException("이미 존재하는 id입니다");
        }
    }

    // 보통 채널을 만들면 바로 그 채널로 들어가지기 때문에 등록과 동시에 반환
    public Channel register(Channel channel) {
        channelName.add(channel.getName());
        data.put(channel.getId(), channel);
        return channel;
    }


}
