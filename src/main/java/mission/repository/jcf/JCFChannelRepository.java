package mission.repository.jcf;

import mission.entity.Channel;
import mission.repository.ChannelRepository;

import java.util.*;

public class JCFChannelRepository implements ChannelRepository {

    private final Map<UUID, Channel> data = new HashMap<>();
    private final Set<String> channelNames = new HashSet<>();



    // 보통 채널을 만들면 바로 그 채널로 들어가지기 때문에 등록과 동시에 반환
    public Channel register(String channelName) {
        // 채널 이름 중복 검증 후 채널 생성
        try {
            validateDuplicateName(channelName);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("이미 그런 채널명 있습니다.");
        }

        Channel newChannel = new Channel(channelName);
        channelNames.add(newChannel.getName());
        data.put(newChannel.getId(), newChannel);
        return newChannel;
    }


    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Channel findById(UUID id) {
        return data.get(id);
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
    public Channel updateChannelName(Channel findChannel, String newName) {
        // 새로운 채널 이름 중복 검증
        validateDuplicateName(newName);

        channelNames.remove(findChannel.getName());
        channelNames.add(newName);
        findChannel.setName(newName);
        return findChannel;
    }

    private void validateDuplicateName(String name){
        if (channelNames.contains(name)){
            throw new IllegalArgumentException("이미 존재하는 id입니다");
        }
    }

    public void delete(UUID id) {
        channelNames.remove(data.get(id).getName());
        data.remove(id);
    }
}
