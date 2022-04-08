package us.careydevelopment.ecosystem.business.exception;

import us.careydevelopment.util.api.model.ValidationError;

import java.util.List;

public class InvalidRequestException extends Exception {
    private static final long serialVersionUID = 9155139576610874161L;

    private List<ValidationError> errors;

    public InvalidRequestException(String message, List<ValidationError> errors) {
        super(message);
        this.errors = errors;
    }

    public List<ValidationError> getErrors() {
        return errors;
    }
}
