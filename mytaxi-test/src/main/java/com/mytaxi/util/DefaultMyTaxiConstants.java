package com.mytaxi.util;

/**
 * @author Mohan Sharma Created on 24/09/18.
 */
public class DefaultMyTaxiConstants
{
	public static final String OAUTH_CLIENT = "my_taxi";
	public static final String OAUTH_SECRET = "secret";
	public static final String OAUTH_READ = "read";
	public static final String OAUTH_WRITE = "write";
	public static final String OAUTH_GRANT_PASSWORD = "password";
	public static final String OAUTH_GRANT_REFRESH = "refresh_token";
	public static final String RESOURCE_ID = "my_taxi_api";
	public static final String DRIVER_OFFLINE_EXCEPTION = "Driver with Id: %d is offline, only online drivers can select or deselect a car!!";
	public static final String DRIVER_ALREADY_USING_CAR_EXCEPTION = "Driver having id: %d is already using car with id: %d, please deselect the current car to select a new car!!";
	public static final String DRIVER_NOT_FOUND_EXCEPTION = "No active driver found with the given id: %d";
	public static final String CAR_NOT_FOUND_EXCEPTION = "No active car found with the given id: %d";
	public static final String CAR_IN_USE_EXCEPTION = "Car having id: %d is being used by some other driver, please select a new car!!";
	public static final String DRIVER_NOT_USING_CAR_EXCEPTION = "Driver having id: %d is currently not using any car!!";
}
