package com.sprint.mission.discodeit.service.jcf;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import java.util.HashMap;
import com.sprint.mission.discodeit.io.InputHandler;


public class JCFUserService implements UserService {
    //Scanner sc = new Scanner(System.in);
    // HashMap(nickname - user)
    private final HashMap<String, User> Users;
    // mocking 이용으로 추가
    private InputHandler inputHandler;

    public JCFUserService(InputHandler inputHandler){
        // 생성자에서 users 데이터 초기화
        this.Users = new HashMap<>();
        // mocking 이용으로 추가
        this.inputHandler = inputHandler;
    }

    // mocking 이용으로 추가
    public void setInputHandler(InputHandler inputHandler) {
        this.inputHandler = inputHandler;
    }

    // 의존성 주입을 위한 user 반환
    public User getUser(String nickname){
        return Users.get(nickname);
    }

    public void Create(String nickname){
        User user = new User(nickname);
        Users.put(nickname, user);
    }

    public int ReadAll(){
        // 전체 유저 조희
        if (Users.isEmpty()) {
            System.out.println("No users exists.\n");
        }else{
            System.out.println("All Users : " + Users.keySet());
        }
        return Users.size();
    }
    public String ReadUser(String nickname){
        if(Users.get(nickname) == null ){
            System.out.println(nickname + " does not exist\n");
        }else{
            System.out.println("User name is " + Users.get(nickname).getNickname());
            System.out.println("You created this account at: " + Users.get(nickname).getCreatedAt());
        }
        return Users.get(nickname).getNickname();
    }

    public void UpdateNickname(String nickname){
        // nickname 을 key로 설정했어서
        // delete 과정을 거친 후 생성
        //String newNickname = sc.next();
        String newNickname = inputHandler.getNewInput();
        Users.put(newNickname, Users.get(nickname));
        Users.get(newNickname).setNickname(newNickname);
        Users.remove(nickname);
        // 수정 시간 업데이트를 위해
        Users.get(newNickname).setUpdateAt((System.currentTimeMillis()));
    }

    public void DeleteAll(){
        //String keyword = sc.next().toLowerCase();
        String keyword = inputHandler.getYesNOInput().toLowerCase();
        if(keyword.equals("y")){
            Users.clear();
        }
    }
    public void DeleteUser(String nickname){
        //String keyword = sc.next().toLowerCase();
        String keyword = inputHandler.getYesNOInput().toLowerCase();
        if(keyword.equals("y")){
            Users.remove(nickname);
        }
    }
}
