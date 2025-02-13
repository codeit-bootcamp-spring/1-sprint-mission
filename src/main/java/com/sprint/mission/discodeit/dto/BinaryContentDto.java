package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.BelongType;
import lombok.Value;

import java.util.UUID;

@Value(staticConstructor = "of")
public class BinaryContentDto {
    String name;
    BelongType type;
    UUID belongTo;
    byte[] data;
}
