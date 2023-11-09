package com.mytaxi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "The car is already being used by some other driver!!")
public class CarAlreadyInUseException extends Exception
{
    public CarAlreadyInUseException(String message)
    {
        super(message);
    }
}
