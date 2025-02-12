package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString(callSuper = true)
@RequiredArgsConstructor
public class BinaryContent extends BaseEntity {
    private final UUID ownerId;
    private final byte[] data;
    private final String contentType;
}
