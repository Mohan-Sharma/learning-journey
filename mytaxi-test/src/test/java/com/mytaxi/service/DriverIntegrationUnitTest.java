package com.mytaxi.service;

import static com.mytaxi.MyTaxiTestConstants.DUMMY_ID;
import static com.mytaxi.MyTaxiTestConstants.OFFLINE_DRIVER_PWD;
import static com.mytaxi.MyTaxiTestConstants.OFFLINE_DRIVER_USERNAME;
import static com.mytaxi.MyTaxiTestConstants.ONLINE_DRIVER_PWD;
import static com.mytaxi.MyTaxiTestConstants.ONLINE_DRIVER_USERNAME;
import static com.mytaxi.MyTaxiTestConstants.TEST_LATITUDE;
import static com.mytaxi.MyTaxiTestConstants.TEST_LICENSE_PLATE;
import static com.mytaxi.MyTaxiTestConstants.TEST_LONGITUDE;
import static com.mytaxi.MyTaxiTestConstants.TEST_MANUFACTURER;
import static com.mytaxi.MyTaxiTestConstants.TEST_SEAT_COUNT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.common.collect.Lists;
import com.mytaxi.dataaccessobject.CarRepository;
import com.mytaxi.dataaccessobject.DriverRepository;
import com.mytaxi.domainobject.CarDO;
import com.mytaxi.domainobject.DriverDO;
import com.mytaxi.domainvalue.GeoCoordinate;
import com.mytaxi.domainvalue.OnlineStatus;
import com.mytaxi.exception.CarAlreadyInUseException;
import com.mytaxi.exception.ConstraintsViolationException;
import com.mytaxi.exception.DriverConstraintsViolationException;
import com.mytaxi.exception.EntityNotFoundException;
import com.mytaxi.service.car.DefaultCarService;
import com.mytaxi.service.driver.DefaultDriverService;
import com.mytaxi.spring.config.MyTaxiTestConfigs;

/**
 * @author Mohan Sharma Created on 26/09/18.
 */
@RunWith(SpringRunner.class)
@Import(MyTaxiTestConfigs.class)
@SpringBootTest
public class DriverIntegrationUnitTest
{

	private DefaultDriverService driverService;
	private CarRepository carRepository;
	private DefaultCarService carService ;
	private DriverRepository driverRepository;

	private DriverDO offlineDriver;
	private DriverDO onlineDriver;
	private CarDO carDO;


	@Before
	public void setUp()
	{
		carRepository = mock(CarRepository.class);
		driverRepository = mock(DriverRepository.class);
		carService = new DefaultCarService(carRepository);
		driverService = new DefaultDriverService(driverRepository, carService);

		offlineDriver = getOfflineDriver();
		onlineDriver = getOnlineDriver();
		carDO = getMockCar();
	}

	@Test(expected = EntityNotFoundException.class)
	public void whenUnknownEntityIdThenEntityNotFoundException() throws EntityNotFoundException
	{
		when(driverService.find(any(Long.class))).thenThrow(mock(EntityNotFoundException.class));
	}

	@Test
	public void whenValidEntityIdThenShouldReturnValidEntity() throws EntityNotFoundException
	{
		DriverDO driverFound = mock(DriverDO.class);
		DriverDO driverMock = mock(DriverDO.class);
		assertNotEquals(driverMock, driverFound);

		when(driverRepository.findOneByIdAndDeletedFalse(any(Long.class))).thenReturn(Optional.of(driverMock));

		driverFound = driverService.find(any(Long.class));
		assertEquals(driverMock, driverFound);
	}

	@Test
	public void whenOfflineDriverIsCreatedNonNullFieldsThenShouldPersistAndReturnOfflineStatus() throws ConstraintsViolationException
	{
		when(driverService.create(offlineDriver)).thenReturn(offlineDriver);

		assertEquals(OFFLINE_DRIVER_PWD, offlineDriver.getPassword());
		assertEquals(OFFLINE_DRIVER_USERNAME, offlineDriver.getUsername());
		assertNull(offlineDriver.getCoordinate());
		assertFalse(offlineDriver.getDeleted());
		assertNotEquals(OnlineStatus.ONLINE, offlineDriver.getOnlineStatus());
	}

	@Test
	public void whenOnlineDriverIsCreatedNonNullFieldsThenShouldPersistAndReturnOnlineStatus() throws ConstraintsViolationException
	{
		when(driverService.create(onlineDriver)).thenReturn(onlineDriver);

		assertEquals(ONLINE_DRIVER_PWD, onlineDriver.getPassword());
		assertEquals(ONLINE_DRIVER_USERNAME, onlineDriver.getUsername());
		assertNotNull(onlineDriver.getCoordinate());
		assertFalse(onlineDriver.getDeleted());
		assertNotEquals(OnlineStatus.OFFLINE, onlineDriver.getOnlineStatus());
	}

	@Test
	public void whenMandatoryFieldsAreNullThenShouldThrowDataIntegrityViolationException()
			throws ConstraintsViolationException
	{
		onlineDriver.setUsername(null);

		when(driverService.create(onlineDriver)).thenThrow(DataIntegrityViolationException.class);
	}

	@Test(expected = IllegalArgumentException.class)
	public void whenCreateNullDriverThenShouldThrowDataIntegrityViolationException() throws ConstraintsViolationException
	{
		when(driverService.create(null)).thenThrow(IllegalArgumentException.class);
		driverService.create(null);
	}


