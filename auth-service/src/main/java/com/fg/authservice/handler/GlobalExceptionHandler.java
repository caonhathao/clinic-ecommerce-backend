package com.fg.authservice.handler;

import com.fg.authservice.exception.UserAlreadyExistsException;
import com.fg.authservice.exception.InvalidCredentialsException;
import com.fg.authservice.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ErrorMessageDTO buildErrorResponse(String message, String code, Map<String, String> details) {
        String timestamp = DateTimeFormatter.ISO_INSTANT.format(Instant.now());
        return ErrorMessageDTO.builder()
                .code(code)
                .message(message)
                .timestamp(timestamp)
                .details(details)
                .build();
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorMessageDTO> handleUserNotFoundException(UserNotFoundException e) {
        return new ResponseEntity<>(
                buildErrorResponse(e.getMessage(), ErrorCode.USER_NOT_FOUND.name(), null),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorMessageDTO> handleUserAlreadyExistsException(UserAlreadyExistsException e) {
        return new ResponseEntity<>(
                buildErrorResponse(e.getMessage(), ErrorCode.USER_ALREADY_EXISTS.name(), null),
                HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorMessageDTO> handleInvalidCredentialsException(InvalidCredentialsException e) {
        return new ResponseEntity<>(
                buildErrorResponse(e.getMessage(), ErrorCode.INVALID_CREDENTIALS.name(), null),
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessageDTO> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> validationErrors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            validationErrors.put(fieldName, message);
        });

        return new ResponseEntity<>(
                buildErrorResponse("Validation failed", ErrorCode.VALIDATION_ERROR.name(), validationErrors),
                HttpStatus.BAD_REQUEST
        );
    }
}
