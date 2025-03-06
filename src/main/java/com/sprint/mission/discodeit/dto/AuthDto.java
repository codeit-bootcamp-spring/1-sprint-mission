package com.sprint.mission.discodeit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class AuthDto {
    private UUID id;
    private String username;
    private String password;
}