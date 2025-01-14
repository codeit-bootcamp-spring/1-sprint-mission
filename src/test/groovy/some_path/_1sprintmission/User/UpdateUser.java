package some_path._1sprintmission.User;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import some_path._1sprintmission.discodeit.entiry.User;
import some_path._1sprintmission.discodeit.entiry.enums.DiscodeStatus;
import some_path._1sprintmission.discodeit.service.jcf.JCFUserService;

@SpringBootApplication
public class UpdateUser {
    public static void main(String[] args) {
        JCFUserService userService = new JCFUserService();

        User user = new User("before", "before@gmail.com", "010-2342-5324");
        userService.createUser(user);
        userService.getUser(user.getId());

        //유저 정보 업데이트
        User user1 = new User("after", "after@gmail.com", "010-2342-5324");
        userService.updateUser(user.getId(), user1);
        userService.getUser(user.getId());

        //직접 set에 접근하여 변경하는 방법
        user.setVerified(Boolean.TRUE);
        user.setDiscodeStatus(DiscodeStatus.DEACTIVATE);
    }
}
