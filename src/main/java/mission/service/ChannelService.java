package mission.service;


import mission.entity.Channel;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface ChannelService {
    //등록
    Channel createOrUpdate(Channel channel);

    Channel findByName(String name);

    //[ ] 조회(단, 다건)
    Set<Channel> findAll();
    Channel findById(UUID id);

    //[ ] 수정

    Channel update(Channel channel);
    //[ ] 삭제
    void deleteById(UUID id);

    //void delete(Channel channel);

    void validateDuplicateName(String name);
}