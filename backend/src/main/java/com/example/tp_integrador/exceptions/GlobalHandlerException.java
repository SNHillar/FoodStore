package com.example.tp_integrador.exceptions;

import com.example.tp_integrador.dtos.error.ErrorDto;
import jakarta.validation.ConstraintDeclarationException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
public class GlobalHandlerException {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> handleValidationErrors(MethodArgumentNotValidException ex){
        List<String> details = ex.getBindingResult()
                .getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .toList();

        ErrorDto errorDto = ErrorDto.of(HttpStatus.BAD_REQUEST.value(), "Validate error.", details);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

    @ExceptionHandler(ConstraintDeclarationException.class)
    public ResponseEntity<ErrorDto> handleConstraintViolation(ConstraintViolationException ex){
        List<String> details = ex.getConstraintViolations().stream()
                .map(constraintViolation -> constraintViolation.getPropertyPath() + ": " + constraintViolation.getMessage())
                .toList();

        ErrorDto errorDto = ErrorDto.of(HttpStatus.BAD_REQUEST.value(), "Constraint error.", details);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDto> handleIllegalArgument(IllegalArgumentException ex){
        ErrorDto errorDto = ErrorDto.simple(HttpStatus.BAD_REQUEST.value(), "Invalid Argument: ", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);

    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleGenericException(Exception ex){
        ErrorDto errorDto = ErrorDto.simple(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error.", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDto);
    }
}
