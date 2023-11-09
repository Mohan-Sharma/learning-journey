package com.mytaxi.util.validation;

import java.time.ZonedDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Mohan Sharma Created on 23/09/18.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorDetails
{
	private ZonedDateTime timestamp;
	private String message;
	private String error;
}
