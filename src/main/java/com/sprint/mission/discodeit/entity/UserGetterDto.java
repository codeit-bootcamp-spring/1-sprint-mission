package com.sprint.mission.discodeit.entity;


import java.util.UUID;
public record UserGetterDto(
        long createdAt,
        long updatedAt,
        UUID id,
        String email,
        String userName,
        String password
) {
    public static UserGetterDto from(User user){
        return new UserGetterDto(user.getCreatedAt(), user.getUpdatedAt(), user.getId(), user.getEmail(), user.getUserName(), user.getPassword());
    } //todo 유저 Dto에 유저 password를 반환하도록 하는게 맞나? 보안 ㄱㅊ? 애초에 저 숫자가 패스워드가 맞는지?
}
