package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.CreatePrivateChannelRequestDto;
import com.sprint.mission.discodeit.dto.channel.CreatePublicChannelRequestDto;
import com.sprint.mission.discodeit.dto.channel.FindChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.UpdatePublicChannelRequestDto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface ChannelService {// 생성

    UUID createPublic(CreatePublicChannelRequestDto createPublicChannelRequestDto);
    UUID createPrivate(CreatePrivateChannelRequestDto createPrivateChannelRequestDto);

    // 읽기
    FindChannelResponseDto find(UUID id);

    // 모두 읽기
    List<FindChannelResponseDto> findAllByUserId(UUID userId);

    // 수정
    FindChannelResponseDto updateChannel(UpdatePublicChannelRequestDto updatePublicChannelRequestDto);

    // 멤버 수정
    // 멤버가 List에 있을 경우 멤버 삭제, 없을 경우 추가
    void addMember(UUID id, UUID memberId);
    void deleteMember(UUID id, UUID memberId);

    // 삭제
    void delete(UUID id);

    // 채널 존재 여부 확인
    void channelIsExist(UUID id);

    void updateLastMessageTime(UUID channelID, Instant lastMessageTime);
}
