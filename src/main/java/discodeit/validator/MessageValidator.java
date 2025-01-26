package discodeit.validator;

public class MessageValidator implements Validator {

    public void validate(String content) {
        validateContent(content);
    }

    public void validateContent(String content) {
        isBlank(content);
    }
}
