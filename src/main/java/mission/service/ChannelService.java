package mission.service;


import mission.entity.Channel;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface ChannelService {
    //등록
    Channel create(Channel channel);

    Channel findByName(String name);

    //[ ] 조회(다건)
    Set<Channel> findAll();

    //[ ] 수정
    Channel update(Channel channel);

    //[ ] 삭제
    void deleteById(UUID id);
    Channel findById(UUID id);
}