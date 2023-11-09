package com.mytaxi.datatransferobject;

import com.mytaxi.domainobject.CarDO.CarRating;
import com.mytaxi.domainobject.CarDO.EngineType;
import com.mytaxi.domainvalue.OnlineStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Mohan Sharma Created on 22/09/18.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchCriteriaDTO
{
	private String username;
	private OnlineStatus onlineStatus;
	private String licensePlate;
	private CarRating rating;
	private EngineType engineType;
	private Integer seatCount;
	private String manufacturer;
	private Boolean convertible;
}
