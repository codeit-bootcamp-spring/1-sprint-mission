package mission.service;


import mission.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    //등록
    User create(User user);

    //[ ] 조회(단건)
    User findById(UUID id);
    User findByNamePW(String name, String password);

    //[ ] 조회(다건)
    List<User> findAll();

    //[ ] 수정
    //User update(UUID id, String name, String password);

    // 그냥 한번에 닉네임, 비밀번호 다 바꾼다고 가정
    User update(User user);

    //[ ] 삭제
    void delete(UUID id, String name, String password);

}
