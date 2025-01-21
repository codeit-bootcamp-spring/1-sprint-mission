package discodeit.service;

import discodeit.enity.User;

import java.util.UUID;

public interface UserService {

    // Create
    UUID createUser(String email, String name, String password);  // 유저 생성

    // Read
    void viewUserInfo(UUID userId);    // 유저 정보 단건 조회

    void viewAllUser(); // 모든 유저 조회

    // Update
    void updateUserName(UUID userId, String name);  // 유저 이름 수정

    void updateUserEmail(UUID userId, String email);  // 유저 이메일 수정

    void updateUserPassword(UUID userId, String password);  // 유저 비밀번호 수정

    // Delete
    void deleteUser(UUID userId);

    User findById(UUID userId);
}
