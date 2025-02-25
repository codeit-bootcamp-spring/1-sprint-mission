package com.sprint.mission.discodeit.util.type;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum ChannelFormat {
    TEXT, VOICE
}