package com.mytaxi.service.car;

import static java.lang.String.format;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mytaxi.controller.mapper.CarMapper;
import com.mytaxi.dataaccessobject.CarRepository;
import com.mytaxi.datatransferobject.CarDTO;
import com.mytaxi.domainobject.CarDO;
import com.mytaxi.exception.ConstraintsViolationException;
import com.mytaxi.exception.EntityNotFoundException;
import com.mytaxi.util.DefaultMyTaxiConstants;

import lombok.AllArgsConstructor;

/**
 * @author Mohan Sharma Created on 20/09/18.
 */
@Service
@AllArgsConstructor
public class DefaultCarService implements CarService
{
	private static final Logger LOG = LoggerFactory.getLogger(DefaultCarService.class);

	private CarRepository carRepository;

	@Override
	public CarDO findActiveCarById(long carId) throws EntityNotFoundException
	{
		return carRepository
				.findOneByCarIdAndDeletedFalse(carId)
				.orElseThrow(() -> new EntityNotFoundException(format(DefaultMyTaxiConstants.CAR_NOT_FOUND_EXCEPTION , carId)));
	}

	@Override
	@Transactional
	public CarDO createOrUpdate(CarDTO carDTO) throws ConstraintsViolationException
	{
		CarDO carDO = null;
		try
		{
			Optional<CarDO> carDOOptional = carRepository.findOneByLicensePlate(carDTO.getLicensePlate());
			if(carDOOptional.isPresent())
				carDO = CarMapper.copyPropertiesFromDtoToModel(carDOOptional.get(), carDTO);
			else
				carDO = CarMapper.makeCarDO(carDTO);
			return carRepository.save(carDO);
		}
		catch (DataIntegrityViolationException e)
		{
			LOG.warn("ConstraintsViolationException while updating a car: {}", carDO, e);
			throw new ConstraintsViolationException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public void delete(long carId) throws EntityNotFoundException
	{
		CarDO carDO = findActiveCarById(carId);
		carDO.setDeleted(true);
	}

	@Override public List<CarDO> findAllAvailableCars()
	{
		return carRepository.findAllByDeletedFalseAndDriverIsNull();
	}
}
