package mission.service;


import mission.entity.Channel;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface ChannelService {
    //등록
    Channel createOrUpdate(Channel channel) throws IOException;

    Channel findByName(String name);

    //[ ] 조회(단, 다건)
    Set<Channel> findAll();
    Channel findById(UUID id);

    //[ ] 수정

    Channel update(Channel channel, String newName);
    //[ ] 삭제
    void deleteById(UUID id);

    //void delete(Channel channel);

    void validateDuplicateName(String name);
}