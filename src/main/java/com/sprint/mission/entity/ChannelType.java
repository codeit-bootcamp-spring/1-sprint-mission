package com.sprint.mission.entity;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "채널 타입")
public enum ChannelType {
  PRIVATE, PUBLIC;
}
