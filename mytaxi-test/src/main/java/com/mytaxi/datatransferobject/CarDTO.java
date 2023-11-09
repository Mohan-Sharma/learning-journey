package com.mytaxi.datatransferobject;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mytaxi.domainobject.CarDO.CarRating;
import com.mytaxi.domainobject.CarDO.EngineType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Mohan Sharma Created on 21/09/18.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CarDTO
{
	@NotNull(message = "License Plate can not be null!")
	private String licensePlate;
	@NotNull(message = "Engine Type can not be null!")
	private EngineType engineType;
	@NotNull(message = "Manufacturer can not be null!")
	private String manufacturer;
	@NotNull(message = "Seat Count can not be null!")
	@Min(value = 2, message = "Min seat count allowed is 2!!")
	private Integer seatCount;
	private CarRating rating;
	private boolean convertible;
	private boolean deleted;
}
