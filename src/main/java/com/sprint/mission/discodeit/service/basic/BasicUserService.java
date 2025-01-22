package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.io.InputHandler;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.UUID;

public class BasicUserService implements UserService {

    /**
     * [x ] 기존에 구현한 서비스 구현체의 "비즈니스 로직"과 관련된 코드를 참고하여 구현하세요.
     * => JCF랑 동일하게 작성했습니다
     * [x ] 필요한 Repository 인터페이스를 필드로 선언하고 생성자를 통해 초기화하세요.
     * [x ] "저장 로직"은 Repository 인터페이스 필드를 활용하세요. (직접 구현하지 마세요.)
     **/


    private final UserRepository userRepository;
    // mocking 이용으로 추가
    private InputHandler inputHandler;

    public BasicUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.inputHandler = inputHandler;
    }

    @Override
    public UUID createUser(String nickname) {
        User user = new User(nickname);
        // 인터페이스 구현체의 메서드 saveUser(User user) 이용
        userRepository.saveUser(user);
        return user.getId();
    }

    @Override
    public int showAllUsers() {
        // 전체 유저 조희
        if (userRepository.getAllUsers().isEmpty()) {
            System.out.println("No users exists.\n");
        }else{
            System.out.println(userRepository.getAllUsers().toString());
        }
        return userRepository.getAllUsers().size();
    }

    @Override
    public User getUserById(UUID id) {
        if(userRepository.getAllUsers().get(id) == null ){
            System.out.println(userRepository.getAllUsers().get(id) + " does not exist\n");
        }else{
            System.out.println( userRepository.getAllUsers().get(id).toString());
        }
        return userRepository.getAllUsers().get(id);
    }

    @Override
    public void updateUserNickname(UUID id) {
        String newNickname = inputHandler.getNewInput();
        userRepository.findUserById(id).setNickname(newNickname);
        // 수정 시간 업데이트를 위해
        userRepository.findUserById(id).setUpdateAt(System.currentTimeMillis());
    }

    @Override
    public void clearAllUsers() {
        String keyword = inputHandler.getYesNOInput().toLowerCase();
        if(keyword.equals("y")){
            userRepository.deleteAllUsers();
        }
    }

    @Override
    public void removeUserById(UUID id) {
        String keyword = inputHandler.getYesNOInput().toLowerCase();
        if(keyword.equals("y")){
            userRepository.deleteUserById(id);
        }
    }
}
