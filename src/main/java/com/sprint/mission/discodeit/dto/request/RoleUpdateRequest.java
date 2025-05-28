package com.sprint.mission.discodeit.dto.request;

import com.sprint.mission.discodeit.eenum.Role;
import jakarta.validation.constraints.NotBlank;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleUpdateRequest {
  @NotBlank(message = "사용자 아이디는 필수입니다")
  UUID userId;
  @NotBlank(message = "부여될 권한은 필수입니다")
  Role newRole;
}
