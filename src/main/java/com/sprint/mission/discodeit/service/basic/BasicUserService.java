package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Dto.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

    private final UserRepository userRepository;

    //todo find, findAll 메서드가 이런 용도인게 맞나?..
    //유저 리턴
    @Override
    public UserDto findUserById(UUID userId) {
        if (userId == null) {
            System.err.println("유저 id가 null인 상태입니다. 입력값을 확인해주세요.");
            return null;
        }
        try {
            UserDto userDto = UserDto.from(userRepository.getUserById(userId));
            return userDto;
        } catch (Exception e){
            System.err.println(e.getMessage());
            e.printStackTrace();
            System.err.println("유저 불러오기 실패");
            return null;
        }
    }

    //모든 유저 Dto 리스트로 만들어 리턴
    @Override
    public List<UserDto> findAllUsers() {
        try {
            return userRepository.getUsersMap().values().stream().map(user -> UserDto.from(user)).collect(Collectors.toList());
        } catch (Exception e){
            System.err.println(e.getMessage());
            e.printStackTrace();
            System.err.println("유저 불러오기 실패");
            return null;
        }
    }

    //유저 이름 리턴
    @Override
    public String getUserNameById(UUID userId) {
        if (userId == null){
            System.err.println("유저 id가 null인 상태입니다. 입력값을 확인해주세요.");
            return null;
        }
        try {
            return findUserById(userId).userName();
        } catch (Exception e){
            System.err.println(e.getMessage());
            e.printStackTrace();
            System.err.println("유저 이름 불러오기 실패");
            return null;
        }
    }

    //유저 생성.
    @Override
    public UUID createUser(String userName, String email, String password) {
        if (userName == null || email == null || password == null) {
            System.err.println("userName or email or password is null");
            return null;
        }
        // 이메일, 이름 중복검사하는 과정에서 예외를 던져놨기 때문에 try-catch 사용
        try {
            if (userRepository.isUserExistByName(userName) == true) {
                System.out.println("동일한 이름이 존재합니다.");
                return null;
            } else if (userRepository.isUserExistByEmail(email) == true) {
                System.out.println("동일한 이메일이 존재합니다.");
                return null;
            }
        } catch (Exception e){
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        try {
            UserStatus newUserStatus = new UserStatus();
            User newUser = new User(userName, email, password, newUserStatus.getId());
            userRepository.saveUser(newUser);
            //todo 유저스테이터스 레포지토리 구현 후 스테이터스 저장하기
            //userStatusRepository.saveUserStatus(newUserstatus);
            System.out.println(userName + " 유저 생성 성공!");
            return UserDto.from(newUser).id();
        } catch (Exception e){
            System.err.println(e.getMessage());
            e.printStackTrace();
            System.out.println("유저 생성 실패.");
            return null;
        }
    }

    //todo 아직 바이너리콘텐트레포지토리 구현안돼있어서 주석처리.(미션3에서 지금 구현하지 않기를 요구). 레포지토리 구현 후 수정필요.
    /*
    //유저 생성과 동시에 프로필사진 등록.
    @Override
    public UUID createUser(String userName, String email,String password, String profilePicturePath) {
        if (userName == null || email == null || password == null || profilePicturePath == null) {
            System.out.println("유저 생성 실패. 입력값을 확인해주세요.");
            return null;
        }

        User newUser = new User(userName, email, password);
        //OI핸들러에 경로넘겨주고 그 경로에 있던 이미지 받아오기
        BufferedImage profilePicture = fileIOHandler.loadImage(profilePicturePath);
        //받아온 이미지를 바이너리객체로 만들기
        BinaryContent uploadedProfilePicture = new BinaryContent(newUser.getId(), BinaryContentType.Profile_Picture, profilePicture);
        //프로필사진 바이너리객체를 바이너리레포지토리에 저장.
        BinaryContentRepository.addBinaryContent(BinaryContentType.Profile_Picture, uploadedProfilePicture);
        //새로만든 유저객체에 프로필사진 등록
        newUser.setProfilePicture(uploadedProfilePicture.getId());
        //유저 객체 레포지토리에 저장
        userRepository.addUser(newUser);
        System.out.println(userName + " 유저 생성 성공!");
        return newUser.getId();
    }
    */

    //UUID를 통해 유저 객체를 찾아 삭제. 성공여부 리턴
    @Override
    public boolean deleteUser(UUID userId){
        if (userId == null){
            System.out.println("유저 삭제 실패. 입력값을 확인해주세요.");
            return false;
        }
        try {
            UserDto user = findUserById(userId);
            //todo 아직 바이너리콘텐트/유저스테이터스 레포지토리 구현안돼있어서 주석처리.
            //binaryRepository.delete(user.id());
            //UserStatusRepository.delete(user.id());
            userRepository.deleteUser(userId);
            System.out.println("유저 삭제 성공!");
            return true;
        } catch (Exception e){
            System.err.println(e.getMessage());
            e.printStackTrace();
            System.out.println("유저 삭제 실패.");
            return false;
        }

    }

    //유저명 변경. 성공여부 반환
    @Override
    public boolean changeUserName(UUID userId, String newName) {
        if (userId == null || newName == null){
            System.out.println("유저 이름 변경 실패. 입력값을 확인해주세요.");
            return false;
        }
        try {
            User user = userRepository.getUserById(userId);
            user.setUserName(newName);
            userRepository.saveUser(user);
            System.out.println("유저 이름 변경 성공!");
            return true;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            System.out.println("유저 이름 변경 실패.");
            return false;
        }
    }



    //todo 아직 바이너리콘텐트레포지토리 구현안돼있어서 주석처리. (미션3에서 지금 구현하지 않기를 요구). userDto에 updateDto 작성해놓음.
    /*
    //프로필사진 변경
    @Override
    public boolean changeProfilePicture(UUID userId, String profilePicturePath){
        if (userId == null || profilePicturePath == null || userRepository.isUserExist(userId)==false){
            System.out.println("프로필사진 변경 실패. 입력값을 확인해주세요.");
            return false;
        }
            //OI핸들러에 경로넘겨주고 그 경로에 있던 이미지 받아오기
            BufferedImage profilePicture = fileIOHandler.loadImage(profilePicturePath);
            //받아온 이미지를 바이너리객체로 만들기
            BinaryContent uploadedProfilePicture = new BinaryContent(userId, BinaryContentType.Profile_Picture, profilePicture);
            //프로필사진 바이너리객체를 바이너리레포지토리에 저장.
            BinaryContentRepository.saveBinaryContent(BinaryContentType.Profile_Picture, uploadedProfilePicture);
            //유저객체에 프로필사진 등록
            User user = userRepository.getUser(userId)
            user.setProfilePicture(uploadedProfilePicture.getId());
            userRepository.saveUser(user)
            System.out.println(user.getUserName() + " 프로필사진 변경 성공!");
            return true;
        }


    //프로필사진 삭제
    @Override
    public boolean deleteProfilePicture(UUID userId) {
        if (userId == null) {
            return false;
        }
        try {
            User user = userRepository.getUserById(userId);
            UUID profilePictureId = user.getProfilePictureId();
            user.setProfilePicture(null);
            userRepository.saveUser(user);
            BinaryContentRepository.deleteBinaryContent(profilePictureId);
            System.out.println(user.getUserName() + " 프로필사진 삭제 성공!");
            return true;
        } catch (Exception e){
            System.err.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    */

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




