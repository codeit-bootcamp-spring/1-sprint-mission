package com.sprint.mission.discodeit.dto.data;

public record CsrfTokenDto(
    String token,
    String headerName,
    String parameterName
) {

}
