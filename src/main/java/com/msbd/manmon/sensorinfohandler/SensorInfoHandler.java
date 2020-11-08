
package com.msbd.manmon.sensorinfohandler;

import com.msbd.manmon.domainmodel.EnvironmentStatus;
import com.msbd.manmon.domainmodel.LastRelayStatus;
import com.msbd.manmon.frontendmodels.EnvironmentInfo;
import com.msbd.manmon.frontendmodels.EnvironmentInfoPublish;
import com.msbd.manmon.frontendmodels.RelayInfo;
import com.msbd.manmon.frontendmodels.SesnsorMinMaxInfo;
import com.msbd.manmon.httphandler.HttpRequestHandlerServiceImp;
import com.msbd.manmon.sensorinfohandler.services.SensorInfoPersistServiceImp;
import com.msbd.manmon.sensorinfohandler.services.SensorInfoPreparationServiceImp;
import com.msbd.manmon.uttils.SensorHttpHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
@Service
public class SensorInfoHandler {
    
    private SesnsorMinMaxInfo currentSesnsorMinMaxInfo = new SesnsorMinMaxInfo();
    
    @Autowired
    SensorInfoPreparationServiceImp sensorInfoPreparationServiceImp;
    
    @Autowired
    SensorInfoPersistServiceImp sensorInfoPersistServiceImp;
    
    @Autowired
    private SimpMessagingTemplate msgTemplate;
    
//    @Autowired
//    private static HttpRequestHandlerServiceImp httpRequestHandlerServiceImp;
    
//    private static String currentDate = "date";
    
    @PostConstruct
    public void sensorInfoHandlerPostConstruct() throws Exception{
	
	sensorInfoPreparationServiceImp.setInitialRelayStatus();
	
	String currentDate = sensorInfoPreparationServiceImp.getCurrentDate();
	currentSesnsorMinMaxInfo = sensorInfoPreparationServiceImp.getCurrentSesnsorMinMaxInfo(currentDate);
    }
    
    @SendTo("/topic/sensorinfo")
    public void sensorInfoHandler(String sensorInfo){
	
	sensorInfo = sensorInfoPreparationServiceImp.prepareSensorInfo(sensorInfo);
	
//	msgTemplate.convertAndSend("/topic/sensorinfo", sensorInfo);
	
	EnvironmentInfo prepareEnvironmentInfo = new EnvironmentInfo();
	prepareEnvironmentInfo = sensorInfoPreparationServiceImp.prepareEnvironmentInfo(sensorInfo);
	
	RelayInfo currentRelayInfo = prepareEnvironmentInfo.getRelayInfo();
	RelayInfo lastRelayInfo = sensorInfoPreparationServiceImp.getLastRelayInfo();
	
	if(!lastRelayInfo.equals(currentRelayInfo)){
	    if(lastRelayInfo.getReplay01() == "0"){
		try {
		    SensorHttpHandler.fetchDataFromControllerOnHttpGet("setlampdown");
		    prepareEnvironmentInfo.getRelayInfo().setReplay01("0");
		} catch (Exception ex) {
		    Logger.getLogger(SensorInfoHandler.class.getName()).log(Level.SEVERE, null, ex);
		}
	    }
	    else if(lastRelayInfo.getReplay01() == "1"){
		try {
		    SensorHttpHandler.fetchDataFromControllerOnHttpGet("setlampup");
		    prepareEnvironmentInfo.getRelayInfo().setReplay01("1");
		} catch (Exception ex) {
		    Logger.getLogger(SensorInfoHandler.class.getName()).log(Level.SEVERE, null, ex);
		}
	    }
	    if(prepareEnvironmentInfo.getSensorInfo().getHumidity() > 45.00){
		try {
		    SensorHttpHandler.fetchDataFromControllerOnHttpGet("setdehumidifierup");
		    prepareEnvironmentInfo.getRelayInfo().setReplay02("1");
		} catch (Exception ex) {
		    Logger.getLogger(SensorInfoHandler.class.getName()).log(Level.SEVERE, null, ex);
		}
	    }
	    else{
		try {
		    SensorHttpHandler.fetchDataFromControllerOnHttpGet("setdehumidifierdown");
		    prepareEnvironmentInfo.getRelayInfo().setReplay02("0");
		} catch (Exception ex) {
		    Logger.getLogger(SensorInfoHandler.class.getName()).log(Level.SEVERE, null, ex);
		}
	    }
		
	}
	
	SesnsorMinMaxInfo updatedSesnsorMinMaxInfo = new SesnsorMinMaxInfo();
	
	if(currentSesnsorMinMaxInfo.getTime().substring(6).equals(prepareEnvironmentInfo.getTime().substring(6,8))){
	    currentSesnsorMinMaxInfo = sensorInfoPreparationServiceImp.prepareSesnsorMinMaxInfo(prepareEnvironmentInfo, currentSesnsorMinMaxInfo, true);
	}
	else{
	    currentSesnsorMinMaxInfo = sensorInfoPreparationServiceImp.prepareSesnsorMinMaxInfo(prepareEnvironmentInfo, currentSesnsorMinMaxInfo, false);
	}
	
	sensorInfoPersistServiceImp.sesnsorMinMaxInfoSave(currentSesnsorMinMaxInfo);
	
	EnvironmentInfoPublish environmentInfoPublish = new EnvironmentInfoPublish();
	environmentInfoPublish = sensorInfoPreparationServiceImp.prepareEnvironmentInfoPublish(prepareEnvironmentInfo, currentSesnsorMinMaxInfo);
	
	String environmentStatusToPublish = sensorInfoPreparationServiceImp.environmentInfoPublishToString(environmentInfoPublish);
	msgTemplate.convertAndSend("/topic/sensorinfo", environmentStatusToPublish);
	
	sensorInfoPersistServiceImp.saveSensorInfo(prepareEnvironmentInfo);
	

    }
}
