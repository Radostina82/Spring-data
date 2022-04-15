package gamestore.exeptions;

public class UserNotLoggedInException extends RuntimeException {
    public UserNotLoggedInException() {
        super("Execute login command first!");
    }
}
