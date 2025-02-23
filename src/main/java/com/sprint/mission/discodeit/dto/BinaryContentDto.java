package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.BinaryContent;
import lombok.Value;

import java.util.UUID;

@Value(staticConstructor = "of")
public class BinaryContentDto {
    String name;
    BinaryContent.Type type;
    UUID belongTo;
    byte[] data;
}
