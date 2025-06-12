package com.sprint.mission.discodeit.event;


import com.sprint.mission.discodeit.entity.Role;

public record UserRoleChangedEvent(
    String username,
    Role previousRole,
    Role newRole) {

}