package com.tikal.gettingstarted.mongo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import io.swagger.annotations.ApiModelProperty;

public class LocationInfo {
	
	@Id
	@ApiModelProperty(hidden = true)
	private String id;
	
	@ApiModelProperty(required = true)
	private float latitude;
	
	@ApiModelProperty(required = true)
	private float longtitude;

	@ApiModelProperty
	private float altitude;

	@ApiModelProperty(hidden = true, required = false)
	@Field("employee")
	private Employee trackedEmployee;
	
	public LocationInfo() {
		super();
	}

	public LocationInfo(float latitude, float longtitude, float altitude, Employee trackedEmployee) {
		this();
		this.latitude = latitude;
		this.longtitude = longtitude;
		this.altitude = altitude;
		this.trackedEmployee = trackedEmployee;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public float getLongtitude() {
		return longtitude;
	}

	public void setLongtitude(float longtitude) {
		this.longtitude = longtitude;
	}

	public float getAltitude() {
		return altitude;
	}

	public void setAltitude(float altitude) {
		this.altitude = altitude;
	}

	public Employee getTrackedEmployee() {
		return trackedEmployee;
	}

	public void setTrackedEmployee(Employee trackedEmployee) {
		this.trackedEmployee = trackedEmployee;
	}
}
