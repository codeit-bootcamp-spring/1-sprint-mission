package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.UUID;

public class ChannelRequest {

  @Getter
  @NoArgsConstructor
  public static class CreatePublic {

    @NotBlank(message = "채널 이름은 필수입니다.")
    private String name;
    private String description;
  }

  @Getter
  @NoArgsConstructor
  public static class CreatePrivate {

    @NotNull(message = "참여자 목록은 비어 있을 수 없습니다.")
    @Size(min = 1, message = "적어도 한 명의 참여자가 필요합니다.")
    private List<UUID> participantIds;
  }

  @Getter
  @NoArgsConstructor
  public static class Update {

    private String name;
    private String description;
  }
}
