package com.sprint.mission.discodeit.service;

public interface UserService {
    public abstract void Create(String nickname);

    // Read : 전체 유저 조회, 특정 유저 조회
    public abstract int ReadAll();
    public abstract String ReadUser(String nickname);

    // Update : 특정 유저 닉네임 변경
    public abstract void UpdateNickname(String nickname);

    // Delete : 전체 유저 삭제, 특정 유저 삭제
    public abstract void DeleteAll();
    public abstract void DeleteUser(String nickname);

}
