package some_path._1sprintmission.discodeit.entiry.validation;

import java.util.regex.Pattern;

public class Phone {
    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "^010-\\d{4}-\\d{4}$" // 010-로 시작하고 4자리 숫자-4자리 숫자
    );

    private final String value;

    public Phone(String phone) {
        if (!PHONE_PATTERN.matcher(phone).matches()) {
            throw new IllegalArgumentException("하이픈을 추가해서 작성해주세요: " + phone);
        }
        this.value = phone;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