	@Test
	public void whenADriverIsDeletedThenItsDeletedFieldShouldBeTrue() throws EntityNotFoundException
	{
		when(driverRepository.findOneByIdAndDeletedFalse(any(Long.class))).thenReturn(Optional.of(onlineDriver));

		driverService.delete(any(Long.class));
		assertTrue(onlineDriver.getDeleted());
		assertFalse(offlineDriver.getDeleted());

		when(driverRepository.findOneByIdAndDeletedFalse(any(Long.class))).thenReturn(Optional.of(offlineDriver));

		driverService.delete(any(Long.class));
		assertTrue(offlineDriver.getDeleted());
	}

	@Test(expected = EntityNotFoundException.class)
	public void whenTryToDeleteUnknownCarThenShouldThrowEntityNotFoundException() throws EntityNotFoundException
	{
		doThrow(EntityNotFoundException.class).when(mock(DefaultDriverService.class)).delete(any(Long.class));
		driverService.delete(DUMMY_ID);
	}

	@Test
	public void whenGivenStatusOfDriverIsOnlineThenReturnAllOnlineDriversEntityNotFoundException()
	{
		List<DriverDO> onlineDrivers = driverService.find(OnlineStatus.ONLINE);
		assertNotEquals(1, onlineDrivers.size());
		assertEquals(0, onlineDrivers.size());

		when(driverRepository.findByOnlineStatusAndDeletedFalse(OnlineStatus.ONLINE))
				.thenReturn(Lists.newArrayList(onlineDriver));

		onlineDrivers = driverService.find(OnlineStatus.ONLINE);
		assertEquals(1, onlineDrivers.size());
	}

	@Test(expected = EntityNotFoundException.class)
	public void whenUpdateLocationOfUnknownDriverIdThenShouldThrowEntityNotFoundException() throws EntityNotFoundException
	{
		doThrow(EntityNotFoundException.class).when(mock(DefaultDriverService.class)).updateLocation(any(Long.class), any(Long.class), any(Long.class));
		driverService.updateLocation(DUMMY_ID, TEST_LONGITUDE, TEST_LATITUDE);
	}

	@Test
	public void whenGivenLatitudeAndLongitudeForDriverThenShouldUpdateGeoCoordinateOfDriver() throws EntityNotFoundException
	{
		assertNull(offlineDriver.getCoordinate());

		when(driverRepository.findOneByIdAndDeletedFalse(any(Long.class))).thenReturn(Optional.of(offlineDriver));

		driverService.updateLocation(DUMMY_ID, TEST_LONGITUDE, TEST_LATITUDE);
		assertNotNull(offlineDriver.getCoordinate());
		assertEquals(TEST_LATITUDE, offlineDriver.getCoordinate().getLatitude(), 0.001);
		assertEquals(TEST_LONGITUDE, offlineDriver.getCoordinate().getLongitude(), 0.001);
	}

	@Test(expected = EntityNotFoundException.class)
	public void whenUnknownDriverTryToSelectCarThenShouldThrowEntityNotFoundException()
			throws EntityNotFoundException, CarAlreadyInUseException, DriverConstraintsViolationException
	{
		when(carRepository.findOneByCarIdAndDeletedFalse(any(Long.class))).thenReturn(Optional.of(carDO));
		when(driverService.selectCar(DUMMY_ID, DUMMY_ID)).thenThrow(EntityNotFoundException.class);
		driverService.selectCar(DUMMY_ID, DUMMY_ID);
	}

	@Test(expected = EntityNotFoundException.class)
	public void whenDriverTryToSelectUnknownCarThenShouldThrowEntityNotFoundException()
			throws EntityNotFoundException, CarAlreadyInUseException, DriverConstraintsViolationException
	{
		when(driverRepository.findOneByIdAndDeletedFalse(any(Long.class))).thenReturn(Optional.of(onlineDriver));
		when(driverService.selectCar(DUMMY_ID, DUMMY_ID)).thenThrow(EntityNotFoundException.class);
		driverService.selectCar(DUMMY_ID, DUMMY_ID);
	}

	@Test
	public void whenValidDriverTryToValidCarThenShouldMapCarToDriverAndReturnDriver()
	{

	}

	private DriverDO getOnlineDriver()
	{
		DriverDO driverDO = new DriverDO(ONLINE_DRIVER_USERNAME, ONLINE_DRIVER_PWD);
		driverDO.setOnlineStatus(OnlineStatus.ONLINE);
		GeoCoordinate geoCoordinate = new GeoCoordinate(TEST_LATITUDE, TEST_LONGITUDE);
		driverDO.setCoordinate(geoCoordinate);
		return driverDO;
	}

	private DriverDO getOfflineDriver()
	{
		return new DriverDO(OFFLINE_DRIVER_USERNAME, OFFLINE_DRIVER_PWD);
	}


	private CarDO getMockCar()
	{
		CarDO carDO = new CarDO();
		carDO.setEngineType(CarDO.EngineType.DIESEL);
		carDO.setLicensePlate(TEST_LICENSE_PLATE);
		carDO.setEngineType(CarDO.EngineType.DIESEL);
		carDO.setManufacturer(TEST_MANUFACTURER);
		carDO.setSeatCount(TEST_SEAT_COUNT);
		return carDO;
	}
}
