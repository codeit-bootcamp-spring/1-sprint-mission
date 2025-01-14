package mission.service.file;

import mission.entity.Channel;
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

    /**
     * 디렉토리 생성
     */
    public void createUserDirectory() throws IOException {
        fileUserService.createUserDirectory();
    }

    public void createChannelDirectory() {
        fileChannelService.createChannelDirect();
    }

    /**
     * 파일 생성
     */

    public User createUser(String name, String password) throws IOException {
        // 중복검사 해야됨
        fileUserService.validateDuplicateName(name);
        User newbie = new User(name, password);
        return fileUserService.create(newbie);
    }

    public Channel createChannel(String channelName){
        // 이름 중복 검증을 하고 channel 생성
        fileChannelService.validateDuplicateName(channelName);
        return fileChannelService.create(new Channel(channelName));
    }


    /**
     * 조회
     */

    public User findUserById(UUID id) {
        return fileUserService.findById(id);
    }

    public List<User> findAllUser() throws IOException {
        return fileUserService.findAll();
    }

    // id 잃어버렸을 때, name과 pw로 찾기
    public User findUserByNamePW(String name, String password) throws IOException {
        // 코드 중복 줄이고 파라미터 귀찮으니 User서비스가 아닌 여기서 처리
        List<User> users = findAllUser();
        return users.stream()
                .filter(user -> user.getName().equals(name) && user.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }


    /**
     * 업데이트
     */

    public User updateUserNamePW(UUID id, String oldName, String password, String newName, String newPassword) throws IOException {
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
        existingUser.setNamePassword(newName, newPassword);
        return fileUserService.update(existingUser);
    }

    public Channel updateChannelName(UUID channelId, String oldName, String newName){
        Channel updatingChannel = fileChannelService.findById(channelId);
        if (updatingChannel.getName() != oldName){
            return null;
        }

        updatingChannel.setOldName(updatingChannel.getName());
        updatingChannel.setName(newName);
        return fileChannelService.update(updatingChannel);
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
