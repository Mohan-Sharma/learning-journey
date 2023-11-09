package com.mytaxi.service.car;

import java.util.List;

import com.mytaxi.datatransferobject.CarDTO;
import com.mytaxi.domainobject.CarDO;
import com.mytaxi.exception.ConstraintsViolationException;
import com.mytaxi.exception.EntityNotFoundException;

/**
 * @author Mohan Sharma Created on 20/09/18.
 */
public interface CarService
{
	CarDO findActiveCarById(final long carId) throws EntityNotFoundException;

	CarDO createOrUpdate(final CarDTO carDTO) throws ConstraintsViolationException;

	void delete(final long carId) throws EntityNotFoundException;

	List<CarDO> findAllAvailableCars() throws EntityNotFoundException;

}
