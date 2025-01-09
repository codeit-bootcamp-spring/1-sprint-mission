package sprint.mission.discodeit.exception;

public class InvalidFormatException extends InvalidException {
    public InvalidFormatException(ErrorCode errorCode) {
        super(errorCode);
    }
}