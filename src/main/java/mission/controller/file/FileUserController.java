package mission.controller.file;

import mission.entity.User;
import mission.service.file.FileUserService;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

public class FileUserController {

    private final FileUserService fileUserService = new FileUserService();

    public void create(String userName, String password){
        fileUserService.validateDuplicateName(userName);
        try {
            fileUserService.createOrUpdate(new User(userName, password));
        } catch (IOException e) {
            throw new RuntimeException("I/O 오류 : 유저 생성 실패");
        }
    }

    /**
     * 수정
     */

    public void updateName(UUID userId, String newName){
        User user = fileUserService.findById(userId);
        user.setName(newName);
        fileUserService.update(user);
    }

    /**
     * 조회
     */

    public User findUserById(UUID userId){
        return fileUserService.findById(userId);
    }

    public Set<User> findAll(){
        return fileUserService.findAll();
    }

    public User findUserByName_Password(String name, String password){
        return fileUserService.findUsersByName(name).stream()
                .filter(user -> user.getPassword().equals(password))
                .findAny()
                .orElse(null);
        // 패스워드&이름 똑같다면 컬렉션 반환인데, 그럴 가능성 없다고 가증
    }

    /**
     * 삭제
     */
    public void delete(UUID userId, String name, String password){
        User deletingUser = fileUserService.findById(userId);
        if (!(deletingUser.getName().equals(name) && deletingUser.getPassword().equals(password))) {
            System.out.println("닉네임 or 비밀번호가 틀렸습니다.");
            // 이 메서드에서 닉넴, 비번 검증
        } else {
            fileUserService.delete(deletingUser);
        }
    }
}
