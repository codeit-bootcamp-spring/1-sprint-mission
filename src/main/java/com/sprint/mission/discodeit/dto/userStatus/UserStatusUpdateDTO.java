package com.sprint.mission.discodeit.dto.userStatus;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserStatusUpdateDTO {

  @NotNull(message = "마지막 활동 시간은 필수입니다.")
  @PastOrPresent(message = "마지막 활동 시간은 현재 시각보다 미래일 수 없습니다.")
  private Instant newLastActiveAt;
}
