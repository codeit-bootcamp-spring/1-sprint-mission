package com.sprint.mission.discodeit.exception.channel;

import static com.sprint.mission.discodeit.exception.ErrorCode.*;

import java.time.Instant;
import java.util.Map;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class PrivateChannelUpdateException extends ChannelException {

	public PrivateChannelUpdateException(String message, Instant timestamp, Map<String, Object> details) {
		super(message, PRIVATE_CHANNEL_UPDATE, details);
	}
}