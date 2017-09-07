package com.tikal.gettingstarted.mongo.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.tikal.gettingstarted.mongo.entity.LocationInfo;

public interface LocationInfoDao extends MongoRepository<LocationInfo, String> {
	
	public long countByTrackedEmployeeId(String id);
	
	public void deleteByTrackedEmployeeId(String id);

}
