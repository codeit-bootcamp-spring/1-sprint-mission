package com.sprint.mission.discodeit.dto.channel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ChannelCreatePrivateDTO {
    private List<UUID> ids;
}
