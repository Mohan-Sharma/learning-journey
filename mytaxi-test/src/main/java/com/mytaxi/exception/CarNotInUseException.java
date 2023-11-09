package com.mytaxi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "The car is not being used by the driver!!")
public class CarNotInUseException extends Exception
{
    public CarNotInUseException(String message)
    {
        super(message);
    }
}
