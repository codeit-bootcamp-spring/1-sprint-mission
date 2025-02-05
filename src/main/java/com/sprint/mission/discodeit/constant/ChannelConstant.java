package com.sprint.mission.discodeit.constant;

public class ChannelConstant {
  public static final int CHANNEL_NAME_MIN_LENGTH = 2;
  public static final int CHANNEL_NAME_MAX_LENGTH = 100;
  public static final String BASIC_TAG = "General";
  public static final String VOICE = "voice";
  public static final String CHAT = "chat";
  public static final String CHANNEL_NAME_LENGTH_NOT_VALID = "채널명은 " + CHANNEL_NAME_MIN_LENGTH + " 이상, " + CHANNEL_NAME_MAX_LENGTH + " 이하여야 합니다.";
  public static final String CHANNEL_VALIDATION_EXCEPTION_MESSAGE_BASIC = "채널 생성에 실패했습니다.";
  public static final String NOT_CHAT_CHANNEL = "채팅 채널이 아닙니다.";
  public static final String CHANNEL_NOT_FOUND = "채널을 찾지 못했습니다.";
  public static final String CHANNEL_NOT_AVAILABLE_FOR_MESSAGE = "음성 채널은 메시지를 보낼 수 없습니다.";
  public static final String PRIVATE_CHANNEL_CANNOT_BE_UPDATED = "private 채널은 수정할 수 없습니다.";
}
