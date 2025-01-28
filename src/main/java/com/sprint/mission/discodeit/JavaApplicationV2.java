package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserRepository;
import com.sprint.mission.discodeit.service.file.FileUserService;

import java.util.Scanner;

public class JavaApplicationV2 {
    private static Scanner sc = new Scanner(System.in);
    private static final UserRepository userRepository = new FileUserService();

    public static void main(String[] args) {
        showMainDisplay();
    }

    private static void showMainDisplay() {
        System.out.println("======================================================");
        System.out.println("=                    메인 페이지                      =");
        System.out.println("======================================================");
        System.out.println("= (1) 유저 입력                                       =");
        System.out.println("= (2) 유저 닉네임으로 조회                              =");
        System.out.println("= (3) 유저 전체 조회                                   =");
        System.out.println("= (4) 유저 업데이트                                    =");
        System.out.println("= (5) 유저 삭제                                       =");
        System.out.println("= (6) 종료                                           =");
        System.out.println("======================================================");
        System.out.print("입력: ");
        String userChoose = sc.nextLine();
        switch (userChoose) {
            case "1":
                moveSavePage();
                break;
            case "2":
                moveFindByIdPage();
                break;
            case "3":
                moveFinAllPage();
                break;
            case "4":
                break;
            case "5":
                break;
            case "6":
                return;
            default:
                System.out.println("똑바로 입력하시오");
                showMainDisplay();
                break;
        }
    }

    private static void moveSavePage() {
        UserRepository userRepository = new FileUserService();
        System.out.println("\n\n");
        while (true) {
        System.out.println("======================================================");
        System.out.println("=                      유저 저장                      =");
        System.out.println("======================================================");
        System.out.println("= (1). 새로운 유저 저장                                =");
        System.out.println("= (2). 나가기                                         =");
        System.out.println("======================================================");
        System.out.print("입력: ");
        String usrChoosenInUserSavePage = sc.nextLine();
            switch (usrChoosenInUserSavePage) {
                case "1":
                    System.out.println("======================================================");
                    System.out.print("= 유저 E-mail: ");
                    String eMail = sc.nextLine();
                    System.out.print("= 유저 닉네임: ");
                    String nickName = sc.nextLine();
                    System.out.print("= 유저 비밀번호: ");
                    String password = sc.nextLine();
                    userRepository.save(User.createUser(eMail, nickName, password, false));
                    break;
                case "2":
                    showMainDisplay();
                    return;
                default:
                    System.out.println("입력을 제대로 좀 하시오.");
            }
        }

    }

    private static void moveFindByIdPage() {

        System.out.println("\n\n");
        while (true) {
            System.out.println("======================================================");
            System.out.println("=                 유저 조회(E-mail)                   =");
            System.out.println("======================================================");
            System.out.println("= (1). 유저 조회하기                                   =");
            System.out.println("= (2). 나가기                                         =");
            System.out.println("======================================================");
            System.out.print("입력: ");
            String usrChoosenInFindByIdPage = sc.nextLine();
            switch (usrChoosenInFindByIdPage) {
            case "1":
                System.out.println("======================================================");
                System.out.print("조회하고 싶은 유저의 E-mail:");
                String emailForSearching = sc.nextLine();
                userRepository.findById(emailForSearching);
                break;
            case "2":
                showMainDisplay();
                return;
            default:
                System.out.println("똑바로 입력하시오.");
            }
        }
        }

    private static void moveFinAllPage() {
        System.out.println("\n\n");
        while (true) {
            System.out.println("======================================================");
            System.out.println("=                  유저 조회(ALL)                     =");
            System.out.println("======================================================");
            System.out.println("= (1). 유저 조회하기                                   =");
            System.out.println("= (2). 나가기                                         =");
            System.out.println("======================================================");
            System.out.print("입력: ");
            String usrChoosenInFindByIdPage = sc.nextLine();
            switch (usrChoosenInFindByIdPage) {
                case "1":
                    System.out.println("======================================================");
                    userRepository.findAll();
                    break;
                case "2":
                    showMainDisplay();
                    return;
                default:
                    System.out.println("똑바로 입력하시오.");
            }
        }
    }
}
