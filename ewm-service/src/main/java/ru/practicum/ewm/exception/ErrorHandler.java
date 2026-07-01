package ru.practicum.ewm.exception;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(NotFoundException exception) {
        return new ErrorResponse(
                "NOT_FOUND",
                "The required object was not found.",
                exception.getMessage(),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleValidationException(ValidationException exception) {
        return new ErrorResponse(
                "FORBIDDEN",
                "For the requested operation the conditions are not met.",
                exception.getMessage(),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex) {

        FieldError fieldError = ex.getBindingResult().getFieldError();

        String message = String.format(
                "Field: %s. Error: %s. Value: %s",
                fieldError.getField(),
                fieldError.getDefaultMessage(),
                fieldError.getRejectedValue()
        );

        return new ErrorResponse(
                "BAD_REQUEST",
                "Incorrectly made request.",
                message,
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConstraintViolationException(ConstraintViolationException exception) {
        return new ErrorResponse(
                "CONFLICT",
                "Integrity constraint has been violated.",
                exception.getMessage(),
                LocalDateTime.now()
        );
    }
}
