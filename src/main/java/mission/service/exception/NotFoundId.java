package mission.service.exception;

public class NotFoundId extends NullPointerException {
    public NotFoundId(String message) {
        super(message);
    }

    public NotFoundId() {
        super("id를 잘못 입력했습니다.");
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return super.fillInStackTrace();
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
