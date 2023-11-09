package com.mytaxi.controller;

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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.collect.Lists;
import com.mytaxi.dataaccessobject.DriverRepository;
import com.mytaxi.datatransferobject.CarDTO;
import com.mytaxi.datatransferobject.DriverDTO;
import com.mytaxi.domainobject.CarDO;
import com.mytaxi.domainobject.DriverDO;
import com.mytaxi.domainvalue.GeoCoordinate;
import com.mytaxi.domainvalue.OnlineStatus;
import com.mytaxi.exception.CarAlreadyInUseException;
import com.mytaxi.exception.CarNotInUseException;
import com.mytaxi.exception.DriverConstraintsViolationException;
import com.mytaxi.exception.EntityNotFoundException;
import com.mytaxi.service.driver.DefaultDriverService;

/**
 * @author Mohan Sharma Created on 27/09/18.
 */
@RunWith(SpringRunner.class)
@WebMvcTest(value = DriverController.class, secure = false)
public class DriverControllerTest
{
	@Autowired
	private MockMvc mockMvc;

	private ObjectMapper objectMapper;

	@MockBean
	private DriverController driverController;

	@MockBean
	private DefaultDriverService driverService;

	@MockBean
	private DriverRepository driverRepository;

	private DriverDTO offlineDriver;
	private DriverDTO onlineDriver;
	private CarDTO carDTO;

	private static final String BASE_URL = "/v1/drivers";

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		objectMapper = new ObjectMapper();
		objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

