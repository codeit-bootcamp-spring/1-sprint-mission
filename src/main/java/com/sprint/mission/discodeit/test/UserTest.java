package com.sprint.mission.discodeit.test;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.view.DisplayUser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.UUID;

public class UserTest {

    // 유저 생성 시 자동으로 이메일, 이름, 닉네임을 다르게 하기 위한 필드
    private static int userIndex = 1;

    // 유저 생성
    public static User setUpUser(UserService userService, UserRepository userRepository) throws IOException {


        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

//            System.out.println("이메일 입력 :");
//            String email = br.readLine();
//
//            System.out.println("비밀번호 입력 :");
//            String password = br.readLine();
//
//            System.out.println("이름 입력 :");
//            String name = br.readLine();
//
//            System.out.println("닉네임 입력 :");
//            String nickname = br.readLine();
//
//            System.out.println("전화번호 입력 :");
//            String phoneNumber = br.readLine();

        String email = "test" + userIndex + "@email.com";
        String password = "PWpw11!!";
        String name = "test_name" + userIndex;
        String nickname = "test_nickname" + (userIndex++);
        String phoneNumber = "010-0000-0000";

        User user = new User(email, password, name, nickname, phoneNumber, userRepository);

        userService.create(user);

        System.out.println("================================================================================");
        System.out.println("유저 생성 결과 : ");
        DisplayUser.displayUserInfo(user);
        System.out.println("================================================================================");

        return user;
    }

    // 유저 정보 변경
    public static void updateUser(UUID id, UserService userService) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        loopOut:
        while (true) {
            System.out.println("================================================================================");
            System.out.println("1. 현재 유저 정보 확인");
            System.out.println("2. 이메일 변경");
            System.out.println("3. 비밀번호 변경");
            System.out.println("4. 이름 변경");
            System.out.println("5. 닉네임 변경");
            System.out.println("6. 전화번호 변경");
            System.out.println("7. 종료");
            System.out.println("================================================================================");

            String menu = br.readLine();

            User user = userService.read(id);

            try {

                switch (menu) {

                    case "1":
                        System.out.println("================================================================================");
                        System.out.println("현재 정보 : ");
                        DisplayUser.displayUserInfo(user);
                        System.out.println("================================================================================");
                        break;

                    case "2":
                        System.out.println("이메일 입력 :");
                        String email = br.readLine();
                        userService.updateEmail(id, email);
                        break;

                    case "3":
                        System.out.println("비밀번호 입력 :");
                        String password = br.readLine();
                        userService.updatePassword(id, password);
                        break;

                    case "4":
                        System.out.println("이름 입력 :");
                        String name = br.readLine();
                        userService.updateName(id, name);
                        break;

                    case "5":
                        System.out.println("닉네임 입력 :");
                        String nickname = br.readLine();
                        userService.updateNickname(id, nickname);
                        break;

                    case "6":
                        System.out.println("전화번호 입력 :");
                        String phoneNumber = br.readLine();
                        userService.updatePhoneNumber(id, phoneNumber);
                        break;

                    case "7":
                        System.out.println("종료합니다.");
                        break loopOut;

                    default:
                        System.out.println("1 ~ 7 중 하나를 선택해주세요.");
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }

    // 유저 삭제
    public static void deleteUser(UUID id, UserService userService) {

        System.out.println("================================================================================");
        System.out.println("유저 삭제 전 목록 :");
        DisplayUser.displayAllUserInfo(userService.readAll());
        System.out.println("================================================================================");

        userService.delete(id);

        System.out.println("================================================================================");
        System.out.println("유저 삭제 후 목록 :");
        DisplayUser.displayAllUserInfo(userService.readAll());
        System.out.println("================================================================================");
    }
}
