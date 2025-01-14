package mission.repository.jcf;

import mission.entity.Channel;
import mission.repository.ChannelRepository;
import mission.service.exception.DuplicateName;

import java.util.*;

public class JCFChannelRepository implements ChannelRepository {

    private final Map<UUID, Channel> data = new HashMap<>();
    private final Set<String> channelNames = new HashSet<>();



    // 보통 채널을 만들면 바로 그 채널로 들어가지기 때문에 등록과 동시에 반환
    public Channel register(Channel channel) {
        // 채널 이름 중복 검증 후 채널 생성
        channelNames.add(channel.getName());
        data.put(channel.getId(), channel);
        return channel;
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
            throw new NullPointerException("그런 채널은 존재하지 않습니다.");
        } // 필요한 이유 Message 만들 때
    }

    @Override
    public Channel findByName(String channelName) {
        if (!channelNames.contains(channelName)){
            throw new NoSuchElementException("그런 채널명 없습니다.");
        }

        for (Channel channel : data.values()) {
            if (channel.getName().equals(channelName)){
                return channel;
            }
        }
        return null;
    }

    @Override
    public Channel updateChannelName(Channel updatingChannel) {
        // 새로운 채널 이름 중복 검증
        validateDuplicateName(updatingChannel.getName());
        channelNames.remove(updatingChannel.getOldName());
        channelNames.add(updatingChannel.getName());
        return updatingChannel;
    }

    public void validateDuplicateName(String name){
        if (channelNames.contains(name)){
            throw new DuplicateName(
                    String.format("%s는 이미 존재하는 이름의 채널명입니다", name));
        }
    }

    public void delete(UUID id) {
        channelNames.remove(data.get(id).getName());
        data.remove(id);
    }
}
