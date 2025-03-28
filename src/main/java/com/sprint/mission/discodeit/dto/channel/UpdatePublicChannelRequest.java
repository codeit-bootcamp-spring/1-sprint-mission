package com.sprint.mission.discodeit.dto.channel;

public record UpdatePublicChannelRequest(
    String newName,
    String newDescription
) {

}
