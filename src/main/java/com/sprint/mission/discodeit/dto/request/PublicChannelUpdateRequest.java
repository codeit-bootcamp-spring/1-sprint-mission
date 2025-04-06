package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PublicChannelUpdateRequest {
    @Pattern(regexp = "^[가-힣a-zA-Z0-9]{1,20}$", message = "채널 이름은 한글, 영문, 숫자 포함 1~20자 이내여야 합니다.")
    String newName;
    @Size(min=0, max=100, message = "100자 이내로 작성해주세요.")
    String newDescription;
}
