package com.club.tech.NewsGroupService.exception.handler;

import com.club.tech.NewsGroupService.exception.customexception.NoArticlesFoundException;
import com.club.tech.NewsGroupService.model.response.FaultResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
  private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(NoArticlesFoundException.class)
  public ResponseEntity<FaultResponse> handleNoArticlesFoundException(NoArticlesFoundException ex) {
    log.error("@@ EXCEPTION OCCURRED : %s".formatted(ex.getMessage()));
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
        .body(new FaultResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<FaultResponse> handleException(Exception ex) {
    log.error("@@ EXCEPTION OCCURRED : %s".formatted(ex.getMessage()));
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new FaultResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()));
  }
}
