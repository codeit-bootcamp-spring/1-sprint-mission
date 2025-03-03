package com.sprint.mission.discodeit.channel.dto.request;

import java.util.List;
import java.util.UUID;

//받는사람 보내는 사람을 정해주는게 좋지 않을까...?
public record PrivateChannelCreateRequest(
	List<UUID> participantIds
) {

}