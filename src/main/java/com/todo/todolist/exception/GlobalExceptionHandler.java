package com.todo.todolist.exception;

import com.todo.todolist.dto.ErrorResponse;
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * A centralized class annotated with @ControllerAdvice that handles exceptions
 * thrown by any controller in the application.
 * It ensures consistent error responses and logs errors for debugging.
 */
@ControllerAdvice
public class GlobalExceptionHandler {
  private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  /**
   * Handle BadRequestException.
   *
   * @param ex the exception
   * @return error response
   */
  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException ex) {
    ErrorResponse errorResponse = new ErrorResponse(
        "Bad Request",
        ex.getMessage(),
        LocalDateTime.now(),
        HttpStatus.BAD_REQUEST.value()
    );
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  /**
   * Handle ConflictException.
   *
   * @param ex the exception
   * @return error response
   */
  @ExceptionHandler(ConflictException.class)
  public ResponseEntity<ErrorResponse> handleConflict(ConflictException ex) {
    ErrorResponse errorResponse = new ErrorResponse(
        "Already Exists",
        ex.getMessage(),
        LocalDateTime.now(),
        HttpStatus.CONFLICT.value()
    );
    return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
  }

  /**
   * Handle ForbiddenException.
   *
   * @param ex the exception
   * @return error response
   */
  @ExceptionHandler(ForbiddenException.class)
  public ResponseEntity<ErrorResponse> handleForbidden(ForbiddenException ex) {
    ErrorResponse errorResponse = new ErrorResponse(
        "Forbidden",
        ex.getMessage(),
        LocalDateTime.now(),
        HttpStatus.FORBIDDEN.value()
    );
    return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
  }

  /**
   * Handle NotFoundException.
   *
   * @param ex the exception
   * @return error response
   */
  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException ex) {
    ErrorResponse errorResponse = new ErrorResponse(
        "Not Found",
        ex.getMessage(),
        LocalDateTime.now(),
        HttpStatus.NOT_FOUND.value()
    );
    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
  }

  /**
   * Handle general exceptions.
   *
   * @param ex the exception
   * @return error response
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
    logger.error("Unhandled exception occurred: ", ex);

    ErrorResponse errorResponse = new ErrorResponse(
        "Internal Server Error",
        "An unexpected error occurred: " + ex.getMessage(),
        LocalDateTime.now(),
        HttpStatus.INTERNAL_SERVER_ERROR.value()
    );
    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