		offlineDriver = getOfflineDriver();
		onlineDriver = getOnlineDriver();
		carDTO = getMockCarDto();
	}


	@Test
	public void whenUnknownEntityIdThenEntityNotFoundException() throws Exception
	{
		when(driverController.getDriver(any(Long.class))).thenThrow(EntityNotFoundException.class);
		RequestBuilder requestBuilder = constructGetRequestBuilder(BASE_URL + "/1");
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
		assertNull(response.getContentType());
	}

	@Test
	public void whenValidDriverIdThenShouldReturnDriverWith200HttpStatus() throws Exception
	{
		when(driverController.getDriver(any(Long.class))).thenReturn(offlineDriver);

		RequestBuilder requestBuilder = constructGetRequestBuilder(BASE_URL + "/1");

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		final DriverDTO foundDriver = objectMapper.readValue(response.getContentAsByteArray(), DriverDTO.class);

		assertEquals(HttpStatus.OK.value(), response.getStatus());
		assertEquals("application/json;charset=UTF-8", response.getContentType());
		assertEquals(foundDriver.getUsername(), offlineDriver.getUsername());
		assertEquals(foundDriver.getPassword(), offlineDriver.getPassword());
	}

	@Test
	public void whenValidDriverDataThenShouldCreateDriverWith201HttpStatus() throws Exception
	{
		when(driverController.createDriver(offlineDriver)).thenReturn(offlineDriver);

		MvcResult result = postTransactionAndFetchResponse(objectMapper.writeValueAsString(offlineDriver), BASE_URL);
		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.CREATED.value(), response.getStatus());
	}

	@Test
	public void whenInValidDriverIdForDeleteThenShouldReturn400HttpStatus() throws Exception
	{
		doThrow(EntityNotFoundException.class).when(driverController).deleteDriver(any(Long.class));

		RequestBuilder requestBuilder = constructDeleteRequestBuilder(BASE_URL + "/1");
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
		assertNull(response.getContentType());
	}

	@Test
	public void whenValidDriverIdForDeleteThenShouldReturn204HttpStatus() throws Exception
	{
		doNothing().when(driverController).deleteDriver(any(Long.class));

		RequestBuilder requestBuilder = constructDeleteRequestBuilder(BASE_URL + "/1");
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
		assertNull(response.getContentType());
	}


	@Test
	public void whenGivenStatusOfDriverIsOnlineThenReturnAllOnlineDriversEntityNotFoundException() throws Exception
	{
		when(driverController.findDrivers(OnlineStatus.ONLINE)).thenReturn(Lists.newArrayList(onlineDriver));
		RequestBuilder requestBuilder = constructGetRequestBuilder(BASE_URL + "?onlineStatus="+OnlineStatus.ONLINE);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		final List<DriverDTO> foundCars = objectMapper.readValue(response.getContentAsByteArray(), new TypeReference<List<DriverDTO>>(){});

		assertEquals(1, foundCars.size());
		assertEquals(HttpStatus.OK.value(), response.getStatus());
		assertEquals("application/json;charset=UTF-8", response.getContentType());
	}

	@Test
	public void whenGivenLatitudeAndLongitudeForDriverThenShouldUpdateGeoCoordinateOfDriver() throws Exception
	{
		doNothing().when(mock(DriverController.class)).updateLocation(any(Long.class), any(Long.class), any(Long.class));
		RequestBuilder requestBuilder = constructPutRequestBuilder(BASE_URL + "/1?longitude=" + TEST_LONGITUDE + "&" + "latitude=" + TEST_LATITUDE);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
		assertNull(response.getContentType());
	}


	@Test
	public void whenInValidIdThenShouldThrowEntityNotFoundExceptionWith400HttpStatus() throws Exception
	{
		when(driverController.selectCar(any(Long.class), any(Long.class))).thenThrow(EntityNotFoundException.class);

		MvcResult result = postTransactionAndFetchResponse(StringUtils.EMPTY, BASE_URL+ "/selectcar?driverId=" + DUMMY_ID + "&" + "carId=" + DUMMY_ID);
		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
		assertNull(response.getContentType());
	}

	@Test
	public void whenOfflineDriverTriesToSelectCarThenShouldThrowDriverConstraintsViolationExceptionWith400HttpStatus() throws Exception
	{
		when(driverController.selectCar(any(Long.class), any(Long.class))).thenThrow(DriverConstraintsViolationException.class);
		MvcResult result = postTransactionAndFetchResponse(StringUtils.EMPTY, BASE_URL+ "/selectcar?driverId=" + DUMMY_ID + "&" + "carId=" + DUMMY_ID);
		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
		assertNull(response.getContentType());
	}

	@Test
	public void whenDriverTriesToSelectCarAlreadyTakenByAnotherDriverThenShouldThrowCarAlreadyInUseExceptionWith400HttpStatus() throws Exception
	{
		when(driverController.selectCar(any(Long.class), any(Long.class))).thenThrow(CarAlreadyInUseException.class);
		MvcResult result = postTransactionAndFetchResponse(StringUtils.EMPTY, BASE_URL+ "/selectcar?driverId=" + DUMMY_ID + "&" + "carId=" + DUMMY_ID);
		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.CONFLICT.value(), response.getStatus());
		assertNull(response.getContentType());
	}

	@Test
	public void whenValidDriverTriesToSelectAvailableCarThenShouldReturnDriverWith200HttpStatus() throws Exception
	{
		onlineDriver.setCarDTO(carDTO);
		ResponseEntity<DriverDTO> responseEntity = new ResponseEntity<>(onlineDriver, HttpStatus.OK);
		when(driverController.selectCar(any(Long.class), any(Long.class))).thenReturn(responseEntity);
		MvcResult result = postTransactionAndFetchResponse(StringUtils.EMPTY, BASE_URL+ "/selectcar?driverId=" + DUMMY_ID + "&" + "carId=" + DUMMY_ID);
		MockHttpServletResponse response = result.getResponse();
		final DriverDTO foundDriver = objectMapper.readValue(response.getContentAsByteArray(), DriverDTO.class);

		assertEquals(HttpStatus.OK.value(), response.getStatus());
		assertEquals("application/json;charset=UTF-8", response.getContentType());
		assertEquals(foundDriver.getUsername(), onlineDriver.getUsername());
		assertEquals(foundDriver.getPassword(), onlineDriver.getPassword());
	}


	@Test
	public void whenInValidIdForDeselectCarThenShouldThrowEntityNotFoundExceptionWith400HttpStatus() throws Exception
	{
		when(driverController.deselectCar(any(Long.class), any(Long.class))).thenThrow(EntityNotFoundException.class);

		MvcResult result = postTransactionAndFetchResponse(StringUtils.EMPTY, BASE_URL+ "/deselectcar?driverId=" + DUMMY_ID + "&" + "carId=" + DUMMY_ID);
		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
		assertNull(response.getContentType());
	}

	@Test
	public void whenOfflineDriverTriesToDeselectCarThenShouldThrowDriverConstraintsViolationExceptionWith400HttpStatus() throws Exception
	{
		when(driverController.deselectCar(any(Long.class), any(Long.class))).thenThrow(DriverConstraintsViolationException.class);
		MvcResult result = postTransactionAndFetchResponse(StringUtils.EMPTY, BASE_URL+ "/deselectcar?driverId=" + DUMMY_ID + "&" + "carId=" + DUMMY_ID);
		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
		assertNull(response.getContentType());
	}

	@Test
	public void whenDriverWithoutHavingCarTriesToDeselectCarThenShouldThrowCarNotInUseExceptionWith400HttpStatus() throws Exception
	{
		when(driverController.deselectCar(any(Long.class), any(Long.class))).thenThrow(CarNotInUseException.class);
		MvcResult result = postTransactionAndFetchResponse(StringUtils.EMPTY, BASE_URL+ "/deselectcar?driverId=" + DUMMY_ID + "&" + "carId=" + DUMMY_ID);
		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
		assertNull(response.getContentType());
	}

	@Test
	public void whenOnlineDriverWithCarTriesToDeselectCarThenShouldReturnDriverWith200HttpStatus() throws Exception
	{
		ResponseEntity<DriverDTO> responseEntity = new ResponseEntity<>(onlineDriver, HttpStatus.OK);
		when(driverController.deselectCar(any(Long.class), any(Long.class))).thenReturn(responseEntity);
		MvcResult result = postTransactionAndFetchResponse(StringUtils.EMPTY, BASE_URL+ "/deselectcar?driverId=" + DUMMY_ID + "&" + "carId=" + DUMMY_ID);
		MockHttpServletResponse response = result.getResponse();
		final DriverDTO foundDriver = objectMapper.readValue(response.getContentAsByteArray(), DriverDTO.class);

		assertEquals(HttpStatus.OK.value(), response.getStatus());
		assertEquals("application/json;charset=UTF-8", response.getContentType());
		assertEquals(foundDriver.getUsername(), onlineDriver.getUsername());
		assertEquals(foundDriver.getPassword(), onlineDriver.getPassword());
		assertNull(foundDriver.getCarDTO());
	}

	private DriverDTO getOnlineDriver()
	{
		DriverDTO.DriverDTOBuilder driverDTOBuilder = DriverDTO.newBuilder()
				.setId(1L)
				.setPassword(ONLINE_DRIVER_USERNAME)
				.setUsername(ONLINE_DRIVER_PWD);
		return driverDTOBuilder.createDriverDTO();
	}

	private DriverDTO getOfflineDriver()
	{
		DriverDTO.DriverDTOBuilder driverDTOBuilder = DriverDTO.newBuilder()
				.setId(2L)
				.setPassword(OFFLINE_DRIVER_USERNAME)
				.setUsername(OFFLINE_DRIVER_PWD);
		return driverDTOBuilder.createDriverDTO();
	}

	private CarDTO getMockCarDto()
	{
		CarDTO carDTO = new CarDTO();
		carDTO.setLicensePlate(TEST_LICENSE_PLATE);
		carDTO.setEngineType(CarDO.EngineType.DIESEL);
		carDTO.setManufacturer(TEST_MANUFACTURER);
		carDTO.setSeatCount(TEST_SEAT_COUNT);
		return carDTO;
	}

	private MvcResult postTransactionAndFetchResponse(final String body, final String url) throws Exception
	{
		RequestBuilder requestBuilder =
				MockMvcRequestBuilders
						.post(url)
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)
						.content(body);
		return mockMvc.perform(requestBuilder).andReturn();
	}


	private RequestBuilder constructGetRequestBuilder(final String url)
	{
		return MockMvcRequestBuilders
				.get(url)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON);
	}

	private RequestBuilder constructDeleteRequestBuilder(final String url)
	{
		return MockMvcRequestBuilders
				.delete(url)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON);
	}

	private RequestBuilder constructPutRequestBuilder(final String url)
	{
		return MockMvcRequestBuilders
				.put(url)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON);
	}
}
