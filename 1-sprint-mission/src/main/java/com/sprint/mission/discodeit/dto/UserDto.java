package com.sprint.mission.discodeit.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Data
@Getter
@Setter
public class UserDto {
    UUID id;
    String userName;
    String password;
    String email;
    Boolean containContent;
    String newName;
    String contentUrl;
}
