package com.sprint.mission.discodeit.dto.channel;

public record PublicChannelUpdateRequest(
    // 채널 이름 수정
    String newName,
    // 채널 설명 수정
    String newDescription
) {

}