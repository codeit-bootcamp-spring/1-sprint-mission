package mission;

import mission.entity.User;
import mission.controller.FileMainService;
import mission.controller.ProjectManager;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

public class javaApplication {

    private static final ProjectManager projectManager = new ProjectManager();
    private static final FileMainService fileMainService = new FileMainService();

    public static void main(String[] args) throws IOException {
        // 사전 세팅
        initDirectory();

        // 유저 생성 테스트
        User userA = userCreateTest("직원 A", "1234");
        User userB = userCreateTest("직원 B", "4321");


        // 찾는 유저 테스트
        User findUserById = userFindTest(userA.getId());
        User findUserByN_PW = fileMainService.findUserByNamePW("직원 A", "1234");
        System.out.println("findUserById = " + findUserById);
        System.out.println("findUserById = userA : " + userA.equals(findUserById));
        System.out.println("findUserByN_PW = userA : " + userA.equals(findUserByN_PW));

        // 모두 찾는 테스트
        Set<User> users = findUsers();

        // 닉네임 패스워드 수정
        User updateUserA = fileMainService.updateUserNamePW(userA.getId(), userA.getName(), userA.getPassword(), "유저 F", "9999");
        System.out.println("기존 유저 A id = 업테이트 유저 id : " + updateUserA.getId().equals(userA.getId()));
        System.out.println("기존 유저 A의 닉네임 = 업데이트 유저 닉네임 : " + updateUserA.getName().equals(userA.getName()));

        // 중복검사해야하네

        // User B 삭제 테스트 : 필요 정보 ID, NAME, PW
        fileMainService.deleteUser(userB.getId(), userB.getName(), userB.getPassword());
        System.out.println("전체 유저 목록 = " + findUsers());


    }

    private static Set<User> findUsers() throws IOException {
        Set<User> users = fileMainService.findAllUser();
        System.out.println("전체 유저 수 = " + users.size());
        return users;
    }

    private static User userFindTest(UUID findId) throws IOException {
        User findUser = fileMainService.findUserById(findId);
        return findUser;
    }

    private static User userCreateTest(String name, String password) throws IOException {
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
