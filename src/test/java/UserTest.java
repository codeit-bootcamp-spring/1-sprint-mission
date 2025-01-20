import com.sprint.mission.discodeit.dto.UserUpdateDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.UserValidationException;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;
import com.sprint.mission.discodeit.util.PasswordEncryptor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserTest {
  @Test
    void run() throws UserValidationException {
        String username = "kiki1875";
        String pwd = "123456789";


        User user = new User.UserBuilder(username, pwd,"kevinheo0413@gmail.com").build();

        assertThat(user.getUsername()).isEqualTo(username);
    }
}
//}
//
//
//    @BeforeEach
//    void beforeEach(){
//        JCFUserService.getInstance().data.clear();
//    }
//
//    @DisplayName("사용자 객체 를 생성할 수 있다")
//    @Test
//    void 사용자_생성(){
//        String username = "kiki1875";
//        String pwd = "123456789";
//        String email = "kevinheo0413@gmail.com";
//        String nickname = "허지웅";
//        String phoneNumber = "01012345678";
//        String profileURL = "profileURL.com";
//        String description = "this is description";
//
//        User user = new User(
//            username,
//            pwd,
//            email,
//            nickname,
//            phoneNumber,
//            profileURL,
//            description
//        );
//
//        Assertions.assertThat(user.getNickname()).isEqualTo(nickname);
//    }
//
//    @DisplayName("비밀번호를 암호화할 수 있다")
//    @Test
//    void 비밀번호_암호화(){
//        String pwd = "123123123";
//        String hashedPwd = PasswordEncryptor.hashPassword(pwd);
//
//        Assertions.assertThat(PasswordEncryptor.checkPassword(pwd, hashedPwd)).isEqualTo(true);
//    }
//
//    @DisplayName("사용자를 저장할 수 있다")
//    @Test
//    void 사용자_저장(){
//        String username = "kiki1875";
//        String pwd = "123456789";
//        String email = "kevinheo0413@gmail.com";
//        String nickname = "허지웅";
//        String phoneNumber = "01012345678";
//        String profileURL = "profileURL.com";
//        String description = "this is description";
//
//        User user = new User(
//            username,
//            pwd,
//            email,
//            nickname,
//            phoneNumber,
//            profileURL,
//            description
//        );
//
//
//        User user2 = JCFUserService.getInstance().createUser(user);
//        Assertions.assertThat(user).isEqualTo(user2);
//    }
//
//    @DisplayName("ID를 통해 유저를 불러올 수 있다")
//    @Test
//    void ID를_통해_유저를_불러올_수_있다(){
//        String username = "kiki1875";
//        String pwd = "123456789";
//        String email = "kevinheo0413@gmail.com";
//        String nickname = "허지웅";
//        String phoneNumber = "01012345678";
//        String profileURL = "profileURL.com";
//        String description = "this is description";
//
//
//
//        User user = JCFUserService.getInstance().createUser(new User(
//            username,
//            pwd,
//            email,
//            nickname,
//            phoneNumber,
//            profileURL,
//            description
//        ));
//
//        Optional<User> user2 = JCFUserService.getInstance().readUserById(user.getUUID());
//
//        Assertions.assertThat(user).isEqualTo(user2.get());
//    }
//
//    @DisplayName("사용자 리스트를 불러올 수 있다")
//    @Test
//    void 사용자_리스트를_불러올_수_있다(){
//        String username = "kiki1875";
//        String pwd = "123456789";
//        String email = "kevinheo0413@gmail.com";
//        String nickname = "허지웅";
//        String phoneNumber = "01012345678";
//        String profileURL = "profileURL.com";
//        String description = "this is description";
//
//        String username2 = "kiki1875b";
//        String pwd2 = "123456789";
//        String email2 = "kevinheo04134@gmail.com";
//        String nickname2 = "허지웅";
//        String phoneNumber2 = "010123456781";
//        String profileURL2 = "profileURL.com";
//        String description2 = "this is description";
//
//        JCFUserService.getInstance().createUser(new User(
//            username, pwd, email, nickname, phoneNumber, profileURL, description
//        ));
//        JCFUserService.getInstance().createUser(new User(
//            username2, pwd2, email2, nickname2, phoneNumber2, profileURL2, description2
//        ));
//
//        Assertions.assertThat(JCFUserService.getInstance().readAllUsers()).hasSize(2);
//    }
//
//    @DisplayName("중복된 전화번호는 가입할 수 없다")
//    @Test()
//    void 중복_전화번호_가입(){
//        String username = "kiki1875";
//        String pwd = "123456789";
//        String email = "kevinheo0413@gmail.com";
//        String nickname = "허지웅";
//        String phoneNumber = "01012345678";
//        String profileURL = "profileURL.com";
//        String description = "this is description";
//
//        String username2 = "kiki1875b";
//        String pwd2 = "123456789";
//        String email2 = "kevinheo04134@gmail.com";
//        String nickname2 = "허지웅";
//        String phoneNumber2 = "01012345678";
//        String profileURL2 = "profileURL.com";
//        String description2 = "this is description";
//
//        JCFUserService.getInstance().createUser(new User(
//            username, pwd, email, nickname, phoneNumber, profileURL, description
//        ));
//
//        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
//            JCFUserService.getInstance().createUser(new User(username2, pwd2, email2, nickname2, phoneNumber2, profileURL2, description2));
//        });
//
//        assertEquals("중복된 전화번호 입니다.", exception.getMessage());
//    }
//
//    @DisplayName("중복된 이메일은 가입할 수 없다")
//    @Test
//    void 중복된_이메일은_가입할_수_없다(){
//        String username = "kiki1875";
//        String pwd = "123456789";
//        String email = "kevinheo0413@gmail.com";
//        String nickname = "허지웅";
//        String phoneNumber = "01012345678";
//        String profileURL = "profileURL.com";
//        String description = "this is description";
//
//        String username2 = "kiki1875b";
//        String pwd2 = "123456789";
//        String email2 = "kevinheo0413@gmail.com";
//        String nickname2 = "허지웅";
//        String phoneNumber2 = "0101234567811";
//        String profileURL2 = "profileURL.com";
//        String description2 = "this is description";
//
//        JCFUserService.getInstance().createUser(new User(
//            username, pwd, email, nickname, phoneNumber, profileURL, description
//        ));
//
//        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
//            JCFUserService.getInstance().createUser(new User(username2, pwd2, email2, nickname2, phoneNumber2, profileURL2, description2));
//        });
//
//        assertEquals("중복된 이메일 입니다.", exception.getMessage());
//    }
//
//    @DisplayName("사용자 정보를 업데이트 할 수 있다.")
//    @Test
//    void 사용자_정보를_업데이트_할_수_있다(){
//        String username = "kiki1875";
//        String pwd = "123456789";
//        String email = "kevinheo0413@gmail.com";
//        String nickname = "허지웅";
//        String phoneNumber = "01012345678";
//        String profileURL = "profileURL.com";
//        String description = "this is description";
//
//        String username2 = "kiki1875b";
//        String pwd2 = "123456789";
//        String email2 = "kevinheo04134@gmail.com";
//        String nickname2 = "허지웅";
//        String phoneNumber2 = "010123456781";
//        String profileURL2 = "profileURL.com";
//        String description2 = "this is description";
//
//        User user = JCFUserService.getInstance().createUser(new User(
//            username, pwd, email, nickname, phoneNumber, profileURL, description
//        ));
//
//        JCFUserService.getInstance().updateUser(user.getUUID(), new UserUpdateDto(
//            Optional.ofNullable(username2),
//            Optional.ofNullable(pwd2),
//            Optional.ofNullable(email2),
//            Optional.ofNullable(nickname2),
//            Optional.ofNullable(phoneNumber2),
//            Optional.ofNullable(profileURL2),
//            Optional.ofNullable(description2)
//        ), "123456789");
//
//        Assertions.assertThat(user.getUUID()).isEqualTo(
//            JCFUserService.getInstance().readUserById(user.getUUID()).get().getUUID());
//        Assertions.assertThat(JCFUserService.getInstance().readUserById(user.getUUID()).get().getPhoneNumber()).isEqualTo(phoneNumber2);
//    }
//
//    @DisplayName("비밀번호가 틀리면 업데이트를 할 수 없다")
//    @Test
//    void 비밀번호가_틀리면_업데이트를_할수_없다(){
//        String username = "kiki1875";
//        String pwd = "123456789";
//        String email = "kevinheo0413@gmail.com";
//        String nickname = "허지웅";
//        String phoneNumber = "01012345678";
//        String profileURL = "profileURL.com";
//        String description = "this is description";
//
//        String username2 = "kiki1875b";
//        String pwd2 = "123456789";
//        String email2 = "kevinheo04134@gmail.com";
//        String nickname2 = "허지웅";
//        String phoneNumber2 = "010123456781";
//        String profileURL2 = "profileURL.com";
//        String description2 = "this is description";
//
//        User user = JCFUserService.getInstance().createUser(new User(
//            username, pwd, email, nickname, phoneNumber, profileURL, description
//        ));
//
//        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, ()->
//            JCFUserService.getInstance().updateUser(user.getUUID(), new UserUpdateDto(
//                Optional.ofNullable(username2),
//                Optional.ofNullable(pwd2),
//                Optional.ofNullable(email2),
//                Optional.ofNullable(nickname2),
//                Optional.ofNullable(phoneNumber2),
//                Optional.ofNullable(profileURL2),
//                Optional.ofNullable(description2)), "12345116789")
//        );
//
//        Assertions.assertThat(exception).hasMessageContaining("비밀번호");
//    }
//}
