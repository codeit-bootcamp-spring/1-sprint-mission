package com.sprint.mission.discodeit.exception.channel;

import static com.sprint.mission.discodeit.exception.ErrorCode.*;

import java.time.Instant;
import java.util.Map;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class ChannelNotFoundException extends ChannelException {

	public ChannelNotFoundException(String message, Instant timestamp, Map<String, Object> details) {
		super(message, CHANNEL_NOT_FOUND, details);
	}
}