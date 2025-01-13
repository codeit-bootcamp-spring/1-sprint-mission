package mission.service.jcf;


import mission.entity.Channel;
import mission.service.ChannelService;


import java.util.*;

public class JCFChannelService implements ChannelService {

    private final Map<String, Channel> data = new HashMap();
    //private final Set<String> channelNames = new HashSet<>();

    @Override
    public Channel create(String channelName) {
        if (data.containsKey(channelName)){
            throw new IllegalArgumentException("이미 그런 채널명 있습니다.");
        }
        Channel newChannel = new Channel(channelName);
        data.put(channelName, newChannel);
        return newChannel;
    }

    @Override
    public Channel findByName(String channelName) {
        if (!data.containsKey(channelName)){
            throw new IllegalArgumentException("그런 채널명 없습니다");
        }
        return data.get(channelName);
    }

    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(data.values());
    }


    @Override
    public Channel update(String oldName, String newName){
        if (!data.containsKey(oldName)){
            throw new IllegalArgumentException("그런 채널명 없습니다");
        }

        if (data.containsKey(newName)){
            throw new IllegalArgumentException("채널명 중복");
        }

        Channel existingChannel = data.get(oldName);
        existingChannel.setName(newName);
        data.put(newName, existingChannel);
        data.remove(oldName); // 기존 key 삭제
        // 업데이트된 Channel 반환
        return existingChannel;
    }

    @Override
    public void delete(String name) {
        if (!data.containsKey(name)){
            throw new NoSuchElementException("그런 이름의 채널없습니다.");
        }
        data.remove(name);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        JCFChannelService that = (JCFChannelService) o;
        return Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(data);
    }



}

