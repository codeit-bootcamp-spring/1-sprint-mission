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
                .hasMessage("[ERROR] 비밀번호 변경에 실패하였습니다.");
    }
}
