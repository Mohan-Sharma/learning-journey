package com.mytaxi.service.driver;

import static java.lang.String.format;

import com.google.common.collect.Sets;
import com.mytaxi.dataaccessobject.DriverRepository;
import com.mytaxi.domainobject.CarDO;
import com.mytaxi.domainobject.DriverDO;
import com.mytaxi.domainobject.DriverSpecification;
import com.mytaxi.domainvalue.GeoCoordinate;
import com.mytaxi.domainvalue.OnlineStatus;
import com.mytaxi.exception.CarAlreadyInUseException;
import com.mytaxi.exception.CarNotInUseException;
import com.mytaxi.exception.ConstraintsViolationException;
import com.mytaxi.exception.DriverConstraintsViolationException;
import com.mytaxi.exception.EntityNotFoundException;
import com.mytaxi.service.car.CarService;
import com.mytaxi.util.DefaultMyTaxiConstants;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import lombok.AllArgsConstructor;

/**
 * Service to encapsulate the link between DAO and controller and to have business logic for some driver specific things.
 * <p/>
 */
@Service
@AllArgsConstructor
public class DefaultDriverService implements DriverService
{

    private static final Logger LOG = LoggerFactory.getLogger(DefaultDriverService.class);

    private DriverRepository driverRepository;
    private CarService carService;


    /**
     * Selects a driver by id.
     *
     * @param driverId
     * @return found driver
     * @throws EntityNotFoundException if no driver with the given id was found.
     */
    @Override
    public DriverDO find(Long driverId) throws EntityNotFoundException
    {
        return findDriverChecked(driverId);
    }


    /**
     * Creates a new driver.
     *
     * @param driverDO
     * @return
     * @throws ConstraintsViolationException if a driver already exists with the given username, ... .
     */
    @Override
    public DriverDO create(DriverDO driverDO) throws ConstraintsViolationException
    {
        DriverDO driver;
        try
        {
            driver = driverRepository.save(driverDO);
        }
        catch (DataIntegrityViolationException e)
        {
            LOG.warn("ConstraintsViolationException while creating a driver: {}", driverDO, e);
            throw new ConstraintsViolationException(e.getMessage());
        }
        return driver;
    }


    /**
     * Deletes an existing driver by id.
     *
     * @param driverId
     * @throws EntityNotFoundException if no driver with the given id was found.
     */
    @Override
    @Transactional
    public void delete(Long driverId) throws EntityNotFoundException
    {
        DriverDO driverDO = findDriverChecked(driverId);
        driverDO.setDeleted(true);
    }


    /**
     * Update the location for a driver.
     *
     * @param driverId
     * @param longitude
     * @param latitude
     * @throws EntityNotFoundException
     */
    @Override
    @Transactional
    public void updateLocation(long driverId, double longitude, double latitude) throws EntityNotFoundException
    {
        DriverDO driverDO = findDriverChecked(driverId);
        driverDO.setCoordinate(new GeoCoordinate(latitude, longitude));
    }


    /**
     * Find all drivers by online state.
     *
     * @param onlineStatus
     */
    @Override
    public List<DriverDO> find(OnlineStatus onlineStatus)
    {
        return driverRepository.findByOnlineStatusAndDeletedFalse(onlineStatus);
    }


    private DriverDO findDriverChecked(final long driverId) throws EntityNotFoundException
    {
        return driverRepository.findOneByIdAndDeletedFalse(driverId)
            .orElseThrow(() -> new EntityNotFoundException(format(DefaultMyTaxiConstants.DRIVER_NOT_FOUND_EXCEPTION,  driverId)));
    }

    /**
     * This function helps the driver to select a car.
     * @param driverId
     * @param carId
     * @throws EntityNotFoundException
     * @throws DriverConstraintsViolationException
     * @throws CarAlreadyInUseException
     */
    @Override
    @Transactional
    public DriverDO selectCar(final long driverId, final long carId)
            throws EntityNotFoundException, CarAlreadyInUseException, DriverConstraintsViolationException
    {
        final DriverDO driver = findDriverChecked(driverId);

        if(!OnlineStatus.ONLINE.equals(driver.getOnlineStatus()))
            throw new DriverConstraintsViolationException(format(DefaultMyTaxiConstants.DRIVER_OFFLINE_EXCEPTION, driverId));
        if(Objects.nonNull(driver.getCar()))
            throw new DriverConstraintsViolationException(format(DefaultMyTaxiConstants.DRIVER_ALREADY_USING_CAR_EXCEPTION, driverId, driver.getCar().getCarId()));

        CarDO car = carService.findActiveCarById(carId);
        if(Objects.nonNull(car.getDriver()))
            throw new CarAlreadyInUseException(format(DefaultMyTaxiConstants.CAR_IN_USE_EXCEPTION, carId));
        driver.setCar(car);
        return driver;
    }

    /**
     * This function helps the driver to deselect the selected car.
     * @param driverId
     * @param carId
     * @throws EntityNotFoundException
     * @throws CarNotInUseException
     * @throws DriverConstraintsViolationException
     */
    @Override
    @Transactional
    public DriverDO deselectCar(final long driverId, final long carId)
            throws EntityNotFoundException, CarNotInUseException, DriverConstraintsViolationException
    {
        final DriverDO driver = findDriverChecked(driverId);

        if(!OnlineStatus.ONLINE.equals(driver.getOnlineStatus()))
            throw new DriverConstraintsViolationException(format(DefaultMyTaxiConstants.DRIVER_OFFLINE_EXCEPTION, driverId));

        CarDO car = driver.getCar();

        if(Objects.isNull(car))
            throw new CarNotInUseException(format(DefaultMyTaxiConstants.DRIVER_NOT_USING_CAR_EXCEPTION, driverId));
        driver.setCar(null);
        Set<CarDO> alreadyDrivenCars = driver.getCars();
        if(CollectionUtils.isEmpty(alreadyDrivenCars))
            alreadyDrivenCars = Sets.newHashSet();
        alreadyDrivenCars.add(car);
        driver.setCars(alreadyDrivenCars);
        return driver;
    }

    @Override
    public List<DriverDO> searchDriversByCriteria(DriverDO driver)
    {
        DriverSpecification driverSpecification = new DriverSpecification(driver);
        return driverRepository.findAll(driverSpecification);
    }
}
