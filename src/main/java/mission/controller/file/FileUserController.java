package mission.controller.file;

import mission.entity.User;
import mission.service.file.FileUserService;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

public class FileUserController {

    private final FileUserService fileUserService = FileUserService.getInstance();

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

    public User updateUserNamePW(UUID id, String oldName, String password, String newName, String newPassword) throws IOException {
        // 1. id 검증   2. 입력한 닉네임,PW 검증 후 수정해서 FILEUSERSERVICE에 넘김
        // (3 선택) 여기서 newName을 검증할지 말지 결정 <= 이거까지 맡으면 main이 하는 일이 많은 것 같은데

        User existingUser = fileUserService.findById(id); // file 검증 끝이라 NULL 검증 필요 X
        if (!(existingUser.getName().equals(oldName) & existingUser.getPassword().equals(password))) {
            throw new IllegalStateException(String.format("닉네임(%s) 또는 password가 잘못됐습니다", oldName));
        }

        // 3. 유저 이름 중복 검증 시 사용
        // fileUserService.validateDuplicateName(newName);
        existingUser.setNamePassword(newName, newPassword);
        return fileUserService.update(existingUser);
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

    /**
     * 디렉토리 생성
     */
    public void createUserDirectory() throws IOException {
        fileUserService.createUserDirectory();
    }
}
