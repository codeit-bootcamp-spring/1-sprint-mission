package mission.service;


import mission.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    //등록
    User create(String name, String password);

    //[ ] 조회(단건)
    User findById(UUID id);
    User findByNamePW(String name, String password);

    //[ ] 조회(다건)
    List<User> findAll();

    //[ ] 수정
    User update(UUID id, String name, String password);

    //[ ] 삭제
    void delete(UUID id, String name, String password);

}
