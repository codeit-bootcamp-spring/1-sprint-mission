package mission.service;


import mission.entity.Channel;

import java.util.List;

public interface ChannelService {
    //등록
    Channel create(String channelName);

    Channel findByName(String name);

    //[ ] 조회(다건)
    List<Channel> findAll();

    //[ ] 수정
    Channel update(String oldName, String newName);

    //[ ] 삭제
    public void delete(String name);
}