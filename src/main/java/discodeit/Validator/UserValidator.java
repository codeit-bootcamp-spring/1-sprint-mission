package discodeit.Validator;

public class UserValidator implements Validator {

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
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        if (!email.matches(emailRegex)) {
            throw new IllegalArgumentException("잘못된 이메일 형식입니다.");
        }
    }

    public void checkPhoneNumberFormat(String phoneNumber) {
        String phoneNumberRegex = "^\\d{3}-\\d{3,4}-\\d{4}$";
        if (!phoneNumber.matches(phoneNumber)) {
            throw new IllegalArgumentException("잘못된 핸드폰 번호 형식입니다.");
        }
    }
}
