package fr.nantes.eni.alterplanning.controller;

import fr.nantes.eni.alterplanning.exception.RestResponseException;
import fr.nantes.eni.alterplanning.model.response.ErrorResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> exceptionHandler(Exception ex) {
        ErrorResponse error = new ErrorResponse();
        error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.setError("Internal Error, Please contact your administrator");
        error.setMessage("Exception: " + ex.getClass().getSimpleName() + ", error: " + ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> badCredentialsExceptionHandler(BadCredentialsException ex) {
        ErrorResponse error = new ErrorResponse();
        error.setStatus(HttpStatus.UNAUTHORIZED.value());
        error.setError("Bad Credentials");
        error.setMessage(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> accessDeniedExceptionHandler(AccessDeniedException ex) {
        ErrorResponse error = new ErrorResponse();
        error.setStatus(HttpStatus.FORBIDDEN.value());
        error.setError("Forbidden");
        error.setMessage(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(RestResponseException.class)
    public ResponseEntity<ErrorResponse> restResponseExceptionHandler(RestResponseException ex) {
        ErrorResponse error = new ErrorResponse();
        HttpStatus httpStatus = ex.getStatus() == null ? HttpStatus.INTERNAL_SERVER_ERROR : ex.getStatus();

        error.setStatus(httpStatus.value());
        error.setError(httpStatus.getReasonPhrase());
        error.setMessage(ex.getMessage());

        if (StringUtils.isEmpty(ex.getMessage())) {
            error.setMessage("An error occurred");
        }

        if (ex.getResult() != null) {
            final Map<String, String> errorList = new HashMap<>();
            ex.getResult().getFieldErrors().forEach(e -> errorList.put(e.getField(), e.getDefaultMessage()));
            error.setErrorList(errorList);
        }

        return new ResponseEntity<>(error, httpStatus);
    }
}
