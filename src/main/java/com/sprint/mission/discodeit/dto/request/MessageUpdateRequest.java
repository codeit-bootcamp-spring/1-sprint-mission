package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class MessageUpdateRequest{
    @Size(max=1500, message = "1500자 이내로 작성해주세요.")
    String newContent;
    UUID requesterId;
}
