package com.sprint.mission.discodeit.dto;

/* # UserResDTO
 * - 사용자 응답 전달 객체
 * -- 응답 전달할 때
 * -- 완전 기본, 추가 필요하면 또 추가해야지
 */

import com.sprint.mission.discodeit.entity.User;
import lombok.Getter;

@Getter
public class UserResDTO implements UserDTO {
    private Long id;
    private String uuid;
    private String userName;
    private String nickname;
    private String email;

    public UserResDTO(Long id, User user) {
        this.id = id;
        this.uuid = user.getId().toString();
        this.userName = user.getUserName().getName();
        this.nickname = user.getNickname().getName();
        this.email = user.getEmail().getEmail();
    }

    @Override
    public String toString() {
        return "UserResDTO{" +
                "id=" + id +
                ", uuid='" + uuid + '\'' +
                ", userName='" + userName + '\'' +
                ", nickname='" + nickname + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
