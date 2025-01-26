package mission;

import mission.controller.ChannelController;
import mission.controller.MessageController;
import mission.controller.UserController;
import mission.controller.jcf.JCFChannelController;
import mission.controller.jcf.JCFMessageController;
import mission.controller.jcf.JCFUserController;
import mission.entity.User;
import java.io.IOException;
import java.util.*;

public class javaApplication {

    private static final ChannelController channelController = new JCFChannelController();
    private static final UserController userController = new JCFUserController();
    private static final MessageController messageController = new JCFMessageController();

    public static void main(String[] args) throws IOException {

        // 유저 생성 테스트
        List<User> userList = userCreateTest(3);

        // 수정
        User beforeUpdateUser = userList.get(0);
        System.out.println("BeforeUpdate => 닉네임: " + beforeUpdateUser.getName() + ", 비밀번호: " + beforeUpdateUser.getPassword());
        User afterUpdateUser = userController.updateUserNamePW(beforeUpdateUser.getId(), beforeUpdateUser.getName() + "-1", String.valueOf(1000 + new Random().nextInt(9000)));
        System.out.println("afterUpdate => 닉네임: " + afterUpdateUser.getName() + ", 비밀번호: " + afterUpdateUser.getPassword());

        // 나머지 시간 부족



    }

    private static void findUserTest(){
        List<User> userList = userController.findAll().stream().toList();
        System.out.println("전체 유저 목록: " + userList + "유저 수: " + userList.size());
        User findUser = userController.findById(userList.get(0).getId());
        System.out.println("findById = " + findUser);
    }

    private static List<User> userCreateTest(int userCount) {
        for (int i = 1; i <= userCount; i++) {
            User creatUser = userController.create("유저" + i, String.valueOf(1000 + new Random().nextInt(9000)));
            System.out.println(creatUser.getName() + " 생성 완료");
        }
        System.out.println("전체 유저: " + userController.findAll());
        return userController.findAll().stream().toList();
   }


}
