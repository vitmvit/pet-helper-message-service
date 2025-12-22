package by.vitikova.discovery.controller.advice;

import by.vitikova.discovery.ErrorDto;
import by.vitikova.discovery.exception.EntityNotFoundException;
import by.vitikova.discovery.exception.InvalidJwtException;
import by.vitikova.discovery.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDto> error(ResourceNotFoundException e) {
        var errorResponse = this.buildErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(errorResponse);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorDto> error(EntityNotFoundException e) {
        var errorResponse = this.buildErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(errorResponse);
    }

    @ExceptionHandler(InvalidJwtException.class)
    public ResponseEntity<ErrorDto> error(InvalidJwtException e) {
        var errorResponse = this.buildErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> error(Exception e) {
        var errorResponse = this.buildErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }

    private ErrorDto buildErrorResponse(String message, HttpStatus code) {
        return new ErrorDto(message, code.value());
    }
}