package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;

//public class UserValidTest {
//
//    static void userNameTest(UserService userService) {
//        // 사용자 예외 테스트
//        System.out.println("사용자 이름이 null일 경우");
//        try {
//            User user = userService.create(null, "buzzin2426@gmail.com", "010-1234-1234", "qwer!@34");
//            System.out.println("유저 생성: " + user.getId());
//        } catch (Exception e){
//            System.out.println(e.getMessage());
//        }
//
//        System.out.println("사용자 이름이 비어있을 경우");
//        try {
//            User user = userService.create("", "buzzin2426@gmail.com", "01012341234", "qwer!@34");
//            System.out.println("유저 생성: " + user.getId());
//        } catch (Exception e){
//            System.out.println(e.getMessage());
//        }
//
//        System.out.println("사용자 이름 32자를 초과할 경우");
//        try {
//            User user = userService.create("qwerqwerqwerqwerqwerqwerqwerqwera", "buzzin2426@gmail.com", "010-1234-1234", "qwer!@34");
//            System.out.println("유저 생성: " + user.getId());
//        } catch (Exception e){
//            System.out.println(e.getMessage());
//        }
//    }
//
//    static void userEmailTest(UserService userService) {
//        // 이메일 예외 테스트
//        System.out.println("이메일이 유효하지 않을 경우");
//        try {
//            User user = userService.create("이병규", "buzzin2426gmail.com", "01012341234", "qwer!@34");
//            System.out.println("유저 생성: " + user.getId());
//        } catch (Exception e){
//            System.out.println(e.getMessage());
//        }
//    }
//
//    static void userphoneNumberTest(UserService userService) {
//        // 전화번호 예외 테스트
//        System.out.println("전화번호가 유효하지 않을 경우");
//        try {
//            User user = userService.create("이병규", "buzzin2426@gmail.com", "01032132", "qwer!@34");
//            System.out.println("유저 생성: " + user.getId());
//        } catch (Exception e){
//            System.out.println(e.getMessage());
//        }
//    }
//
//    static void userpasswordTest(UserService userService) {
//        // 비밀번호 예외 테스트
//        System.out.println("비밀번호가 유효하지 않을 경우");
//        try {
//            User user = userService.create("이병규", "buzzin2426@gmail.com", "01012341234", "1234");
//            System.out.println("유저 생성: " + user.getId());
//        } catch (Exception e){
//            System.out.println(e.getMessage());
//        }
//    }
//
//    public static void main(String[] args) {
//        UserService userService = new BasicUserService(new FileUserRepository());
//
//        userNameTest(userService);
//        System.out.println();
//        userEmailTest(userService);
//        System.out.println();
//        userphoneNumberTest(userService);
//        System.out.println();
//        userpasswordTest(userService);
//    }
//}
