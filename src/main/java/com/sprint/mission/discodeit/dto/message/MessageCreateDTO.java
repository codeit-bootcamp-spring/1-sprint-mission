package com.sprint.mission.discodeit.dto.message;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageCreateDTO {

  @NotBlank(message = "메시지 내용은 필수입니다.")
  private String content;

  @NotNull(message = "채널 ID는 필수입니다.")
  private UUID channelId;

  @NotNull(message = "작성자 ID는 필수입니다.")
  private UUID authorId;
}
