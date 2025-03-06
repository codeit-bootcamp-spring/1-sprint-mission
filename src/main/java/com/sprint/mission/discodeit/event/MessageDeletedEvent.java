package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.entity.Message;

import java.util.UUID;

public record MessageDeletedEvent (Message message){
}
