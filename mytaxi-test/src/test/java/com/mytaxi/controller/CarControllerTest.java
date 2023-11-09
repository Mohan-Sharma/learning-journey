package com.mytaxi.controller;

import static com.mytaxi.MyTaxiTestConstants.TEST_LICENSE_PLATE;
import static com.mytaxi.MyTaxiTestConstants.TEST_MANUFACTURER;
import static com.mytaxi.MyTaxiTestConstants.TEST_SEAT_COUNT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import com.google.common.collect.Sets;
import com.mytaxi.dataaccessobject.CarRepository;
import com.mytaxi.datatransferobject.CarDTO;
import com.mytaxi.domainobject.CarDO;
import com.mytaxi.exception.ConstraintsViolationException;
import com.mytaxi.exception.EntityNotFoundException;
import com.mytaxi.service.car.DefaultCarService;

/**
 * @author Mohan Sharma Created on 26/09/18.
 */
@RunWith(SpringRunner.class)
@WebMvcTest(value = CarController.class, secure = false)
public class CarControllerTest
{
	@Autowired
	private MockMvc mockMvc;

	private ObjectMapper objectMapper;

	@MockBean
	private CarController carController;

	@MockBean
	private DefaultCarService carService;

	@MockBean
	private CarRepository carRepository;

	private CarDTO carDTO;
	private CarDO carDO;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		objectMapper = new ObjectMapper();
		objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

		carDTO = getMockCarDto();
		carDO = getMockCar();
	}


	@Test
	public void whenValidCarIdThenShouldReturnCarWith200HttpStatus() throws Exception
	{
		when(carController.getCar(any(Long.class))).thenReturn(carDTO);

		RequestBuilder requestBuilder = constructGetRequestBuilder("/v1/cars/1");

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		final CarDO foundCar = objectMapper.readValue(response.getContentAsByteArray(), CarDO.class);

		assertEquals(HttpStatus.OK.value(), response.getStatus());
		assertEquals("application/json;charset=UTF-8", response.getContentType());
		assertEquals(foundCar, carDO);
	}

	@Test
	public void whenInValidCarIdThenShouldReturn400HttpStatus() throws Exception
	{
		when(carController.getCar(any(Long.class))).thenThrow(EntityNotFoundException.class);

		RequestBuilder requestBuilder = constructGetRequestBuilder("/v1/cars/1");
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
		assertNull(response.getContentType());
	}

	@Test
	public void whenValidCarDataThenShouldCreateCarWith201HttpStatus() throws Exception
	{
		when(carController.createCar(carDTO)).thenReturn(carDTO);

		MvcResult result = postTransactionAndFetchResponse(objectMapper.writeValueAsString(carDTO), "/v1/cars");
		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.CREATED.value(), response.getStatus());
	}

	@Test
	public void whenInValidCarIdForDeleteThenShouldReturn400HttpStatus() throws Exception
	{
		doThrow(EntityNotFoundException.class).when(carController).deleteCar(any(Long.class));

		RequestBuilder requestBuilder = constructDeleteRequestBuilder("/v1/cars/1");
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
		assertNull(response.getContentType());
	}

	@Test
	public void whenValidCarIdForDeleteThenShouldReturn204HttpStatus() throws Exception
	{
		doNothing().when(carController).deleteCar(any(Long.class));

		RequestBuilder requestBuilder = constructDeleteRequestBuilder("/v1/cars/1");
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
		assertNull(response.getContentType());
	}

	@Test
	public void shouldReturnAllAvailableCars() throws Exception
	{
		when(carController.getAllAvailableCars()).thenReturn(Lists.newArrayList(carDTO));

		RequestBuilder requestBuilder = constructGetRequestBuilder("/v1/cars/allAvailableCars");
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		final List<CarDTO> foundCars = objectMapper.readValue(response.getContentAsByteArray(), new TypeReference<List<CarDTO>>(){});

		assertEquals(1, foundCars.size());
		assertEquals(HttpStatus.OK.value(), response.getStatus());
		assertEquals("application/json;charset=UTF-8", response.getContentType());
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

	private CarDTO getMockCarDto()
	{
		CarDTO carDTO = new CarDTO();
		carDTO.setLicensePlate(TEST_LICENSE_PLATE);
		carDTO.setEngineType(CarDO.EngineType.DIESEL);
		carDTO.setManufacturer(TEST_MANUFACTURER);
		carDTO.setSeatCount(TEST_SEAT_COUNT);
		return carDTO;
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
