package com.sprint.mission.repository;

import com.sprint.mission.entity.main.Channel;
import com.sprint.mission.entity.main.User;

import java.time.Instant;
import java.util.List;

public class TestDto {

    Channel channel;
    List<User> users;
    Instant lastMessageAt;
}
