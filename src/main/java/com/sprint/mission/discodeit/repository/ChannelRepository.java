package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelRepository {
    //생성
    void createChannel(Channel channel);

    //읽기
    Optional<Channel> getChannelById(UUID id);

    //모두 읽기
    List<Channel> getAllChannels();

    //수정
    void updateChannel(UUID id,Channel updatedChannel);

    //삭제
    void deleteChannel(UUID id);
}
