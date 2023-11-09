package com.mytaxi.spring.config;

import java.time.ZonedDateTime;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.mytaxi.util.validation.ErrorDetails;

/**
 * @author Mohan Sharma Created on 25/09/18.
 */
@ControllerAdvice
@Configuration
public class CustomExceptionHandler
{

	@ExceptionHandler(value = { ConstraintViolationException.class })
	protected ResponseEntity<ErrorDetails> handle(ConstraintViolationException e)
	{
		Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
		StringBuilder strBuilder = new StringBuilder();
		for (ConstraintViolation<?> violation : violations ) {
			strBuilder.append(violation.getMessage()).append(System.lineSeparator());
		}
		final ErrorDetails errorDetails =
				ErrorDetails
						.builder()
						.message(StringUtils.isNotBlank(e.getMessage()) ? e.getMessage() : "Constraints Violations")
						.error(strBuilder.toString())
						.timestamp(ZonedDateTime.now())
						.build();
		return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
	}
}
