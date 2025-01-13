package some_path._1sprintmission.discodeit.entiry.validation;

import java.util.regex.Pattern;

public class Email {
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$" // 간단한 이메일 형식
    );

    private final String value;

    public Email(String email) {
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("지원하지 않는 이메일 형식입니다: " + email);
        }
        this.value = email;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
