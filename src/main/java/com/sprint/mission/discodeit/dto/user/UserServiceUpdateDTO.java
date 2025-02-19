package com.sprint.mission.discodeit.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserServiceUpdateDTO {
    private UUID id;
    private String name;
    private String email;
    private MultipartFile file;
}
