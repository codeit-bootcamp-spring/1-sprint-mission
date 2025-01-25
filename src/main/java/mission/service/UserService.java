package mission.service;


import mission.entity.User;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface UserService {
    //등록
    User createOrUpdate(User user) throws IOException;

    //[ ] 조회(단건)
    User findById(UUID id);
    //User findByNamePW(String name, String password);

    //[ ] 조회(다건)
    Set<User> findAll();

    //[ ] 수정
    // 그냥 한번에 닉네임, 비밀번호 다 바꾼다고 가정
    User update(User user);

    Set<User> findUsersByName(String findName);

    //[ ] 삭제
    void delete(User user);

    void validateDuplicateName(String name);
}
