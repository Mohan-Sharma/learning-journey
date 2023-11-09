package com.mytaxi.controller.mapper;

import java.time.ZonedDateTime;
import java.util.Objects;

import com.mytaxi.datatransferobject.CarDTO;
import com.mytaxi.domainobject.CarDO;

/**
 * @author Mohan Sharma Created on 21/09/18.
 */
public class CarMapper
{
	public static CarDTO makeCarDTO(CarDO car)
	{
		CarDTO carDTO =
				CarDTO
						.builder()
						.licensePlate(car.getLicensePlate())
						.convertible(car.getConvertible())
						.deleted(car.isDeleted())
						.manufacturer(car.getManufacturer())
						.seatCount(car.getSeatCount())
						.build();

		if(Objects.nonNull(car.getRating()))
			carDTO.setRating(car.getRating());
		if(Objects.nonNull(car.getEngineType()))
			carDTO.setEngineType(car.getEngineType());

		return carDTO;
	}

	public static CarDO makeCarDO(CarDTO carDTO)
	{
		CarDO car =  CarDO
				.builder()
				.licensePlate(carDTO.getLicensePlate())
				.convertible(carDTO.isConvertible())
				.manufacturer(carDTO.getManufacturer())
				.seatCount(carDTO.getSeatCount())
				.dateCreated(ZonedDateTime.now())
				.build();

		if(Objects.nonNull(carDTO.getRating()))
			car.setRating(carDTO.getRating());
		if(Objects.nonNull(carDTO.getEngineType()))
			car.setEngineType(carDTO.getEngineType());

		return car;
	}

	public static CarDO copyPropertiesFromDtoToModel(CarDO car, CarDTO carDTO)
	{
		car =  car
				.toBuilder()
				.licensePlate(carDTO.getLicensePlate())
				.convertible(carDTO.isConvertible())
				.manufacturer(carDTO.getManufacturer())
				.seatCount(carDTO.getSeatCount())
				.dateCreated(ZonedDateTime.now())
				.deleted(carDTO.isDeleted())
				.build();

		if(Objects.nonNull(carDTO.getRating()))
			car.setRating(carDTO.getRating());
		if(Objects.nonNull(carDTO.getEngineType()))
			car.setEngineType(carDTO.getEngineType());

		return car;
	}
}
