
package com.msbd.manmon.sensorinfohandler.services;

import com.google.gson.Gson;
import com.msbd.manmon.domainmodel.LastRelayStatus;
import com.msbd.manmon.domainmodel.SensorMinMaxStatus;
import com.msbd.manmon.frontendmodels.EnvironmentInfo;
import com.msbd.manmon.frontendmodels.EnvironmentInfoPublish;
import com.msbd.manmon.frontendmodels.RelayInfo;
import com.msbd.manmon.frontendmodels.SesnsorMinMaxInfo;
import com.msbd.manmon.repository.EnvironmentStatusCrudRepository;
import com.msbd.manmon.repository.RelayStatusCrudRepository;
import com.msbd.manmon.repository.SesnsorMinMaxStatusCrudRepository;
import com.msbd.manmon.schedulers.NoiseInfoFetchScheduler;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SensorInfoPreparationServiceImp implements SensorInfoPreparationService{
    
    @Autowired
    RelayStatusCrudRepository relayStatusCrudRepository;
    @Autowired
    SesnsorMinMaxStatusCrudRepository sesnsorMinMaxStatusCrudRepository;
    @Autowired
    EnvironmentStatusCrudRepository environmentStatusCrudRepository;
    @Autowired
    NoiseInfoFetchScheduler noiseInfoFetchScheduler;

    @Override
    public String getCurrentDate(){
	DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
	LocalDateTime now = LocalDateTime.now();
	String currentDate = dateFormatter.format(now);
	return currentDate;
    }

    @Override
    public RelayInfo getLastRelayInfo() {
	
	LastRelayStatus getLastRelayStatus = new LastRelayStatus();
	getLastRelayStatus = relayStatusCrudRepository.findById("relayStatusId").get();
	
	Gson gson = new Gson();
	RelayInfo lastRelayInfo = new RelayInfo();
	lastRelayInfo = gson.fromJson(getLastRelayStatus.getLastRelayStatus(), RelayInfo.class);
	return lastRelayInfo;
    }
    
    @Override
    public SesnsorMinMaxInfo getCurrentSesnsorMinMaxInfo(String currentDate){
		
	SesnsorMinMaxInfo currentSesnsorMinMaxInfo = new SesnsorMinMaxInfo();
	currentSesnsorMinMaxInfo.setTime(currentDate);
	currentSesnsorMinMaxInfo.setTemperatureMin(22.10);
	currentSesnsorMinMaxInfo.setTemperatureMax(27.20);
	currentSesnsorMinMaxInfo.setHumidityMin(80.10);
	currentSesnsorMinMaxInfo.setHumidityMax(85.22);
	currentSesnsorMinMaxInfo.setLightMin(20.02);
	currentSesnsorMinMaxInfo.setLightMax(40.67);
	currentSesnsorMinMaxInfo.setNoiseMin(40.0);
	currentSesnsorMinMaxInfo.setNoiseMax(65.0);
	currentSesnsorMinMaxInfo.setWaterMin(0.0);
	currentSesnsorMinMaxInfo.setWaterMax(0.4);
	//currentSesnsorMinMaxInfo.setTime(currentDate);
	
	Gson gson = new Gson();
	String initialSesnsorMinMaxInfoString;
	initialSesnsorMinMaxInfoString = gson.toJson(currentSesnsorMinMaxInfo);

	SensorMinMaxStatus sesnsorMinMaxStatusForSave = new SensorMinMaxStatus();
	sesnsorMinMaxStatusForSave.setTime(currentDate);
	sesnsorMinMaxStatusForSave.setSensorMinMaxStatus(initialSesnsorMinMaxInfoString);
	
	sesnsorMinMaxStatusCrudRepository.save(sesnsorMinMaxStatusForSave);
	
	return currentSesnsorMinMaxInfo;
	
//	SensorMinMaxStatus currentDateSensorMinMaxStatus = new SensorMinMaxStatus();
//	currentDateSensorMinMaxStatus = sesnsorMinMaxStatusCrudRepository.findById(currentDate).get();
//	return currentDateSensorMinMaxStatus;
    }

    @Override
    public String prepareSensorInfo(String sensorInfoMain) {
	final String temperatureString = "\"temperature\"";
	String tenSecondNoiseValue = Arrays.toString(noiseInfoFetchScheduler.noiseValueList());
	String beginString = sensorInfoMain.substring(0,sensorInfoMain.indexOf(temperatureString));
	String endString = sensorInfoMain.substring(sensorInfoMain.indexOf(temperatureString));
	return beginString + " \"noiseLevel\": " + tenSecondNoiseValue + ", " + endString;
    }

    @Override
    public EnvironmentInfo prepareEnvironmentInfo(String responseData) {
	Gson responseGson = new Gson();
        EnvironmentInfo environmentInfo = responseGson.fromJson(responseData, EnvironmentInfo.class);
        return environmentInfo;
    }
    
    @Override
    public SesnsorMinMaxInfo prepareSesnsorMinMaxInfo(EnvironmentInfo environmentInfo, SesnsorMinMaxInfo sesnsorMinMaxInfo, boolean state){
	
	if(state){
	    if(environmentInfo.getSensorInfo().getTemperature() < sesnsorMinMaxInfo.getTemperatureMin()){
		sesnsorMinMaxInfo.setTemperatureMin(environmentInfo.getSensorInfo().getTemperature());
	    }
	    else if(environmentInfo.getSensorInfo().getTemperature() > sesnsorMinMaxInfo.getTemperatureMax()){
		sesnsorMinMaxInfo.setTemperatureMax(environmentInfo.getSensorInfo().getTemperature());
	    }
	    if(environmentInfo.getSensorInfo().getHumidity() < sesnsorMinMaxInfo.getHumidityMin()){
		sesnsorMinMaxInfo.setHumidityMin(environmentInfo.getSensorInfo().getHumidity());
	    }
	    else if(environmentInfo.getSensorInfo().getHumidity() > sesnsorMinMaxInfo.getHumidityMax()){
		sesnsorMinMaxInfo.setHumidityMax(environmentInfo.getSensorInfo().getHumidity());
	    }
	    if(environmentInfo.getSensorInfo().getLight() < sesnsorMinMaxInfo.getLightMin()){
		sesnsorMinMaxInfo.setLightMin(environmentInfo.getSensorInfo().getLight());
	    }
	    else if(environmentInfo.getSensorInfo().getLight() > sesnsorMinMaxInfo.getLightMax()){
		sesnsorMinMaxInfo.setLightMax(environmentInfo.getSensorInfo().getLight());
	    }
	}
	else{
	    sesnsorMinMaxInfo.setTemperatureMin(environmentInfo.getSensorInfo().getTemperature());
	    sesnsorMinMaxInfo.setHumidityMin(environmentInfo.getSensorInfo().getHumidity());
	    sesnsorMinMaxInfo.setLightMin(environmentInfo.getSensorInfo().getLight());
	    sesnsorMinMaxInfo.setTime(environmentInfo.getTime().substring(0, 8));
	}
	

	return sesnsorMinMaxInfo;
    }
    
    @Override
    public EnvironmentInfoPublish prepareEnvironmentInfoPublish(EnvironmentInfo environmentInfo, SesnsorMinMaxInfo sesnsorMinMaxInfo){
	
	EnvironmentInfoPublish environmentInfoPublish = new EnvironmentInfoPublish();
	
	environmentInfoPublish.setTime(environmentInfo.getTime());
	environmentInfoPublish.setSensorInfo(environmentInfo.getSensorInfo());
	environmentInfoPublish.setRelayInfo(environmentInfo.getRelayInfo());
	environmentInfoPublish.setSesnsorMinMaxInfo(sesnsorMinMaxInfo);
	
	return environmentInfoPublish;
    }

    @Override
    public String environmentInfoPublishToString(EnvironmentInfoPublish environmentInfoPublish) {
	Gson gson = new Gson();
	String environmentInfoPublishToString;
	environmentInfoPublishToString = gson.toJson(environmentInfoPublish);
	return environmentInfoPublishToString;
    }

    @Override
    public RelayInfo isRelayStatusAlright(RelayInfo currentRelayInfo) {
	return currentRelayInfo;
    }

    @Override
    public void setInitialRelayStatus() {
	RelayInfo initialRelayInfo = new RelayInfo();
	initialRelayInfo.setReplay01("0");
	initialRelayInfo.setReplay02("0");
	initialRelayInfo.setReplay03("0");
	initialRelayInfo.setReplay04("0");
	
	Gson gson = new Gson();
	String initialRelayStatusString;
	initialRelayStatusString = gson.toJson(initialRelayInfo);
	
	LastRelayStatus initialRelayStatus = new LastRelayStatus();
	initialRelayStatus.setLastRelayStatus(initialRelayStatusString);
	
	LastRelayStatus saveIniLastRelayStatus = new LastRelayStatus();
	saveIniLastRelayStatus = relayStatusCrudRepository.save(initialRelayStatus);
    }


    
}
