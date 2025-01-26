package mission;

import mission.controller.ChannelController;
import mission.controller.MessageController;
import mission.controller.UserController;
import mission.controller.file.FileChannelController;
import mission.controller.file.FileMessageController;
import mission.controller.file.FileUserController;
import mission.entity.User;

import java.util.*;

public class fileApplication {

    private static final ChannelController fileChannelController = new FileChannelController();
    private static final UserController fileUserController = new FileUserController();
    private static final MessageController fileMessageController = new FileMessageController();

    public static void main(String[] args) {
        initDirectory();

        System.out.println("<<<<<<<<<<<<<<<<<<<<<<USER 테스트>>>>>>>>>>>>>>>>>>>>>>");

        //생성 테스트
        List<User> userList = createUser(3);

        //이름 비번 수정 테스트
        User updateUser = updateTest(userList.get(1));
        System.out.println(fileUserController.findAll());

        //삭제 테스트
        deleteTest(updateUser);

        //조회 테스트
        findTest();

        System.out.println("<<<<<<<<<<<<<<<<<<<<<<Channel 테스트>>>>>>>>>>>>>>>>>>>>>>");





    }

    private static Boolean findTest() {
        System.out.println("==============조회 테스트 시작==============");
        Set<User> users = fileUserController.findAll();

        for (User user : users) {
            User testUser = fileUserController.findById(user.getId());
            if (!testUser.equals(user)){
                System.out.println("find 메서드 오류");
                return false;
            }
        }
        System.out.println("findUser 테스트 완료 : 전부 일치");
        return true;
    }

    private static void deleteTest(User deletingUser) {
        System.out.println("==============삭제 테스트 시작==============");
        Set<User> beforeUsers = fileUserController.findAll();
        System.out.println("삭제 전 유저 목록: " + beforeUsers + "유저 수: " + beforeUsers.size());
        fileUserController.deleteUser(deletingUser.getId(), deletingUser.getName(), deletingUser.getPassword());
        Set<User> afterUsers = fileUserController.findAll();
        System.out.println("삭제 후 유저 목록: " + afterUsers + "유저 수: " + afterUsers.size());
    }

    private static User updateTest(User beforeUpdateUser) {
        System.out.println("==============수정 테스트 시작==============");
        fileUserController.updateUserNamePW(beforeUpdateUser.getId(), "user 2-1", "1234");
        User updateUser = fileUserController.findById(beforeUpdateUser.getId());
        System.out.println("업데이트 전 : " + beforeUpdateUser);
        System.out.println("업데이트 후 : " + updateUser); // 아이디 같음
        return updateUser;
    }


    private static List<User> createUser(int userCount) {
        System.out.println("==============생성 테스트 시작==============");
        System.out.println("==============USER 생성 수 : " + userCount + "==============");
        Random random = new Random();
        List<User> users = new ArrayList<>();
        for (int i = 0; i < userCount; i++){
            int randomPassword = 1000 + random.nextInt(9000);
            users.add(fileUserController.create("user " + (i+1), String.valueOf(randomPassword)));
        }
        System.out.println("user 목록: " + users + "유저 수: " + users.size());
        return users;
    }


    private static void initDirectory(){
        System.out.println("==============디렉토리 초기화==============");
        fileChannelController.createChannelDirectory();
        System.out.println("Channel 디렉토리 추가 완료");
        fileUserController.createUserDirectory();
        System.out.println("User 디렉토리 추가 완료");
        fileMessageController.createMessageDirectory();
        System.out.println("Message 디렉토리 추가 완료");
    }
}
