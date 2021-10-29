package com.yomimashou.study;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.BAD_REQUEST)
public class BLValidationException extends RuntimeException {

  public BLValidationException(String message) {
    super(message);
  }
}
