package com.sprint.mission.discodeit.controller;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

import com.sprint.mission.discodeit.dto.response.NotificationDto;
import com.sprint.mission.discodeit.dto.response.UserDto;
import com.sprint.mission.discodeit.security.jwt.JwtHeader;
import com.sprint.mission.discodeit.security.jwt.JwtUtils;
import com.sprint.mission.discodeit.service.notification.NotificationService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

  private final NotificationService notificationService;
  private final JwtUtils jwtUtils;

  /**
   * @methodName : findNotifications
   * @date : 2025-06-04 오후 6:34
   * @author : wongil
   * @Description: 유저의 모든 알림 가져오기
   **/
  @ResponseStatus(OK)
  @GetMapping
  public List<NotificationDto> findNotifications(
      @RequestHeader(JwtHeader.JWT_HEADER) String token) {

    UserDto userDto = jwtUtils.parseUserDto(token);

    return notificationService.findAll(userDto);
  }

  /**
   * @methodName : deleteNoti
   * @date : 2025-06-04 오후 6:35
   * @author : wongil
   * @Description: 알림 확인(삭제)
   **/
  @ResponseStatus(NO_CONTENT)
  @DeleteMapping("/{notificationId}")
  public void deleteNoti(@RequestHeader(JwtHeader.JWT_HEADER) String token,
      @PathVariable UUID notificationId) {

    UserDto userDto = jwtUtils.parseUserDto(token);
    notificationService.check(userDto, notificationId);

  }
}
