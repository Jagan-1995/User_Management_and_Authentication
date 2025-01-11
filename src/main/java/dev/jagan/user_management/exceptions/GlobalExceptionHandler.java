package dev.jagan.user_management.exceptions;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice  // This class handles exceptions globally for all controllers
public class GlobalExceptionHandler {

    // Handles validation exceptions when request body validation fails
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        // Iterate through the validation errors and map field names to error messages
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField(); // Get the name of the field
            String errorMessage = error.getDefaultMessage(); // Get the error message for the field
            errors.put(fieldName, errorMessage); // Store the field and its error message
        });
        // Return a 400 BAD_REQUEST response with the validation errors
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    // Handles EntityNotFoundException, which can occur when a requested entity is not found
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFound(EntityNotFoundException ex) {
        // Return a 404 NOT_FOUND response with the exception message
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    // Handles BadCredentialsException, which occurs when authentication fails
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentials(BadCredentialsException ex) {
        // Return a 401 UNAUTHORIZED response with a custom error message
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }

    // Handles IllegalArgumentException, which is thrown when invalid arguments are passed
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        // Return a 400 BAD_REQUEST response with the exception message
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    // Handles any other unexpected exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        // Return a 500 INTERNAL_SERVER_ERROR response with a generic error message
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An unexpected error occurred");
    }
}
