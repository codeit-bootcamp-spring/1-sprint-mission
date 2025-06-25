package com.sprint.mission.discodeit.config;

public enum CacheNames {
  users,   // 유저 개개인의 정보를 담을 캐시
  userChannels,         // 유저별 참여 채널 목록
  userNotifications,    // 유저별 알림 목록
  userReadStatuses,     // 유저별 읽음 상태 목록
  channelParticipants;  // 채널별 참여자 목록

  public static final String USERS = "users";
  public static final String USER_CHANNELS = "userChannels";
  public static final String USER_NOTIFICATIONS = "userNotifications";
  public static final String USER_READ_STATUSES = "userReadStatuses";
  public static final String CHANNEL_PARTICIPANTS = "channelParticipants";
}