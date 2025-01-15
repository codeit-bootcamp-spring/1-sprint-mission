package mission.service.file;

import mission.entity.Channel;
import mission.entity.Message;
import mission.entity.User;
import mission.repository.file.FileUserRepository;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;

// 많은 일은 안하지만 그래도 생성, 업데이트 시 id, name, PW 검사정도만
// 이 검사를 뚫어야 (user or channel or message)service가 시작되게 설계
public class FileMainService {

    // 지금은 간단한 CRUD 기능이지만 Channel, Message, User 서비스간 의존이 늘어나면 이렇게 Main 클래스 필요
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

    public User createUser(String name, String password){
        // 중복검사 해야됨
        fileUserService.validateDuplicateName(name);
        User newbie = new User(name, password);

        try {
            return fileUserService.create(newbie);
        } catch (IOException e) {
            System.out.println("I/O 오류 : 생성 실패");
            return null;
        }

        // Main에서 잡은 이유
        // userservice에서 create메서드를 update랑 같이 쓰면서 코드 중복을 줄였는데,
        // 생성이 실패된건지, 수정이 실패된건지 파악하기 위해 userservice의 create문에서 예외처리하지 않고 여기서 처리
    }

    public Channel createChannel(String channelName){
        // 중복 검증 처리 시 : fileChannelService.validateDuplicateName(channelName);
        return fileChannelService.create(new Channel(channelName));
    }


    /**
     * 조회
     */

    public User findUserById(UUID id) {
        return fileUserService.findById(id);
    }

    public Set<User> findAllUser() throws IOException {
        return fileUserService.findAll();
    }

    public Set<User> findUserByName(String findName){
        return fileUserService.findUsersByName(findName);
    }

    // id 잃어버렸을 때, name과 pw로 찾기
    public User findUserByNamePW(String name, String password) throws IOException {
        // 코드 중복 줄이고 파라미터 귀찮으니 User서비스가 아닌 여기서 처리
        Set<User> findNameUsers = findUserByName(name);
        if (findNameUsers.isEmpty()){
            System.out.println("닉네임을 잘못 입력하셨습니다");
            return null;
        } else {
            return findNameUsers.stream()
                    .filter(user ->  user.getPassword().equals(password))
                    .findFirst()
                    .orElseGet(() -> {
                        System.out.println("비밀번호를 잘못 입력하셨습니다");
                        return null;});
        }
    }


    /**
     * 업데이트
     */

    public User updateUserNamePW(UUID id, String oldName, String password, String newName, String newPassword) throws IOException {
        // 1. id 검증   2. 입력한 닉네임,PW 검증 후 수정해서 FILEUSERSERVICE에 넘김
        // (3 선택) 여기서 newName을 검증할지 말지 결정 <= 이거까지 맡으면 main이 하는 일이 많은 것 같은데

        User existingUser = findUserById(id); // file 검증 끝이라 NULL 검증 필요 X
        if (!(existingUser.getName().equals(oldName) & existingUser.getPassword().equals(password))) {
            throw new IllegalStateException(String.format("닉네임(%s) 또는 password가 잘못됐습니다", oldName));
        }
        // 3. 유저 이름 중복 검증 시 사용
        // fileUserService.validateDuplicateName(newName);
        existingUser.setNamePassword(newName, newPassword);
        return fileUserService.update(existingUser);
    }

    public Channel updateChannelName(UUID channelId, String oldName, String newName){
        Channel updatingChannel = fileChannelService.findById(channelId);
        if (!updatingChannel.getName().equals(oldName)){
            System.out.printf("(변경 전 채널명 : %s)을(를) 잘못입력했습니다.", oldName);
            System.out.println();
            return null;
        }

        fileChannelService.validateDuplicateName(newName);
        updatingChannel.setName(newName);
        return fileChannelService.update(updatingChannel);
    }

    public Message updateMessage(UUID messageId, String newMessage){
        Message updatingMessage = fileMessageService.findMessageById(messageId);
        updatingMessage.setMessage(newMessage);
        return fileMessageService.update(updatingMessage);
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
