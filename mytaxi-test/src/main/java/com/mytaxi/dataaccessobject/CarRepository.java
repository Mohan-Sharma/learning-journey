package com.mytaxi.dataaccessobject;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.mytaxi.domainobject.CarDO;

/**
 * @author Mohan Sharma Created on 20/09/18.
 */
public interface CarRepository extends JpaRepository<CarDO, Long>, JpaSpecificationExecutor
{

	List<CarDO> findAllByDeletedFalseAndDriverIsNull();

	Optional<CarDO> findOneByCarIdAndDeletedFalse(final long carId);

	Optional<CarDO> findOneByLicensePlate(final String licensePlate);
}
