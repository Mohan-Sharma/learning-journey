package com.mytaxi.controller;

import com.mytaxi.controller.mapper.DriverMapper;
import com.mytaxi.datatransferobject.DriverDTO;
import com.mytaxi.datatransferobject.SearchCriteriaDTO;
import com.mytaxi.domainobject.DriverDO;
import com.mytaxi.domainvalue.OnlineStatus;
import com.mytaxi.exception.CarAlreadyInUseException;
import com.mytaxi.exception.CarNotInUseException;
import com.mytaxi.exception.ConstraintsViolationException;
import com.mytaxi.exception.DriverConstraintsViolationException;
import com.mytaxi.exception.EntityNotFoundException;
import com.mytaxi.service.driver.DriverService;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

/**
 * All operations with a driver will be routed by this controller.
 * <p/>
 */
@RestController
@Validated
@AllArgsConstructor
@RequestMapping("v1/drivers")
public class DriverController
{

    private final DriverService driverService;

    @GetMapping("/{driverId}")
    @ResponseStatus(HttpStatus.OK)
    public DriverDTO getDriver(@PathVariable("driverId") long driverId) throws EntityNotFoundException
    {
        return DriverMapper.makeDriverDTO(driverService.find(driverId));
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DriverDTO createDriver(@Valid @RequestBody DriverDTO driverDTO) throws ConstraintsViolationException
    {
        DriverDO driverDO = DriverMapper.makeDriverDO(driverDTO);
        return DriverMapper.makeDriverDTO(driverService.create(driverDO));
    }


    @DeleteMapping("/{driverId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDriver(@PathVariable("driverId") long driverId) throws EntityNotFoundException
    {
        driverService.delete(driverId);
    }


    @PutMapping("/{driverId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateLocation(
            @PathVariable("driverId") long driverId, @RequestParam("longitude") double longitude, @RequestParam("latitude") double latitude)
            throws EntityNotFoundException
    {
        driverService.updateLocation(driverId, longitude, latitude);
    }


    @GetMapping
    public List<DriverDTO> findDrivers(@RequestParam("onlineStatus") OnlineStatus onlineStatus)
    {
        return DriverMapper.makeDriverDTOList(driverService.find(onlineStatus));
    }

    @PostMapping("/selectcar")
    public ResponseEntity<DriverDTO> selectCar(@RequestParam("driverId") long driverId, @RequestParam("carId") long carId)
            throws EntityNotFoundException, CarAlreadyInUseException, DriverConstraintsViolationException
    {
        final DriverDO driver = driverService.selectCar(driverId, carId);
        return new ResponseEntity<>(DriverMapper.makeDriverDTO(driver), HttpStatus.OK);
    }

    @PostMapping("/deselectcar")
    public ResponseEntity<DriverDTO> deselectCar(@RequestParam("driverId") long driverId, @RequestParam("carId") long carId)
            throws EntityNotFoundException, CarNotInUseException, DriverConstraintsViolationException
    {
        final DriverDO driver = driverService.deselectCar(driverId, carId);
        return new ResponseEntity<>(DriverMapper.makeDriverDTO(driver), HttpStatus.OK);
    }

    @PostMapping("/searchByCriteria")
    @ResponseStatus(HttpStatus.OK)
    public List<DriverDTO> searchDriversByCriteria(@Valid @RequestBody SearchCriteriaDTO searchCriteriaDTO) throws ConstraintsViolationException
    {
        DriverDO driverDO = DriverMapper.constructDriverDO(searchCriteriaDTO);
        List<DriverDO> drivers =  driverService.searchDriversByCriteria(driverDO);
        return DriverMapper.makeDriverDTOList(drivers);
    }
}


