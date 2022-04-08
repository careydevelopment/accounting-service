package us.careydevelopment.ecosystem.business.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import us.careydevelopment.ecosystem.business.exception.InvalidRequestException;
import us.careydevelopment.ecosystem.business.exception.NotAuthorizedException;
import us.careydevelopment.ecosystem.business.exception.NotFoundException;
import us.careydevelopment.ecosystem.business.exception.UnknownUserException;
import us.careydevelopment.util.api.model.IRestResponse;
import us.careydevelopment.util.api.model.ValidationError;
import us.careydevelopment.util.api.response.ResponseEntityUtil;

import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<IRestResponse<Void>> notFoundException(NotFoundException ne) {
        return ResponseEntityUtil.createResponseEntityWithError(ne.getMessage(),
                HttpStatus.NOT_FOUND.value());
    }

    @ExceptionHandler(UnknownUserException.class)
    public ResponseEntity<IRestResponse<Void>> unknownUserException(UnknownUserException ue) {
        return ResponseEntityUtil.createResponseEntityWithUnauthorized(ue.getMessage());
    }

    @ExceptionHandler(NotAuthorizedException.class)
    public ResponseEntity<IRestResponse<Void>> notAUthorizedException(NotAuthorizedException ne) {
        return ResponseEntityUtil.createResponseEntityWithUnauthorized(ne.getMessage());
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<IRestResponse<List<ValidationError>>> invalidRegistrant(
            InvalidRequestException ex) {
        List<ValidationError> errors = ex.getErrors();
        return ResponseEntityUtil.createResponseEntityWithValidationErrors(errors);
    }
}
