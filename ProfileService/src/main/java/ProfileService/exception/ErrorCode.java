package ProfileService.exception;

public enum ErrorCode {
    CONFIRM_PASSWORD_NOT_MATCHING(1001, "Confirm password does not match with password"),
    USER_IS_EXISTED(1002, "User already exists"),
    UNAUTHENTICATED(1004, "unauthenticated"),
    USER_IS_NOT_EXISTED(1003, "User not exists");

    private int code;
    private String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
