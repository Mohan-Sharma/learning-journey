package com.mytaxi.domainobject;

import java.util.List;
import java.util.Objects;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import com.google.common.collect.Lists;

import lombok.AllArgsConstructor;

/**
 * @author Mohan Sharma Created on 22/09/18.
 */
@AllArgsConstructor
public class DriverSpecification implements Specification<DriverDO>
{
	private DriverDO driver;

	@Override public Predicate toPredicate(Root<DriverDO> root, CriteriaQuery<?> query, CriteriaBuilder cb)
	{
		List<Predicate> predicates = Lists.newArrayList();
		if (StringUtils.isNotBlank(driver.getUsername())) {
			predicates.add(cb.like(cb.lower(root.get("username")), driver.getUsername().toLowerCase() + "%"));
		}
		if (Objects.nonNull(driver.getOnlineStatus())) {
			predicates.add(cb.equal(root.get("onlineStatus"), driver.getOnlineStatus().name()));
		}
		final CarDO car = driver.getCar();
		if (Objects.nonNull(car)) {
			Join<DriverDO, CarDO> carRoot = root.join("car");
			if(StringUtils.isNotBlank(car.getLicensePlate()))
			{
				predicates.add(cb.equal(carRoot.get("licensePlate"), car.getLicensePlate()));
			}
			if(Objects.nonNull(car.getRating()))
			{
				predicates.add(cb.equal(carRoot.get("rating"), car.getRating()));
			}
			if(Objects.nonNull(car.getEngineType()))
			{
				predicates.add(cb.equal(carRoot.get("engineType"), car.getEngineType()));
			}
			if(Objects.nonNull(car.getSeatCount()))
			{
				predicates.add(cb.equal(carRoot.get("seatCount"), car.getSeatCount()));
			}
			if(Objects.nonNull(car.getManufacturer()))
			{
				predicates.add(cb.equal(carRoot.get("manufacturer"), car.getManufacturer()));
			}
			if(Objects.nonNull(car.getConvertible()))
			{
				predicates.add(cb.equal(carRoot.get("convertible"), car.getConvertible()));
			}
		}
		return andTogether(predicates, cb);
	}


	private Predicate andTogether(List<Predicate> predicates, CriteriaBuilder cb) {
		return cb.and(predicates.toArray(new Predicate[0]));
	}
}
