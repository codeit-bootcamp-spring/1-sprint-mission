package com.sprint.mission.discodeit.service;


import java.util.UUID;

public interface AuthService {

  void forceLogout(UUID userId);

  void printSessions();

}
