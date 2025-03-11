package com.sprint.mission.discodeit.entity;

public enum ChannelType {
  PUBLIC, PRIVATE;

  public static ChannelType fromString(String str) {
    for (ChannelType type : ChannelType.values()) {
        if (type.name().equalsIgnoreCase(str)) {
            return type;
        }
    }
    throw new IllegalArgumentException("Invalid ChannelType: " + str);
  }
}
