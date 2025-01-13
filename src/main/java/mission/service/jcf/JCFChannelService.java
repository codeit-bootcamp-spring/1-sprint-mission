package mission.service.jcf;


import mission.entity.Channel;
import mission.repository.jcf.JCFChannelRepository;
import mission.service.ChannelService;


import java.util.*;

public class JCFChannelService implements ChannelService {

    //private final Map<String, Channel> data = new HashMap();
    //private final Set<String> channelNames = new HashSet<>();

    private final JCFChannelRepository channelRepository = new JCFChannelRepository();

    @Override
    public Channel create(String channelName) {
        return channelRepository.register(channelName);
    }

    @Override
    public Channel findByName(String channelName) {
        return channelRepository.findByName(channelName);
    }

    @Override
    public List<Channel> findAll() {
        return channelRepository.findAll();
    }

    @Override
    public Channel findById(UUID id) {
        return channelRepository.findById(id);
    }

    @Override
    public Channel update(UUID id, String newName) {
        Channel findChannel = channelRepository.findById(id);
        return channelRepository.updateChannelName(findChannel, newName);
    }

    @Override
    public void delete(String name) {
        if (!data.containsKey(name)) {
            throw new NoSuchElementException("그런 이름의 채널없습니다.");
        }
        data.remove(name);
    }

}

