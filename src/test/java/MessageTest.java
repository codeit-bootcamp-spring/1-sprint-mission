import discodeit.entity.Message;
import discodeit.entity.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class MessageTest {
    @Test
    void 메시지_삭제_시_보낸_이_확인() {
        User user1 = new User("name", "email1@codeit.com", "010-1111-1111", "1234");
        User user2 = new User("name", "email2@codeit.com", "010-2222-2222", "1234");

        Message message = new Message("message", user1);

        assertThatThrownBy(() -> message.checkSender(user2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("자신의 메시지만 삭제할 수 있습니다.");
    }
}
