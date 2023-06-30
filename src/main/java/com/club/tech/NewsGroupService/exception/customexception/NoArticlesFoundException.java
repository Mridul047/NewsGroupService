package com.club.tech.NewsGroupService.exception.customexception;

public class NoArticlesFoundException extends RuntimeException {

  public NoArticlesFoundException(String message) {
    super(message);
  }
}
