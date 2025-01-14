package mission.service.exception;

public class DuplicateName extends RuntimeException {
  public DuplicateName() {
    super();
  }

  public DuplicateName(String message) {
        super(message);
    }

  public DuplicateName(String message, Throwable cause) {
    super(message, cause);
  }

  public DuplicateName(Throwable cause) {
    super(cause);
  }

  protected DuplicateName(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
