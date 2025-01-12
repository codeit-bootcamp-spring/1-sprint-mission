package com.sprint.mission.discodeit.dto;

import java.util.Optional;

public class MessageUpdateDto {

    private Optional<String> content = Optional.empty();
    private Optional<String> contentUrl = Optional.empty();

    public MessageUpdateDto(Optional<String> content, Optional<String> contentUrl) {
        this.content = content;
        this.contentUrl = contentUrl;
    }

    public Optional<String> getContent() {
        return content;
    }

    public Optional<String> getContentUrl() {
        return contentUrl;
    }
}
