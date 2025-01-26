package discodeit.validator;

public interface Validator {

    default void isBlank(String input) {
        if (input == null || input.isBlank()) {
            throw new IllegalArgumentException("입력이 비어있습니다.");
        }
    }
}
