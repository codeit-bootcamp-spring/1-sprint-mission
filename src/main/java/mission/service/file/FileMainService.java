package mission.service.file;

import mission.entity.User;
import mission.repository.file.FileUserRepository;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

// 많은 일은 안하지만 그래도 생성, 업데이트 시 id, name, PW 검사정도만
// 이 검사를 뚫어야 (user or channel or message)service가 시작되게 설계
public class FileMainService {

    private final FileChannelService fileChannelService = new FileChannelService();
    private final FileUserService fileUserService = new FileUserService();
    private final FileMessageService fileMessageService = new FileMessageService();

    public void createUserDirectory() throws IOException {
        fileUserService.createUserDirectory();
    }

    public User createUser(String name, String password) throws IOException {
        // 중복검사 해야됨
        fileUserService.validateDuplicateName(name);
        User newbie = new User(name, password);
        return fileUserService.create(newbie);
    }

    public User findUserById(UUID id) {
        return fileUserService.findById(id);
    }

    public List<User> findAll() throws IOException {
        return fileUserService.findAll();
    }

    public User updateUserNamePW(UUID id, String oldName, String password, String newName) throws IOException {
        // 1. id 검증   2. 입력한 닉네임,PW 검증 후 수정해서 FILEUSERSERVICE에 넘김
        // (3 선택) 여기서 newName을 검증할지 말지 결정 <= 이거까지 맡으면 main이 하는 일이 많은 것 같은데

        User existingUser = findUserById(id);
        if (existingUser == null) {
            return null;
        }

        // 2번
        if (!(existingUser.getName().equals(oldName) & existingUser.getPassword().equals(password))) {
            throw new IllegalStateException(String.format("닉네임(%s) 또는 password가 잘못됐습니다", oldName));
        }

        // 3번은 fileUserService에 맡김
        existingUser.setOldName(existingUser.getName());
        existingUser.setNamePassword(newName, password);
        return fileUserService.update(existingUser);
    }


    /**
     * 삭제
     */
    public void deleteUser(UUID userId, String nickName, String password) {
        // 아이디 검증 <= findUserById에서 파일 존재 여부 확인을 해버려서 필요가 없어짐
        User deletingUser = findUserById(userId);
//        if (deletingUser == null) {
//            throw new NoSuchElementException("해당 ID를 가진 사용자는 없습니다");
//        }

        // 닉네임, 패스워드 검증
        if (!(deletingUser.getName().equals(nickName) && deletingUser.getPassword().equals(password))) {
            throw new IllegalStateException("닉네임 OR 패스워드가 잘못됐습니다.");
        }
        fileUserService.delete(deletingUser);
    }


}
