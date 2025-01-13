package mission.service;


import mission.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    //등록
    Channel create(String channelName);

    Channel findByName(String name);

    //[ ] 조회(다건)
    List<Channel> findAll();

    //[ ] 수정
    Channel update(UUID id, String newName);

    //[ ] 삭제
    public void delete(String name);

    Channel findById(UUID id);
}