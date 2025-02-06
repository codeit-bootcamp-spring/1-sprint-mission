package com.sprint.mission.discodeit.dto.channelService;

import java.util.List;
import java.util.UUID;

public record PrivateChannelCreateRequest(
        String password, // PRIVATE 채널은 비밀번호가 필요함
        List<UUID> userIds // 채널에 참여할 유저 ID 목록
) {
}
