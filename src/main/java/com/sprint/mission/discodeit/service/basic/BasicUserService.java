package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Dto.ChannelDto;
import com.sprint.mission.discodeit.entity.Dto.UserDto;
import com.sprint.mission.discodeit.entity.Type.BinaryContentType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileIOHandler;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

    private final UserRepository userRepository;
    private final FileIOHandler fileIOHandler;
    private final BinaryContentRepository binaryContentRepository;
    private final UserStatusService userStatusService;
    private final ChannelService channelService;

    //유저 리턴
    @Override
    public UserDto findUserById(UUID userId) {
        if (userId == null || userRepository.isUserExistByUUID(userId)) { System.out.println("유저 반환 실패. 입력값을 확인해주세요."); return null;}
        return UserDto.from(userRepository.getUserById(userId));
    }

    //모든 유저 Dto 리스트로 만들어 리턴. 유저 존재하지 않으면 빈리스트 리턴.
    @Override
    public List<UserDto> findAllUsers() {
        HashMap<UUID, User> usersMap = userRepository.getUsersMap();
        if (usersMap.isEmpty()){return Collections.emptyList();}
        return usersMap.values().stream().map(user -> UserDto.from(user)).collect(Collectors.toList());
    }

    //유저 생성.
    @Override
    public UUID createUser(String userName, String email, String password) {
        if (userName == null || email == null || password == null) {System.out.println("유저 생성 실패. 입력값이 null 입니다."); return null;}
        if (userRepository.isUserExistByName(userName) == true) { System.out.println("동일한 이름이 존재합니다."); return null; }
        if (userRepository.isUserExistByEmail(email) == true) { System.out.println("동일한 이메일이 존재합니다."); return null; }
        User newUser = new User(userName, email, password);
        userStatusService.createUserStatus(newUser.getId());
        if (userRepository.saveUser(newUser)==false) { System.out.println("유저 생성 실패"); return null; }
        System.out.println(userName + " 유저 생성 성공!");
        return newUser.getId();
    }

    //todo 바이너리 서비스 구현시 수정필요!!!!!!!!!!
    //유저 생성과 동시에 프로필사진 등록.
    @Override
    public UUID createUser(String userName, String email,String password, String profilePicturePath) {
        if (userName == null || email == null || password == null || profilePicturePath == null) {
            System.out.println("유저 생성 실패. 입력값을 확인해주세요.");
            return null;
        }
        User newUser = new User(userName, email, password);
        UserStatus newUserStatus = userStatusService.createUserStatus(newUser.getId());
        //OI핸들러에 경로넘겨주고 그 경로에 있던 이미지 받아오기
        BufferedImage profilePicture = fileIOHandler.loadImage(profilePicturePath);
        //받아온 이미지를 바이너리객체로 만들기
        BinaryContent binaryProfilePicture = new BinaryContent(newUser.getId(), BinaryContentType.Profile_Picture, profilePicture);
        //프로필사진 바이너리객체를 바이너리레포지토리에 저장.
        binaryContentRepository.saveBinaryContent(binaryProfilePicture);
        //새로만든 유저객체에 프로필사진 등록
        newUser.setProfilePicture(binaryProfilePicture.getId());
        //유저 객체 레포지토리에 저장
        if (userRepository.saveUser(newUser)==false) { System.out.println("유저 생성 실패"); return null; }
        System.out.println(userName + " 유저 생성 성공!");
        return newUser.getId();
    }

    //todo 여기도 수정필요!!!!!!!
    //UUID를 통해 유저 객체를 찾아 삭제. 성공여부 리턴
    @Override
    public boolean deleteUser(UUID userId){
        if (userId == null || userRepository.isUserExistByUUID(userId)==false){ System.out.println("유저 삭제 실패. 입력값을 확인해주세요."); return false; }
        User user = userRepository.getUserById(userId);
        List<ChannelDto> channelsUserIn = channelService.findAllByUserId(userId);
        if (channelsUserIn.size()>0){channelsUserIn.forEach(channel -> {channelService.deleteMember(channel.id(),userId);});}
        //todo 아직 바이너리콘텐트/유저스테이터스 레포지토리 구현안돼있어서 주석처리.
        //if (userRepository.deleteUser(userId) == false || binaryRepository.delete(user.id())==false || userStatusRepository.(user.id())==false) { System.out.println("유저 삭제 과정 중 문제 발생"); return false; }
        System.out.println("유저 삭제 성공!");
        return true;
    }

    //유저명 변경. 성공여부 반환
    @Override
    public boolean changeUserName(UUID userId, String newName) {
        if (userId == null || newName == null){ System.out.println("유저 이름 변경 실패. 입력값을 확인해주세요."); return false; }
        User user = userRepository.getUserById(userId);
        user.setUserName(newName);
        if (userRepository.saveUser(user) == false) { System.out.println("유저 이름 변경 실패"); return false; }
        System.out.println("유저 이름 변경 성공!");
        return true;
    }
    //todo 여기도 수정필요!!!!!!!
    //프로필사진 변경
    @Override
    public boolean changeProfilePicture(UUID userId, String profilePicturePath){
        if (userId == null || profilePicturePath == null || userRepository.isUserExistByUUID(userId)==false){
            System.out.println("프로필사진 변경 실패. 입력값을 확인해주세요.");
            return false;
        }
        try {
            //OI핸들러에 경로넘겨주고 그 경로에 있던 이미지 받아오기
            BufferedImage profilePicture = fileIOHandler.loadImage(profilePicturePath);
            //받아온 이미지를 바이너리객체로 만들기
            BinaryContent uploadedProfilePicture = new BinaryContent(userId, BinaryContentType.Profile_Picture, profilePicture);
            //프로필사진 바이너리객체를 바이너리레포지토리에 저장.
            binaryContentRepository.saveBinaryContent(uploadedProfilePicture);
            //유저객체에 프로필사진 등록
            User user = userRepository.getUserById(userId);
            if (user.getProfilePictureId() != null) {
                binaryContentRepository.deleteBinaryContent(user.getProfilePictureId());
            }
            user.setProfilePicture(uploadedProfilePicture.getId());
            userRepository.saveUser(user);
            System.out.println(user.getUserName() + " 프로필사진 변경 성공!");
            return true;
        }catch (Exception e){
            System.err.println(e.getMessage());
            e.printStackTrace();
            System.out.println("프로필사진 변경 실패.");
            return false;
        }
    }
    //todo 여기도 수정필요!!!!!!!
    //프로필사진 삭제
    @Override
    public boolean deleteProfilePicture(UUID userId) {
        if (userId == null) {
            System.out.println("프로필사진 삭제 실패. 입력값을 확인해주세요.");
            return false;
        }
        try {
            User user = userRepository.getUserById(userId);
            UUID profilePictureId = user.getProfilePictureId();
            //원래 프사 없었으면 걍 true 반환.
            if (profilePictureId == null) {
                return true;
            }
            user.setProfilePicture(null);
            userRepository.saveUser(user);
            binaryContentRepository.deleteBinaryContent(profilePictureId);
            System.out.println(user.getUserName() + " 프로필사진 삭제 성공!");
            return true;
        } catch (Exception e){
            System.err.println(e.getMessage());
            e.printStackTrace();
            System.out.println("프로필사진 삭제 실패.");
            return false;
        }
    }

    //todo AuthService 클래스 위치 여기에 둬도 되나?
    public class AuthService{
        public boolean login(String username, String password) {
            if (username.isBlank()||password.isBlank()){
                return false;
            }
            try{
                //false일 요인이 있다면 어차피 앞에서 예외를 던질것이기때문에 validateUserToLogin의 반환값이 true인지 검증하지 않았음.
                userRepository.validateUserToLogin(username, password);
                System.out.println("로그인 성공!");
                //todo 반환값으로 유저객체나 뭔가 로그인정보를 반환해야될것같은데 지금으로서는 뭘 반환해야할지 확신이안가서 그냥 불린 리턴
                return true;
            } catch (Exception e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
                System.out.println("로그인 실패.");
                return false;
            }
        }
    }



}




