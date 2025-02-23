package com.sprint.mission.discodeit.dto.request;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.UUID;

public record BinaryContentRequest(
        String name,
        BinaryContent.Type type,
        UUID belongTo,
        byte[] data
) {
    public static BinaryContentRequest of(String name, BinaryContent.Type type, UUID belongTo, byte[] data) {
        return new BinaryContentRequest(name, type, belongTo, data);
    }
}
