package com.mytaxi.domainobject;

import java.time.ZonedDateTime;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Mohan Sharma Created on 20/09/18.
 */
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "car", uniqueConstraints = @UniqueConstraint(name = "uc_license_plate", columnNames = {"licensePlate"}))
@EqualsAndHashCode(of = "carId")
public class CarDO
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long carId;

	@Column(nullable = false)
	@NotNull(message = "Licence Plate of a car can not be null!")
	private String licensePlate;

	@Enumerated(EnumType.STRING)
	private CarRating rating;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	@NotNull(message = "Car must have an engine!")
	private EngineType engineType;

	@Column(nullable = false)
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private ZonedDateTime dateCreated;

	@OneToOne(mappedBy = "car", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
	private DriverDO driver;

	@ManyToMany(mappedBy = "cars")
	private Set<DriverDO> drivers;

	@Column(nullable = false)
	private Integer seatCount;

	@Column(nullable = false)
	private String manufacturer;

	private boolean deleted;

	private Boolean convertible;

	public enum CarRating
	{
		ONE_STAR("*"), TWO_STAR("* *"), THREE_STAR("* * *"), FOUR_STAR("* * * *"), FIVE_STAR("* * * * *");



		@Getter
		private String rating;

		CarRating(final String rating)
		{
			this.rating = rating;
		}
	}

	public enum EngineType
	{
		ELECTRIC, PETROL, DIESEL;
	}
}
