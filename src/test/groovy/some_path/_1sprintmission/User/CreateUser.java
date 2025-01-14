package some_path._1sprintmission.User;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import some_path._1sprintmission.discodeit.entiry.User;
import some_path._1sprintmission.discodeit.service.jcf.JCFUserService;

@SpringBootApplication
public class CreateUser {
    public static void main(String[] args) {
        JCFUserService userService = new JCFUserService();

        //정상작동
        User user = new User("David", "David@gmail.com", "010-9948-3849");

        //이메일 형식 에러
        //User user1 = new User("Summer", "summergmail.com", "010-9948-3849");

        //번호 형식 에러
        //User user2 = new User("hi", "hi@gmail.com", "010-9948-349");

    }
}
