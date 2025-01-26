package some_path._1sprintmission.discodeit.service.jcf;

import some_path._1sprintmission.discodeit.entiry.Channel;
import some_path._1sprintmission.discodeit.repository.jcf.JCFChannelRepository;
import some_path._1sprintmission.discodeit.service.ChannelService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class JCFChannelService implements ChannelService {
    private final JCFChannelRepository channelRepository;

    public JCFChannelService(JCFChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    @Override
    public void createChannel(Channel channel) {
        channelRepository.save(channel);
    }

    @Override
    public Optional<Channel> getChannel(UUID id) {
        return channelRepository.findById(id);
    }

    @Override
    public List<Channel> getChannelAll() {
        return channelRepository.findAll();
    }

    @Override
    public void updateChannel(UUID id, Channel updatedChannel) {
        Optional<Channel> existingChannel = channelRepository.findById(id);

        if (existingChannel.isPresent()) {
            channelRepository.save(updatedChannel);
        } else {
            throw new IllegalArgumentException("Channel does not exist.");
        }
    }

    @Override
    public void deleteChannel(UUID id) {
        Optional<Channel> existingChannel = channelRepository.findById(id);

        if (existingChannel.isPresent()) {
            channelRepository.delete(id);
        } else {
            throw new IllegalArgumentException("Channel does not exist.");
        }
    }
}
