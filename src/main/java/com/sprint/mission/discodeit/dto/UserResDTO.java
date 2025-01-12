package com.sprint.mission.discodeit.dto;

/* # UserResDTO
 * - 사용자 응답 전달 객체
 * -- 응답 전달할 때
 * -- 완전 기본, 추가 필요하면 또 추가해야지
 */

import com.sprint.mission.discodeit.entity.User;

public class UserResDTO implements UserDTO{
    private Long id;
    private String uuid;
    private String userName;
    private String nickname;
    private String email;

    public UserResDTO(Long id, User user){
        this.id = id;
        this.uuid = user.getId().toString();
        this.userName = user.getUserName().getName();
        this.nickname = user.getNickname().getName();
        this.email = user.getEmail().getEmailAddress();
    }

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public String getNickname() {
        return nickname;
    }

    @Override
    public String getEmail() {
        return email;
    }

    public Long getId() {
        return id;
    }

    public String getUuid() {
        return uuid;
    }
}
