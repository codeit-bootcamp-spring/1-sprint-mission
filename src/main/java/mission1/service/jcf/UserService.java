package mission1.service.jcf;


import mission1.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    //등록
    User create(User user);

    //[ ] 조회(단건)
    User find(UUID id);

    //[ ] 조회(다건)
    List<User> findAll();

    //[ ] 수정
    User update(UUID id, String name, String password);

    //[ ] 삭제
    void delete(UUID id, String name, String password);
}
