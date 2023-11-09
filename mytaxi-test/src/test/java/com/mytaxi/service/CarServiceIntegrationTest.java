package com.mytaxi.service;

import static com.mytaxi.MyTaxiTestConstants.TEST_LICENSE_PLATE;
import static com.mytaxi.MyTaxiTestConstants.TEST_MANUFACTURER;
import static com.mytaxi.MyTaxiTestConstants.TEST_SEAT_COUNT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
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
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.common.collect.Lists;
import com.mytaxi.dataaccessobject.CarRepository;
import com.mytaxi.datatransferobject.CarDTO;
import com.mytaxi.domainobject.CarDO;
import com.mytaxi.domainobject.CarDO.EngineType;
import com.mytaxi.exception.ConstraintsViolationException;
import com.mytaxi.exception.EntityNotFoundException;
import com.mytaxi.service.car.DefaultCarService;
import com.mytaxi.spring.config.MyTaxiTestConfigs;

/**
 * @author Mohan Sharma Created on 25/09/18.
 */
@RunWith(SpringRunner.class)
@Import(MyTaxiTestConfigs.class)
@SpringBootTest
public class CarServiceIntegrationTest
{

	@InjectMocks
	private DefaultCarService carService;

	@MockBean
	private CarRepository carRepository;

	private CarDO carDO;
	private CarDTO carDTO;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		carDO = getMockCar();
		carDTO = getMockCarDto();
	}

	@Test(expected = EntityNotFoundException.class)
	public void whenUnknownEntityIdThenEntityNotFoundException() throws EntityNotFoundException
	{
		when(carService.findActiveCarById(any(Long.class))).thenThrow(mock(EntityNotFoundException.class));
		carService.findActiveCarById(any(Long.class));
	}

	@Test
	public void whenValidEntityIdThenShouldReturnValidEntity() throws EntityNotFoundException
	{
		when(carRepository.findOneByCarIdAndDeletedFalse(any(Long.class))).thenReturn(Optional.of(carDO));

		CarDO carDOFound = carService.findActiveCarById(any(Long.class));
		assertEquals(TEST_LICENSE_PLATE, carDOFound.getLicensePlate());
		assertEquals(carDOFound, carDO);
	}

	@Test
	public void whenCarIsCreatedNonNullFieldsThenShouldPersistAndReturnTheFields() throws ConstraintsViolationException
	{
		when(carService.createOrUpdate(carDTO)).thenReturn(carDO);

		assertEquals(EngineType.DIESEL, carDO.getEngineType());
		assertEquals(TEST_MANUFACTURER, carDO.getManufacturer());
		assertEquals(TEST_SEAT_COUNT, carDO.getSeatCount().longValue());
		assertEquals(TEST_LICENSE_PLATE, carDO.getLicensePlate());
	}

	@Test
	public void whenCarIsCreatedWithNullFieldsThenShouldThrowDataIntegrityViolationException() throws ConstraintsViolationException
	{
		carDTO.setEngineType(null);
		when(carService.createOrUpdate(carDTO)).thenThrow(DataIntegrityViolationException.class);
	}

	@Test
	public void whenExistingCarFieldsAreUpdatedIsThenShouldReturnTheUpdatedFields() throws ConstraintsViolationException
	{
		carDTO.setEngineType(EngineType.ELECTRIC);
		carDO.setEngineType(EngineType.ELECTRIC);
		when(carService.createOrUpdate(carDTO)).thenReturn(carDO);

		assertEquals(EngineType.ELECTRIC, carDO.getEngineType());
		assertEquals(TEST_MANUFACTURER, carDO.getManufacturer());
		assertNotEquals(2, carDO.getSeatCount().longValue());
	}

	@Test
	public void whenACarIsDeletedThenItsDeletedFieldShouldBeTrue() throws EntityNotFoundException
	{
		assertFalse(carDO.isDeleted());

		when(carRepository.findOneByCarIdAndDeletedFalse(any(Long.class))).thenReturn(Optional.of(carDO));

		carService.delete(any(Long.class));
		assertTrue(carDO.isDeleted());
	}

	@Test(expected = EntityNotFoundException.class)
	public void whenTryToDeleteUnknownCarThenShouldThrowEntityNotFoundException() throws EntityNotFoundException
	{
		doThrow(EntityNotFoundException.class).when(mock(DefaultCarService.class)).delete(any(Long.class));
		carService.delete(any(Long.class));
	}

	@Test
	public void whenTwoActiveCarsThenShouldReturnCountAsTwo() throws EntityNotFoundException
	{
		when(carRepository.findAllByDeletedFalseAndDriverIsNull()).thenReturn(Lists.newArrayList(carDO));

		List<CarDO> activeCars = carService.findAllAvailableCars();
		assertEquals(1,  activeCars.size());
	}

	private CarDO getMockCar()
	{
		CarDO carDO = new CarDO();
		carDO.setEngineType(EngineType.DIESEL);
		carDO.setLicensePlate(TEST_LICENSE_PLATE);
		carDO.setEngineType(EngineType.DIESEL);
		carDO.setManufacturer(TEST_MANUFACTURER);
		carDO.setSeatCount(TEST_SEAT_COUNT);
		return carDO;
	}

	private CarDTO getMockCarDto()
	{
		CarDTO carDTO = new CarDTO();
		carDTO.setLicensePlate(TEST_LICENSE_PLATE);
		carDTO.setEngineType(EngineType.DIESEL);
		carDTO.setManufacturer(TEST_MANUFACTURER);
		carDTO.setSeatCount(TEST_SEAT_COUNT);
		return carDTO;
	}
}
