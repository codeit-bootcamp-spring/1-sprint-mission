package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PublicChannelCreateRequest {
    
    @NotBlank(message = "채널 이름은 필수입니다")
    private String name;
    
    private String description;
} 