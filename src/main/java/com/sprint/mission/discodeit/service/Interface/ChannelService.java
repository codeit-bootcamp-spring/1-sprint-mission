package com.sprint.mission.discodeit.service.Interface;

import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelService {
    //생성
    Channel createPublicChannel(PublicChannelCreateRequest request);
    Channel createPrivateChannel(PrivateChannelCreateRequest request);

    //읽기
    Optional<ChannelResponseDto> getChannelById(UUID id);

    //모두 읽기
    List<Channel> getAllChannels();
    List<ChannelResponseDto> findAllByUserId(UUID userid);


    //수정
    Channel updateChannel(ChannelUpdateRequest request);

    //삭제
    void deleteChannel(UUID id);
}
