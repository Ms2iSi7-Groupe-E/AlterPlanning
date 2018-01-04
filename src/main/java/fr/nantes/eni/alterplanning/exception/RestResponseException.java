package fr.nantes.eni.alterplanning.exception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;

/**
 * The type Rest response exception.
 */
public class RestResponseException extends Exception {

    private HttpStatus status;
    private BindingResult result;
    private String message;

    /**
     * Instantiates a new Rest response exception.
     *
     * @param message the message
     */
    public RestResponseException(String message) {
        this.message = message;
    }

    /**
     * Instantiates a new Rest response exception.
     *
     * @param status  the status
     * @param message the message
     */
    public RestResponseException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    /**
     * Instantiates a new Rest response exception.
     *
     * @param status the status
     * @param result the result
     */
    public RestResponseException(HttpStatus status, BindingResult result) {
        this.status = status;
        this.result = result;
    }

    /**
     * Instantiates a new Rest response exception.
     *
     * @param status  the status
     * @param message the message
     * @param result  the result
     */
    public RestResponseException(HttpStatus status, String message, BindingResult result) {
        this.status = status;
        this.result = result;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public BindingResult getResult() {
        return result;
    }

    public void setResult(BindingResult result) {
        this.result = result;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
