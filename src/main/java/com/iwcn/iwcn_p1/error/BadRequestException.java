package com.iwcn.iwcn_p1.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {

    public BadRequestException() {
        super("Bad request");
    }

    public BadRequestException(String msg){
        super(msg);
    }
}


/* public class ResourceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;
  
    public ResourceNotFoundException(String msg) {
      super(msg);
    }
  } */