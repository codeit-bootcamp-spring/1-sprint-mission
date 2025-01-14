package mission;

import mission.entity.User;
import mission.service.file.FileMainService;
import mission.service.jcf.ProjectManager;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class javaApplication {

    private static final ProjectManager projectManager = new ProjectManager();
    private static final FileMainService fileMainService = new FileMainService();

    public static void main(String[] args) throws IOException {
        // 사전 세팅
        initDirectory();

        // 유저 생성 테스트
        User userA = UserCreateTest("직원 A", "1234");
        User userB = UserCreateTest("직원 B", "4321");


        // 찾는 유저 테스트
        User findUser = UserFindTest(userA.getId());
        System.out.println("findUser = " + findUser);
        System.out.println("findUser = userA : " + userA.equals(findUser));

        // 모두 찾는 테스트
        List<User> users = findUsers();

        // User B 삭제 테스트 : 필요 정보 ID, NAME, PW
        fileMainService.deleteUser(userB.getId(), userB.getName(), userB.getPassword());
        //UserFindTest(userB.getId());
    }

    private static List<User> findUsers() throws IOException {
        List<User> users = fileMainService.findAll();
        System.out.println("전체 유저 수 = " + users.size());
        return users;
    }

    private static User UserFindTest(UUID findId) throws IOException {
        User findUser = fileMainService.findUserById(findId);
        return findUser;
    }

    private static User UserCreateTest(String name, String password) throws IOException {
        User createUser = fileMainService.createUser(name, password);
        System.out.println("createUser = " + createUser);
        return createUser;
    }


    private static void initDirectory() throws IOException {
        fileMainService.createUserDirectory();
        System.out.println("User 디렉토리 추가 완료");
        // 이거 말고도 Channel, Message 추가


    }
}
