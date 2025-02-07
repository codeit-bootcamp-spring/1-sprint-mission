package some_path._1sprintmission.discodeit.service.file;


import some_path._1sprintmission.discodeit.entiry.Channel;
import some_path._1sprintmission.discodeit.repository.ChannelRepository;

import java.util.List;

public class FileChannelService {

    private final ChannelRepository channelRepository;

    public FileChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    public List<Channel> loadChannels() {
        return channelRepository.findAll();
    }

    public void saveChannels(List<Channel> channels) {
        channelRepository.saveAll(channels);
    }
}
