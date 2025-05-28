package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.common.Phone;
import com.sprint.mission.discodeit.security.role.Role;

public record UserUpdateDto(String username, String nickname, String email,
                            Phone.RegionCode regionCode, String phone, Role role,
                            String introduce) {

}
