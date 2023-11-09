package com.mytaxi.spring.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.mytaxi.dataaccessobject.CarRepository;
import com.mytaxi.dataaccessobject.DriverRepository;
import com.mytaxi.service.car.CarService;
import com.mytaxi.service.car.DefaultCarService;
import com.mytaxi.service.driver.DefaultDriverService;
import com.mytaxi.service.driver.DriverService;

/**
 * @author Mohan Sharma Created on 25/09/18.
 */
@TestConfiguration
@EnableWebMvc
public class MyTaxiTestConfigs
{
}
