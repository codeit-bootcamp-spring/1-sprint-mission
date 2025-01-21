import discodeit.entity.User;
import discodeit.service.ServiceFactory;
import discodeit.service.UserService;
import discodeit.service.jcf.JCFServiceFactory;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class UserTest {

    @Test
    void 유저_비밀번호_정상_변경() {
        User user = new User("name", "email@codeit.com", "010-1111-1111", "1234");

        // 비밀번호 확인, 새로운 비밀번호
        user.updatePassword("1234", "123");
    }

    @Test
    void 유저_기존_비밀번호_잘못_입력() {
        User user = new User("name", "email@codeit.com", "010-1111-1111", "1234");

        // 비밀번호 확인, 새로운 비밀번호
        assertThatThrownBy(() -> user.updatePassword("123", "123"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("잘못된 비밀번호입니다.");
    }

    @Test
    void 중복_이메일_설정_시_오류() {
        ServiceFactory serviceFactory = JCFServiceFactory.getInstance();
        UserService jcfUserService = serviceFactory.getUserService();

        User user1 = jcfUserService.createUser("name", "email1@codeit.com", "010-1111-1111", "1234");
        User user2 = jcfUserService.createUser("name", "email2@codeit.com", "010-2222-2222", "1234");

        assertThatThrownBy(() -> jcfUserService.updateEmail(user2, "email1@codeit.com"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 존재하는 이메일입니다.");
    }

    @Test
    void 중복_전화번호_설정_시_오류() {
        ServiceFactory serviceFactory = JCFServiceFactory.getInstance();
        UserService jcfUserService = serviceFactory.getUserService();

        User user1 = jcfUserService.createUser("name", "email3@codeit.com", "010-3333-3333", "1234");
        User user2 = jcfUserService.createUser("name", "email4@codeit.com", "010-4444-4444", "1234");

        assertThatThrownBy(() -> jcfUserService.updatePhoneNumber(user2, "010-3333-3333"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 존재하는 번호입니다.");
    }
}
