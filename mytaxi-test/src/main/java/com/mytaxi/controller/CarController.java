package com.mytaxi.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.mytaxi.controller.mapper.CarMapper;
import com.mytaxi.datatransferobject.CarDTO;
import com.mytaxi.domainobject.CarDO;
import com.mytaxi.exception.ConstraintsViolationException;
import com.mytaxi.exception.EntityNotFoundException;
import com.mytaxi.service.car.CarService;

import lombok.AllArgsConstructor;

/**
 * This request handler will handle all request related to car.
 *
 * @author Mohan Sharma Created on 20/09/18.
 */
@RestController
@RequestMapping("v1/cars")
@AllArgsConstructor
@Validated
public class CarController
{
	private CarService carService;

	@GetMapping("/{carId}")
	@ResponseStatus(HttpStatus.OK)
	public CarDTO getCar(@PathVariable("carId") long carId) throws EntityNotFoundException
	{
		return CarMapper.makeCarDTO(carService.findActiveCarById(carId));
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public CarDTO createCar(@Valid @RequestBody CarDTO carDTO) throws ConstraintsViolationException
	{
		return CarMapper.makeCarDTO(carService.createOrUpdate(carDTO));
	}


	@DeleteMapping("/{carId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteCar(@PathVariable("carId")  long carId) throws EntityNotFoundException
	{
		carService.delete(carId);
	}

	@GetMapping("/allAvailableCars")
	@ResponseStatus(HttpStatus.OK)
	public List<CarDTO> getAllAvailableCars() throws EntityNotFoundException
	{
		List<CarDO> cars = carService.findAllAvailableCars();
		if(!CollectionUtils.isEmpty(cars))
		{
			return cars
					.stream()
					.map(CarMapper::makeCarDTO)
					.collect(Collectors.toList());
		}
		return Lists.newArrayList();
	}
}
