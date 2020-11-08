
package com.msbd.manmon.services;

import com.google.gson.Gson;
import com.msbd.manmon.config.NoiseDataFetchScheduler;
import com.msbd.manmon.frontendmodels.EnvironmentInfo;
import com.msbd.manmon.domainmodel.EnvironmentStatus;
import com.msbd.manmon.domainmodel.LastRelayStatus;
import com.msbd.manmon.domainmodel.SensorMinMaxStatus;
import com.msbd.manmon.frontendmodels.EnvironmentInfoPublish;
import com.msbd.manmon.frontendmodels.RelayInfo;
import com.msbd.manmon.frontendmodels.SensorThresholdInfo;
import com.msbd.manmon.frontendmodels.SesnsorMinMaxInfo;
import com.msbd.manmon.repository.EnvironmentStatusCrudRepository;
import com.msbd.manmon.repository.RelayStatusCrudRepository;
import com.msbd.manmon.repository.SesnsorMinMaxStatusCrudRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataPreparationService {
    @Autowired
    SesnsorMinMaxStatusCrudRepository sesnsorMinMaxStatusCrudRepository;
    @Autowired
    EnvironmentStatusCrudRepository environmentStatusCrudRepository;
    @Autowired
    RelayStatusCrudRepository relayStatusCrudRepository;
    
    public String environmentInfoPublishToJson(EnvironmentInfoPublish environmentInfoPublish){
	Gson gson = new Gson();
	String environmentInfoPublishJson;
	environmentInfoPublishJson = gson.toJson(environmentInfoPublish);
	return environmentInfoPublishJson;
    }
    
    public void seInitialRelayStatus() throws Exception{
	
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
    
    public String relayInfoToJson(RelayInfo initialRelayInfo){
	Gson gson = new Gson();
	String initialRelayStatus;
	initialRelayStatus = gson.toJson(initialRelayInfo);
	return initialRelayStatus;
    }
    
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
    
    public EnvironmentInfo prepareEnvironmentInfo(String responseData){       
        Gson responseGson = new Gson();
        EnvironmentInfo environmentInfo = responseGson.fromJson(responseData, EnvironmentInfo.class);
//        EnvironmentStatus environmentStatus = new EnvironmentStatus();
//        environmentStatus.setTime(envResponse.getTime());
//        environmentStatus.setTemperature(envResponse.getSensorInfo().getTemperature());
//        environmentStatus.setHumidity(envResponse.getSensorInfo().getHumidity());
//        environmentStatus.setLight(envResponse.getSensorInfo().getLight());
//        environmentStatus.setNoiseMaximum(envResponse.getSensorInfo().getNoiseMax());
//        environmentStatus.setNoiseMinimun(envResponse.getSensorInfo().getNoiseMin());
        return environmentInfo;
    }
    
//    public EnvironmentStatus prepareEnvironmentStatus(String responseData){       
//        Gson responseGson = new Gson();
//        EnvironmentInfo envResponse = responseGson.fromJson(responseData, EnvironmentInfo.class);
//        EnvironmentStatus environmentStatus = new EnvironmentStatus();
//        environmentStatus.setTime(envResponse.getTime());
//        environmentStatus.setTemperature(envResponse.getSensorInfo().getTemperature());
//        environmentStatus.setHumidity(envResponse.getSensorInfo().getHumidity());
//        environmentStatus.setLight(envResponse.getSensorInfo().getLight());
//        environmentStatus.setNoiseMaximum(envResponse.getSensorInfo().getNoiseMax());
//        environmentStatus.setNoiseMinimun(envResponse.getSensorInfo().getNoiseMin());
//        return environmentStatus;
//    }
    
    public RelayInfo prepareRelayInfo(String responseData){
        JSONObject responseJson = new JSONObject(responseData);
        Gson responseGson = new Gson();
        RelayInfo relayInfo = responseGson.fromJson(responseData, RelayInfo.class);
        return relayInfo;
    }
    
    public List<SensorMinMaxStatus> sensorMinMax(){
	List<SensorMinMaxStatus> sensorMinMaxStatuses = new ArrayList<>();
	sensorMinMaxStatuses = (List<SensorMinMaxStatus>) sesnsorMinMaxStatusCrudRepository.findAll();
	return sensorMinMaxStatuses;
    }
    
    public SensorThresholdInfo getSensorThreshold(){
	SensorThresholdInfo sensorThresholdInfo = new SensorThresholdInfo();
	sensorThresholdInfo.setTemperatureMin(20.0);
	sensorThresholdInfo.setTemperatureMax(35.00);
	sensorThresholdInfo.setHumidityMin(40.00);
	sensorThresholdInfo.setHumidityMax(85.00);
	sensorThresholdInfo.setLightMin(10.00);
	sensorThresholdInfo.setLightMax(150.00);
	return sensorThresholdInfo;
    }
    
    public List<EnvironmentInfo> getInitialSensorinfo(){
	EnvironmentStatus environmentStatus = new EnvironmentStatus();
	List<EnvironmentInfo> initialEnvironmentInfo = new ArrayList<>();
	//environmentStatusCrudRepository.findAll();
	
	
	return initialEnvironmentInfo;
    }
    
    public SesnsorMinMaxInfo prepareSesnsorMinMaxInfo(EnvironmentInfo environmentInfo, SesnsorMinMaxInfo sesnsorMinMaxInfo){
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
	
	
	return sesnsorMinMaxInfo;
    }
    
    public EnvironmentInfoPublish prepareEnvironmentInfoPublish(EnvironmentInfo environmentInfo, SesnsorMinMaxInfo sesnsorMinMaxInfo){
	
	EnvironmentInfoPublish environmentInfoPublish = new EnvironmentInfoPublish();
	
	environmentInfoPublish.setTime(environmentInfo.getTime());
	environmentInfoPublish.setSensorInfo(environmentInfo.getSensorInfo());
	environmentInfoPublish.setRelayInfo(environmentInfo.getRelayInfo());
	environmentInfoPublish.setSesnsorMinMaxInfo(sesnsorMinMaxInfo);
	
	return environmentInfoPublish;
    }
    
    public String prepareSensorInfo(String sensorInfoMain){
	final String temperatureString = "\"temperature\"";
	String tenSecondNoiseValue = Arrays.toString(NoiseDataFetchScheduler.noiseValueList());
	String beginString = sensorInfoMain.substring(0,sensorInfoMain.indexOf(temperatureString));
	String endString = sensorInfoMain.substring(sensorInfoMain.indexOf(temperatureString));
	return beginString + " \"noiseLevel\": " + tenSecondNoiseValue + ", " + endString;
    }
    
}
