package some_path._1sprintmission.discodeit.repository;

import some_path._1sprintmission.discodeit.entiry.Channel;

import java.util.List;

public interface ChannelRepository {
    void saveAll(List<Channel> channels);

    List<Channel> findAll();
}