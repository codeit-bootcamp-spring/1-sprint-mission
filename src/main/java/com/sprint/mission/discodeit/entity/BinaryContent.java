package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.global.util.TimeUtil;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

@Getter
public class BinaryContent implements Serializable {

    private static final long serialVersionUID = 1L;
    private final UUID id;
    private final byte[] content;
    private final Instant createdAt;

    public static final BinaryContent EMPTY = new BinaryContent(new byte[0]);

    private BinaryContent(byte[] content) {
        this.id = UUID.randomUUID();
        this.content = content;
        this.createdAt = TimeUtil.getCurrentTime();
    }

    public static BinaryContent of(byte[] content) {
        return new BinaryContent(content);
    }

    @Override
    public String toString() {
        return "BinaryContent{" +
                "id=" + id +
                ", content=" + Arrays.toString(content) +
                ", createdAt=" + createdAt +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        BinaryContent that = (BinaryContent) object;
        return Objects.equals(id, that.id) && Arrays.equals(content, that.content)
                && Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id);
        result = 31 * result + Arrays.hashCode(content);
        return result;
    }
}
