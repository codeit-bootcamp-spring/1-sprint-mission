//package com.sprint.mission.discodeit.jcf;
//
//import com.sprint.mission.discodeit.dto.UserDto;
//import com.sprint.mission.discodeit.entity.User;
//import com.sprint.mission.discodeit.repository.UserRepository;
//import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
//import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
//import com.sprint.mission.discodeit.service.UserService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import javax.swing.*;
//import java.util.List;
//import java.util.UUID;
//
//@Service
//@RequiredArgsConstructor
//public class JCFUserService implements UserService {
//    private JCFUserRepository userRepository;
//    private JCFMessageRepository messageRepository;
//    private JCFChannelService channelService;
//    private JCFUserStatusService jcfUserStatusService;
//
//    @Override
//    public User createUser(String email, String name, String password) {
//                if (email.contains("@") && email.contains(".") &&
//                        userRepository.findUserByEmail(email) != null) {
//            User newUser = new User(UUID.randomUUID(), email, name, password);
//            userRepository.saveUser(newUser);
//            System.out.println("\n***계정 생성 완료***");
//            return newUser;
//        } else {
//            throw new IllegalArgumentException("**올바른 이메일 형식인지 확인해 주세요.**");
//        }
//    }
//
//    @Override
//    public void updateMail(User user, String mail) {
//        if (mail.contains("@") && mail.contains(".") &&! user.getEmail().equals(mail)){
//            user.setEmail(mail);
//            userRepository.saveUser(user);
//            System.out.println("\n***이메일 변경 완료***");
//            System.out.println(user);
//        } else {
//            System.out.println("**올바른 이메일 형식인지 확인해 주세요.**");
//        }
//    }
//
//    @Override
//    public List<User> findAllUserList() {
//        System.out.println("\n***사용자 정보 전부 조회***");
//        return userRepository.printAllUser();
//    }
//
//    @Override
//    public void searchUser(User user) {
//        User foundUser = userRepository.findUserByEmail(user.getEmail());
//        if(foundUser != null){
//            System.out.println("\n***[사용자 정보]***");
//            System.out.println(foundUser);
//        } else {
//            System.out.println("**유저를 찾을 수 없습니다.**\n");
//        }
//    }
//
//    @Override
//    public void deleteUser(User user) {
//        if (userRepository.findUserByEmail(user.getEmail()).equals(user)) {
//            userRepository.deleteUser(user);
//            System.out.println("\n***계정 삭제 완료***");
//        } else {
//            System.out.println("**유저를 찾을 수 없습니다.**\n");
//        }
//    }
//}
//
