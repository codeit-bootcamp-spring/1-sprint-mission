import discodeit.validator.UserValidator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;


public class ValidatorTest {

    @Test
    void 비어있는_입력() {
        UserValidator userValidator = new UserValidator();

        assertThatThrownBy(() -> userValidator.isBlank(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 입력이 비어있습니다.");
    }

    @Test
    void 잘못된_이메일_형식() {
        UserValidator userValidator = new UserValidator();

        assertThatThrownBy(() -> userValidator.validateEmail("user.codeit.com"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 잘못된 이메일 형식입니다.");
    }

    @Test
    void 잘못된_핸드폰_번호_형식() {
        UserValidator userValidator = new UserValidator();

        assertThatThrownBy(() -> userValidator.validatePhoneNumber("01012345678"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 잘못된 핸드폰 번호 형식입니다.");
    }
}
