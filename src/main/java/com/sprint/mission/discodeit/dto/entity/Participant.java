package com.sprint.mission.discodeit.dto.entity;


import lombok.Getter;

@Getter
public class Participant{
    private Long id;
    private User user;
    private Channel channel;
}
