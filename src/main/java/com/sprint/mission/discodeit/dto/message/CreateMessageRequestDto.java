package com.sprint.mission.discodeit.dto.message;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateMessageRequestDto {

    @NotNull
    private String content;
    @NotNull
    private UUID channelId;
    @NotNull
    private UUID authorId;
}
