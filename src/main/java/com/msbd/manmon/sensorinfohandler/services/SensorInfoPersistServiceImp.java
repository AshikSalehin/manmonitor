
package com.msbd.manmon.sensorinfohandler.services;

import com.google.gson.Gson;
import com.msbd.manmon.domainmodel.EnvironmentStatus;
import com.msbd.manmon.domainmodel.SensorMinMaxStatus;
import com.msbd.manmon.frontendmodels.EnvironmentInfo;
import com.msbd.manmon.frontendmodels.SesnsorMinMaxInfo;
import com.msbd.manmon.repository.EnvironmentStatusCrudRepository;
import com.msbd.manmon.repository.SesnsorMinMaxStatusCrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SensorInfoPersistServiceImp implements SensorInfoPersistService{
    
    @Autowired
    SesnsorMinMaxStatusCrudRepository sesnsorMinMaxStatusCrudRepository;
    
    @Autowired
    EnvironmentStatusCrudRepository environmentStatusCrudRepository;

    @Override
    public void sesnsorMinMaxInfoSave(SesnsorMinMaxInfo updatedSesnsorMinMaxInfo) {
	Gson gson = new Gson();
	String updatedSesnsorMinMaxInfoString;
	updatedSesnsorMinMaxInfoString = gson.toJson(updatedSesnsorMinMaxInfo);
	
	SensorMinMaxStatus sesnsorMinMaxStatusForSave = new SensorMinMaxStatus();
	sesnsorMinMaxStatusForSave.setTime(updatedSesnsorMinMaxInfo.getTime());
	sesnsorMinMaxStatusForSave.setSensorMinMaxStatus(updatedSesnsorMinMaxInfoString); 
	
	sesnsorMinMaxStatusCrudRepository.save(sesnsorMinMaxStatusForSave);
    }

    @Override
    public EnvironmentStatus saveSensorInfo(EnvironmentInfo prepareEnvironmentInfo) {
	Gson gson = new Gson();
	String sensorInfoToString;
	sensorInfoToString = gson.toJson(prepareEnvironmentInfo.getSensorInfo());
	
	EnvironmentStatus persistableEnvironmentStatus = new EnvironmentStatus();
	persistableEnvironmentStatus.setTime(prepareEnvironmentInfo.getTime());
	persistableEnvironmentStatus.setSensorInfo(sensorInfoToString);
	return environmentStatusCrudRepository.save(persistableEnvironmentStatus);
    }
    
}
