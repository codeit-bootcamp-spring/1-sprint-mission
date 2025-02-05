package discodeit.validator;

import java.util.regex.Pattern;

public class UserValidator implements Validator {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
    private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile("^\\d{3}-\\d{3,4}-\\d{4}$");

    public void validate(String name, String email, String phoneNumber) {
        validateName(name);
        validateEmail(email);
        validatePhoneNumber(phoneNumber);
    }

    public void validateName(String name) {
        isBlank(name);
    }

    public void validateEmail(String email) {
        isBlank(email);
        checkEmailFormat(email);
    }

    public void validatePhoneNumber(String phoneNumber) {
        isBlank(phoneNumber);
        checkPhoneNumberFormat(phoneNumber);
    }

    public void checkEmailFormat(String email) {
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("[ERROR] 잘못된 이메일 형식입니다.");
        }
    }

    public void checkPhoneNumberFormat(String phoneNumber) {
        if (!PHONE_NUMBER_PATTERN.matcher(phoneNumber).matches()) {
            throw new IllegalArgumentException("[ERROR] 잘못된 핸드폰 번호 형식입니다.");
        }
    }
}
