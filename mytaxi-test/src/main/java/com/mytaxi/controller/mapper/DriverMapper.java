package com.mytaxi.controller.mapper;

import com.mytaxi.datatransferobject.DriverDTO;
import com.mytaxi.datatransferobject.SearchCriteriaDTO;
import com.mytaxi.domainobject.CarDO;
import com.mytaxi.domainobject.DriverDO;
import com.mytaxi.domainvalue.GeoCoordinate;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

public class DriverMapper
{
    public static DriverDO makeDriverDO(DriverDTO driverDTO)
    {
        DriverDO newDriver =  new DriverDO(driverDTO.getUsername(), driverDTO.getPassword());
        if(Objects.nonNull(driverDTO.getCoordinate()))
            newDriver.setCoordinate(driverDTO.getCoordinate());
        return newDriver;
    }


    public static DriverDTO makeDriverDTO(DriverDO driverDO)
    {
        DriverDTO.DriverDTOBuilder driverDTOBuilder = DriverDTO.newBuilder()
                .setId(driverDO.getId())
                .setPassword(driverDO.getPassword())
                .setUsername(StringUtils.EMPTY);

        GeoCoordinate coordinate = driverDO.getCoordinate();
        if (coordinate != null)
        {
            driverDTOBuilder.setCoordinate(coordinate);
        }

        CarDO car = driverDO.getCar();
        if (Objects.nonNull(car))
        {
            driverDTOBuilder.setCarDTO(CarMapper.makeCarDTO(car));
        }
        return driverDTOBuilder.createDriverDTO();
    }

    public static DriverDO constructDriverDO(SearchCriteriaDTO searchCriteriaDTO)
    {
        CarDO carDO = new CarDO();
        if(StringUtils.isNotBlank(searchCriteriaDTO.getLicensePlate()))
            carDO.setLicensePlate(searchCriteriaDTO.getLicensePlate());

        if(Objects.nonNull(searchCriteriaDTO.getConvertible()))
            carDO.setConvertible(searchCriteriaDTO.getConvertible());

        if(Objects.nonNull(searchCriteriaDTO.getRating()))
            carDO.setRating(searchCriteriaDTO.getRating());

        if(Objects.nonNull(searchCriteriaDTO.getEngineType()))
            carDO.setEngineType(searchCriteriaDTO.getEngineType());

        if(StringUtils.isNotBlank(searchCriteriaDTO.getManufacturer()))
            carDO.setManufacturer(searchCriteriaDTO.getManufacturer());

        if(Objects.nonNull(searchCriteriaDTO.getSeatCount()))
            carDO.setSeatCount(searchCriteriaDTO.getSeatCount());

        final DriverDO driverDO = new DriverDO();

        if(StringUtils.isNotBlank(searchCriteriaDTO.getUsername()))
            driverDO.setUsername(searchCriteriaDTO.getUsername());

        if(Objects.nonNull(searchCriteriaDTO.getOnlineStatus()))
            driverDO.setOnlineStatus(searchCriteriaDTO.getOnlineStatus());

        driverDO.setCar(carDO);
        return driverDO;
    }

    public static List<DriverDTO> makeDriverDTOList(Collection<DriverDO> drivers)
    {
        return drivers.stream()
                .map(DriverMapper::makeDriverDTO)
                .collect(Collectors.toList());
    }
}
