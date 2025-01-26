package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.data.ChannelData;

import java.util.Map;
import java.util.UUID;

public class JCFChannelRepository implements ChannelRepository {

    private final ChannelData channelData = ChannelData.getInstance();

    // 데이터 저장
    @Override
    public void save(Channel channel) {

       channelData.save(channel);
    }

    // 데이터 가져오기
    @Override
    public Map<UUID, Channel> load() {

        return channelData.load();
    }

    // 데이터 삭제
    @Override
    public void delete(UUID id) {

        channelData.delete(id);
    }
}