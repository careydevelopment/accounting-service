package us.careydevelopment.accounting.exception;

public class UnknownUserException extends RuntimeException {

    public UnknownUserException(String s) {
        super(s);
    }
}
