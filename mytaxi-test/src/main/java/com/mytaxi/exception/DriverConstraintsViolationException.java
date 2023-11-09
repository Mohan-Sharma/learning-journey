package com.mytaxi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DriverConstraintsViolationException extends Exception
{
    public DriverConstraintsViolationException(String message)
    {
        super(message);
    }

}
