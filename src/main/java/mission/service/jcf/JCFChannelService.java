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
    public Channel create(Channel channel) {
        return channelRepository.register(channel);
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
    public Channel update(Channel channel) {
        return channelRepository.updateChannelName(channel);
    }

    @Override
    public void deleteById(UUID id) {
        channelRepository.delete(id);
    }

    public void validateDuplicateName(String name){
        channelRepository.validateDuplicateName(name);
    }
}

