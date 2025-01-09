import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.JCFUserService;
import com.sprint.mission.discodeit.service.UserService;

public class Main {
    public static void main(String[] args){
        JCFUserService userService = new JCFUserService();

        User user1 = new User("성근", "leesin0114@naver.com", "password123");
        userService.registerUser(user1);




    }
}
