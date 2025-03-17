package com.sprint.mission.discodeit.dto.response;

import java.util.List;

public record CursorResponse<T>(List<T> content, Object nextCursor, int size, boolean hasNext) {

}
