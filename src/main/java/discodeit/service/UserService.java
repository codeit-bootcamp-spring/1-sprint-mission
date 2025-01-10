package discodeit.service;

import discodeit.enity.Message;
import discodeit.enity.User;

public interface UserService {

    // Create
    User createUser(String email, String name);  // 유저 생성
    // Read
    void viewUserInfo(User user);    // 유저 정보 단건 조회
    void viewAllUser(); // 모든 유저 조회
    // Update
    void updateUserName(User user, String name);  // 유저 이름 수정
    void updateUserEmail(User user, String email);  // 유저 이메일 수정
    // Delete
    void deleteUser(User user);

}
