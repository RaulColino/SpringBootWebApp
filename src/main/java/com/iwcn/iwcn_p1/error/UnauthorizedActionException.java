package com.iwcn.iwcn_p1.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedActionException extends RuntimeException {

    public UnauthorizedActionException(){
        super("Unauthorized Action");
    }

    public UnauthorizedActionException(String msg){
        super(msg);
    }
}
