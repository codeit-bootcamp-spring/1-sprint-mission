package com.sprint.mission.discodeit.event;


import javax.management.relation.Role;

public record UserRoleChangedEvent(
    String username,
    String previousRole,
    Role newRole) {

}