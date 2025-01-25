package mission.controller.file;

import mission.controller.UserController;
import mission.entity.User;
import mission.service.file.FileChannelService;
import mission.service.file.FileMessageService;
import mission.service.file.FileUserService;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class FileUserController implements UserController {

    private final FileUserService fileUserService = FileUserService.getInstance();
    private final FileChannelService fileChannelService = FileChannelService.getInstance();

    @Override
    public void create(String userName, String password){
        try {
            fileUserService.createOrUpdate(new User(userName, password));
        } catch (IOException e) {
            throw new RuntimeException("I/O 오류 : 유저 생성 실패");
        }
    }

    /**
     * 수정은 이름, 패스워드 무조건 한번에 바꾼다고 가정
     */
    @Override
    public void updateUserNamePW(UUID id, String newName, String newPassword) {
        // file 검증 끝이라 NULL 검증 필요 X
        fileUserService.update(fileUserService.findById(id).setNamePassword(newName, newPassword));
    }

    /**
     * 조회
     */
    @Override
    public User findById(UUID userId) {
        return fileUserService.findById(userId);
    }

    @Override
    public Set<User> findAll(){
        return fileUserService.findAll();
    }

    @Override
    public Set<User> findUsersByName(String findName){
        return fileUserService.findAll().stream()
                .filter(user -> user.getName().equals(findName))
                .collect(Collectors.toSet());
    }

    @Override
    public User findUserByNamePW(String name, String password){
        return fileUserService.findAll().stream()
                .filter(user -> user.getName().equals(name) && user.getPassword().equals(password))
                .findAny()
                .orElse(null);
        // 패스워드&이름 똑같다면 컬렉션 반환인데, 그럴 가능성 없다고 가증
    }

    @Override
    public Set<User> findUsersInChannel(UUID channelId) {
        return fileChannelService.findById(channelId).getUsersImmutable();
    }

    /**
     * 삭제
     */
    @Override
    public void deleteUser(UUID userId, String name, String password){
        User deletingUser = fileUserService.findById(userId);
        if (!(deletingUser.getName().equals(name) && deletingUser.getPassword().equals(password))) {
            System.out.println("닉네임 or 비밀번호가 틀렸습니다.");
            // 이 메서드에서 닉넴, 비번 검증
        } else {
            fileUserService.delete(deletingUser);
        }
    }

    @Override  // 채널 탈퇴
    public void drops(UUID channel_Id, UUID droppingUser_Id) {
        fileUserService.findById(droppingUser_Id).removeChannel(fileChannelService.findById(channel_Id));
    }

    @Override
    public void dropsAllByUser(UUID droppingUser_Id) {
        fileUserService.findById(droppingUser_Id).removeAllChannel();
    }

    @Override
    public void addChannelByUser(UUID channelId, UUID userId) {
        fileUserService.findById(userId).addChannel(fileChannelService.findById(channelId));
    }

    /**
     * 디렉토리 생성
     */
    public void createUserDirectory() throws IOException {
        fileUserService.createUserDirectory();
    }
}
