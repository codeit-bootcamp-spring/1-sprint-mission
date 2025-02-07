//package com.sprint.mission;
//
//import com.sprint.mission.controller.ChannelController;
//import com.sprint.mission.controller.MessageController;
//import com.sprint.mission.controller.UserController;
//import com.sprint.mission.controller.file.FileChannelController;
//import com.sprint.mission.controller.file.FileMessageController;
//import com.sprint.mission.controller.file.FileUserController;
//import com.sprint.mission.entity.Channel;
//import com.sprint.mission.entity.Message;
//import com.sprint.mission.entity.User;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//import java.util.Set;
//
//public class fileApplication {
//
//    private static final ChannelController fileChannelController = new FileChannelController();
//    private static final UserController fileUserController = new FileUserController();
//    private static final MessageController fileMessageController = new FileMessageController();
//
//    public static void main(String[] args) throws IOException {
//        initDirectory();
//        //생성 테스트
//        List<User> userList = createUser(3);
//
//        //이름 비번 수정 테스트
//        User updatedUser = updateTest(userList.get(1));
//        System.out.println(fileUserController.findAll());
//
//        //삭제 테스트
//        deleteTest(updatedUser);
//
//        //조회 테스트
//        findTest();
//
//        System.out.println("<<<<<<<<<<<<<<<<<<<<<<Channel 테스트>>>>>>>>>>>>>>>>>>>>>>");
//
//        // 생성
//        List<Channel> channelList = createChannel(2);
//
//        // 수정
//        Channel updatedChannel = updateChannelName(channelList.get(0), "new Name");
//
//        // user 등록
//        registerUsers(updatedChannel); // 존재하는 유저 전부 등록 (테스트니까)
//
//        // 삭제
//        deleteChannel(channelList.get(1));
//
//        System.out.println("<<<<<<<<<<<<<<<<<<<<<<Message 테스트>>>>>>>>>>>>>>>>>>>>>>");
//        // 등록
//        Channel writingAt = fileChannelController.findAll().stream().toList().get(0);
//        User writer = fileUserController.findAll().stream().toList().get(0);
//        Message message1 = fileMessageController.create(writingAt.getId(), writer.getId(), "안녕하세요");
//        System.out.println(message1);
//
//        // 수정 및 조회 테스트
//        Message updateMessage = fileMessageController.update(message1.getId(), "안녕히 계세요");
//        System.out.println("수정된 메시지: " + fileMessageController.findById(updateMessage.getId()));
//        System.out.println("전체 메시지: " + fileMessageController.findAll());
//
//        // 삭제
//        fileMessageController.delete(updateMessage.getId());
//        System.out.println(updateMessage.getWritedAt().getName() + "에서 " + updateMessage.getWriter().getName() + "(이)가 작성한 메시지 삭제 완료");
//        System.out.println("전체 메시지: " + fileMessageController.findAll());
//    }
//
//    private static void registerUsers(Channel updatedChannel) throws IOException {
//        for (User user : fileUserController.findAll()) {
//            fileChannelController.addUserByChannel(updatedChannel.getId(), user.getId());
//            System.out.println(user.getName() + " 등록 완료");
//        }
//        System.out.println(fileChannelController.findAll());
//    }
//
//    private static void deleteChannel(Channel deletingChannel) {
//        System.out.println("==============삭제 테스트 시작==============");
//
//        Set<Channel> beforeChannels = fileChannelController.findAll();
//        System.out.println("삭제 전 채널 목록: " + beforeChannels + ", 채널 수: " + beforeChannels.size());
//        fileChannelController.delete(deletingChannel.getId());
//        Set<Channel> afterChannels = fileChannelController.findAll();
//        System.out.println("삭제 후 채널 목록: " + afterChannels + ", 채널 수: " + afterChannels.size());
//    }
//
//    private static Channel updateChannelName(Channel beforeUpdateChannel, String newName) {
//        Channel afterUpdateChannel = fileChannelController.updateChannelName(beforeUpdateChannel.getId(), newName);
//        System.out.println("업데이트 전 : " + beforeUpdateChannel);
//        System.out.println("업데이트 후 : " + afterUpdateChannel); // 아이디 같음
//        return afterUpdateChannel;
//    }
//
//
//    private static List<Channel> createChannel(int channelCount) {
//        System.out.println("==============생성 테스트 시작==============");
//        System.out.println("==============Channel 생성 수 : " + channelCount + "==============");
//        Random random = new Random();
//        List<Channel> channels = new ArrayList<>();
//        for (int i = 0; i < channelCount; i++){
//            channels.add(fileChannelController.create("channel " + (i+1)));
//        }
//        System.out.println("channel 목록: " + channels + ", 채널 수: " + channels.size());
//        return channels;
//    }
//
//
//    private static Boolean findTest() {
//        System.out.println("==============조회 테스트 시작==============");
//        Set<User> users = fileUserController.findAll();
//
//        for (User user : users) {
//            User testUser = fileUserController.findById(user.getId());
//            if (!testUser.equals(user)){
//                System.out.println("find 메서드 오류");
//                return false;
//            }
//        }
//        System.out.println("findUser 테스트 완료 : 전부 일치");
//        return true;
//    }
//
//    private static void deleteTest(User deletingUser) {
//        System.out.println("==============삭제 테스트 시작==============");
//        Set<User> beforeUsers = fileUserController.findAll();
//        System.out.println("삭제 전 유저 목록: " + beforeUsers + "유저 수: " + beforeUsers.size());
//        fileUserController.deleteUser(deletingUser.getId(), deletingUser.getName(), deletingUser.getPassword());
//        Set<User> afterUsers = fileUserController.findAll();
//        System.out.println("삭제 후 유저 목록: " + afterUsers + "유저 수: " + afterUsers.size());
//    }
//
//    private static User updateTest(User beforeUpdateUser) {
//        System.out.println("==============수정 테스트 시작==============");
//        fileUserController.updateUserNamePW(beforeUpdateUser.getId(), "user 2-1", "1234");
//        User updateUser = fileUserController.findById(beforeUpdateUser.getId());
//        System.out.println("업데이트 전 : " + beforeUpdateUser);
//        System.out.println("업데이트 후 : " + updateUser); // 아이디 같음
//        return updateUser;
//    }
//
//
//    private static List<User> createUser(int userCount) {
//        System.out.println("==============생성 테스트 시작==============");
//        System.out.println("==============USER 생성 수 : " + userCount + "==============");
//        Random random = new Random();
//        List<User> users = new ArrayList<>();
//        for (int i = 0; i < userCount; i++){
//            int randomPassword = 1000 + random.nextInt(9000);
//            users.add(fileUserController.create("user " + (i+1), String.valueOf(randomPassword)));
//        }
//        System.out.println("user 목록: " + users + "유저 수: " + users.size());
//        return users;
//    }
//
//
//    private static void initDirectory(){
//        System.out.println("==============디렉토리 초기화==============");
//        fileChannelController.createChannelDirectory();
//        System.out.println("Channel 디렉토리 추가 완료");
//        fileUserController.createUserDirectory();
//        System.out.println("User 디렉토리 추가 완료");
//        fileMessageController.createMessageDirectory();
//        System.out.println("Message 디렉토리 추가 완료");
//    }
//}
