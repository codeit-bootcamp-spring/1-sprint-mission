package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.NotificatonDto;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

  @GetMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  List<NotificatonDto> getNotification(UUID user){
    return null;

  }

  @DeleteMapping("/{notificationId}")
  public void deleteNotification(@PathVariable UUID notificationId){

  }


}
