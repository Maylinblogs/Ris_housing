package by.bsuir.housing.exception;

public class NoSuchRecordException extends RuntimeException {
    public NoSuchRecordException() {
        super();
    }

    public NoSuchRecordException(String message) {
        super(message);
    }

    public NoSuchRecordException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchRecordException(Throwable cause) {
        super(cause);
    }

    protected NoSuchRecordException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
