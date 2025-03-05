//package com.sprint.mission.discodeit.service.currentUse;
//
//import com.sprint.mission.discodeit.dto.UserDto;
//import com.sprint.mission.discodeit.entity.User;
//import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
//import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
//import com.sprint.mission.discodeit.repository.file.FileUserRepository;
//import com.sprint.mission.discodeit.service.interfaces.UserServiceInterface;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//
//@RequiredArgsConstructor
//@Service
//public class UserService implements UserServiceInterface {
//    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy년 MM월 dd일 hh:mm:ss");
//    private UserStatusService statusService;
//    FileUserRepository userRepository;
//    private FileChannelRepository channelRepository;
//    private FileMessageRepository messageRepository;
//
//    @Override
//    public User creatUser(UserDto userDto) throws IOException, ClassNotFoundException {
//        statusService.create(userDto);
//        return userRepository.createUser(userDto);
//    }
//
//    @Override
//    public void modifyUser(UserDto user) throws IOException, ClassNotFoundException {
//        if (userRepository.checkUserId(user.getId())) {
//            userRepository.update(user.getId(), user.getNewName());
//            UserDto dto = new UserDto();
//            dto.setId(user.getId());
//            statusService.updateByUserId(dto);
//            channelRepository.checkJoinChannel(user.getId(), user.getNewName());
//            messageRepository.modifyUserName(user.getId(), user.getNewName());
//            System.out.println("성공적으로 수정하였습니다");
//            return;
//        }
//
//        System.out.println("존재하지 않는 계정입니다");
//    }
//
//    @Override
//    public void deleteUser(UserDto user) throws IOException, ClassNotFoundException {
//        if (userRepository.checkUserId(user.getId())) {
//            userRepository.deleteUser(user.getId());
//            statusService.delete(user);
//            channelRepository.deleteUser(user.getId());
//            messageRepository.modifyUserName(user.getId(),"UNDEFINED");
//            System.out.println("성공적으로 삭제하였습니다");
//            return;
//        }
//
//        System.out.println("존재하지 않는 계정입니다");
//    }
//
//    @Override
//    public void showInfoUser(UserDto userDto) throws IOException, ClassNotFoundException {
//        User user = userRepository.find(userDto);
//        System.out.println(user.getName() + "(" + user.getId() + ")"
//                + "\n생성 일자 : " + format.format(user.getCreatedAt())
//                + "\n최종 수정 일자 : " + format.format(user.getUpdatedAt()));
//    }
//}
