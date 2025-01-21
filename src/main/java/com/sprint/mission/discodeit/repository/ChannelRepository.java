package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import java.util.List;

public interface ChannelRepository {

    void saveAll(List<Channel> channels);

    List<Channel> loadAll();

    void reset();
}
