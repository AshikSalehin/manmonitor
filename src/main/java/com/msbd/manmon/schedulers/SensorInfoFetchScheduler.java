
package com.msbd.manmon.schedulers;

import com.msbd.manmon.sensorinfohandler.SensorInfoHandler;
import com.msbd.manmon.uttils.SensorHttpHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SensorInfoFetchScheduler {
    
    @Autowired
    SensorInfoHandler sensorInfoHandler;
    
    @Autowired
    private SimpMessagingTemplate msgTemplate;
    
    private String sensorInfo = "time";
    private String validityString = "{\"time\":";
    
    @Scheduled(fixedRate = 10000)
    public void fetchSensorData(){

	try {
	    sensorInfo = SensorHttpHandler.fetchDataFromControllerOnHttpGet("sensorinfo");

	    if (sensorInfo.startsWith(validityString)) {
//		msgTemplate.convertAndSend("/topic/sensorinfo", sensorInfo);
		sensorInfoHandler.sensorInfoHandler(sensorInfo);
	    }
	    
	} catch (Exception ex) {
	    java.util.logging.Logger.getLogger(SensorInfoFetchScheduler.class.getName()).log(Level.SEVERE, null, ex);
	}
	
    }
}
