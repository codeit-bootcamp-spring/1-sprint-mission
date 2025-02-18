package com.sprint.mission.discodeit.dto.user;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

@Getter
@Setter
public class UserCreateDTO extends UserBaseDTO{
    private String password;
    private Optional<MultipartFile> profileImage; // 프로필 사진 -> optional 사용(명시적으로 선택적 옵션)

    public UserCreateDTO(UUID id, String userName, String userEmail, String password, MultipartFile profileImage) {
        super(id, userName, userEmail);
        this.password = password;
        this.profileImage = Optional.ofNullable(profileImage); //값이 없으면 자동으로 empty
    }
}
