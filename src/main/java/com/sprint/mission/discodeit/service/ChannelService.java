package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ChannelCreateRequest;
import com.sprint.mission.discodeit.dto.ChannelResponse;
import com.sprint.mission.discodeit.dto.ChannelUpdateRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelService {

    /** ✅ 공개 채널 생성 */
    ChannelResponse createPublicChannel(ChannelCreateRequest channelCreateRequest);

    /** ✅ 비공개 채널 생성 */
    ChannelResponse createPrivateChannel(ChannelCreateRequest channelCreateRequest);

    /** ✅ 모든 채널 조회 */
    List<ChannelResponse> readAll();

    /** ✅ 특정 채널 조회 */
    Optional<ChannelResponse> read(UUID channelId);

    /** ✅ 채널 업데이트 */
    void update(UUID channelId, ChannelUpdateRequest channelUpdateRequest);

    /** ✅ 채널 삭제 */
    void delete(UUID channelId);

    /** ✅ 특정 사용자가 접근할 수 있는 채널 조회 */
    List<ChannelResponse> getChannelsForUser(UUID userId);
}
