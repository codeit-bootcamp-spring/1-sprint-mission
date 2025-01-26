package mission.service;


import mission.entity.Channel;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface ChannelService {

    Channel createOrUpdate(Channel channel) throws IOException;
    Set<Channel> findAll();
    Channel findById(UUID id);
    Channel update(Channel channel, String newName);
    void deleteById(UUID id) throws IOException;
    void validateDuplicateName(String name);
}