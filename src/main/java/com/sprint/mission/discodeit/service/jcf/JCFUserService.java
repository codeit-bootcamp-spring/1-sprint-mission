package com.sprint.mission.discodeit.service.jcf;
import com.sprint.mission.discodeit.entity.BaseEntity;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.UUID;

import com.sprint.mission.discodeit.io.InputHandler;


public class JCFUserService implements UserService {
    // Scanner sc = new Scanner(System.in);
    // private final HashMap<String, User> Users;

    private final UserRepository userRepository;

    // mocking 이용으로 추가
    private InputHandler inputHandler;

    public JCFUserService(UserRepository userRepository, InputHandler inputHandler){
        // 생성자에서 users 데이터 초기화
        this.userRepository = userRepository;
        // mocking 이용으로 추가
        this.inputHandler = inputHandler;
    }

    // mocking 이용으로 추가
    public void setInputHandler(InputHandler inputHandler) {
        this.inputHandler = inputHandler;
    }

    public UUID createUser(String nickname){
        User user = new User(nickname);
        // 인터페이스 구현체의 메서드 saveUser(User user) 이용
        userRepository.saveUser(user);
        return user.getId();
    }

    public int showAllUsers(){
        // 전체 유저 조희
        if (userRepository.getAllUsers().isEmpty()) {
            System.out.println("No users exists.\n");
        }else{
            System.out.println(userRepository.getAllUsers().toString());
        }
        return userRepository.getAllUsers().size();
    }

    public User getUserById(UUID id){
        return userRepository.findUserById(id)
                .orElseGet( () -> {
                    System.out.println(" No  + " + id.toString() + " User exists.\n");
                    return null;
                });
    }

    public void updateUserNickname(UUID id){
        String newNickname = inputHandler.getNewInput();
        userRepository.findUserById(id).ifPresent( user -> user.setNickname(newNickname));
        // 수정 시간 업데이트를 위해
        userRepository.findUserById(id).ifPresent(BaseEntity::refreshUpdateAt);
    }

    public void clearAllUsers(){
        String keyword = inputHandler.getYesNOInput().toLowerCase();
        if(keyword.equals("y")){
            userRepository.deleteAllUsers();
        }
    }
    public void removeUserById(UUID id){
        String keyword = inputHandler.getYesNOInput().toLowerCase();
        if(keyword.equals("y")){
            userRepository.deleteUserById(id);
        }
    }
}
