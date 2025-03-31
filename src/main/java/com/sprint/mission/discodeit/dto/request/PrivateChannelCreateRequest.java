package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrivateChannelCreateRequest {
    
    @NotEmpty(message = "참여자 목록은 필수입니다")
    private List<UUID> participantIds;
} 