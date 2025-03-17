package com.sprint.mission.discodeit.dto.response;

import java.util.List;

public record Cursor<T>(
    List<T> content,
    Object nextCursor,
    boolean hasNext
) {}