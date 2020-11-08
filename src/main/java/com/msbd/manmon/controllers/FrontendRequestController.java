/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.msbd.manmon.controllers;

import com.google.gson.Gson;
import com.msbd.manmon.domainmodel.EnvironmentStatus;
import com.msbd.manmon.domainmodel.SensorMinMaxStatus;
import com.msbd.manmon.frontendmodels.SensorThresholdInfo;
import com.msbd.manmon.httphandler.HttpRequestHandlerServiceImp;
import com.msbd.manmon.repository.EnvironmentStatusCrudRepository;
import com.msbd.manmon.repository.EnvironmentStatusPageRepository;
import com.msbd.manmon.repository.RelayStatusCrudRepository;
import com.msbd.manmon.repository.SesnsorMinMaxStatusCrudRepository;
import com.msbd.manmon.repository.EnvironmentStatusJpaRepository;
import com.msbd.manmon.services.DataPreparationService;
import com.msbd.manmon.uttils.SensorHttpHandler;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
public class FrontendRequestController {
    @Autowired
    DataPreparationService dataPreparationService;

    @Autowired
    EnvironmentStatusCrudRepository environmentStatusCrudRepository;
    @Autowired
    RelayStatusCrudRepository relayStatusCrudRepository;
    @Autowired
    SesnsorMinMaxStatusCrudRepository sesnsorMinMaxStatusCrudRepository;
    @Autowired
    EnvironmentStatusPageRepository environmentStatusPageRepository;
    @Autowired
    EnvironmentStatusJpaRepository environmentStatusJpaRepository;
    
//    @Autowired
//    private static HttpRequestHandlerServiceImp httpRequestHandlerServiceImp;
//    @Autowired
//    private SimpMessagingTemplate msgTemplate;
//    
//    @RequestMapping(method = RequestMethod.POST , value="/senorhello")
//    @SendTo("/topic/greetings")
//    public EnvironmentStatus loadServiceDef() throws Exception {
//        
//        EnvironmentStatus grtng = new EnvironmentStatus();
//        
//        msgTemplate.convertAndSend("/topic/greetings", grtng);
//        
//        return grtng;
//        
//    }
    
    
    @MessageMapping("/sensorinfo")
    public String publishToFrontend(String status) throws Exception {
      
      return status;
      
    }
    
//    @MessageMapping("/instruction_to_backend")
//    @SendTo("/topic/publish_to_frontend")
//    public String instructionToBackend(String instruction) throws Exception {
//      
//      return instruction;
//      
//    } 
    
    //@MessageMapping("/setdehumidifierup")
    @RequestMapping(method = RequestMethod.GET , value="/setdehumidifierup")
    public String setdehumidifierup() throws Exception {
        
        return SensorHttpHandler.fetchDataFromControllerOnHttpGet("setdehumidifierup");
        
    }
    
    //@MessageMapping("/setdehumidifierdown")
    @RequestMapping(method = RequestMethod.GET , value="/setdehumidifierdown")
    public String setdehumidifierdown() throws Exception {
        
        return SensorHttpHandler.fetchDataFromControllerOnHttpGet("setdehumidifierdown");
        
    }
    
    //@MessageMapping("/setlampup")
    @RequestMapping(method = RequestMethod.GET , value="/setlampup")
    public String setlampup() throws Exception {
        
        return SensorHttpHandler.fetchDataFromControllerOnHttpGet("setlampup");
        
    }

    //@MessageMapping("/setlampdown")
    @RequestMapping(method = RequestMethod.GET , value="/setlampdown")
    public String setlampdown() throws Exception {
        
        return SensorHttpHandler.fetchDataFromControllerOnHttpGet("setlampdown");
        //log.info("Response data: "+ sensotInfo);
        
    }
    
    @RequestMapping(method = RequestMethod.GET , value="/current_date_min_max")
    public String sensorMinMax() throws Exception {
        
        //return dataPreparationService.getSensorThreshold();
	List<SensorMinMaxStatus> thirtyDaySensorMinMaxStatus = new ArrayList<>();
	thirtyDaySensorMinMaxStatus = (List<SensorMinMaxStatus>) sesnsorMinMaxStatusCrudRepository.findAll();
	
	Gson gson = new Gson();
	String thirtyDaySensorMinMaxStatusString;
	thirtyDaySensorMinMaxStatusString = gson.toJson(thirtyDaySensorMinMaxStatus);
	
	return  thirtyDaySensorMinMaxStatusString;
	
    }
    
    @RequestMapping(method = RequestMethod.GET , value="/initial_sensorinfo")
    public String initialSensorinfo() throws Exception {
        
	//Page<EnvironmentStatus> initialSensorInfo = new ArrayList<>();
	List<EnvironmentStatus> abcd = new ArrayList<>();
	Pageable initialPage;
	
	if(environmentStatusPageRepository.count() >= 90){
	    initialPage = (Pageable) PageRequest.of(0, 90, Sort.Direction.DESC, "time");
	    //abcd = environmentStatusPageRepository.findTop90();
	    
	}
	else{
	    initialPage = (Pageable) PageRequest.of(0, (int) environmentStatusPageRepository.count(), Sort.Direction.DESC, "time");
	    //abcd = environmentStatusPageRepository.findTop3();
	}
	
	Page<EnvironmentStatus> initialSensorInfo = environmentStatusPageRepository.findAll(initialPage);
	
	Gson gson = new Gson();
	String initialSensorInfoString;
	initialSensorInfoString = gson.toJson(initialSensorInfo.getContent());
	
        return initialSensorInfoString;
    }
    
    @RequestMapping(method = RequestMethod.GET , value="/threshold")
    public String initialThreshold(){
	SensorThresholdInfo initialSensorThresholdInfo = new SensorThresholdInfo();
	initialSensorThresholdInfo = dataPreparationService.getSensorThreshold();
	
	Gson gson = new Gson();
	String initialSensorThresholdInfoString;
	initialSensorThresholdInfoString = gson.toJson(initialSensorThresholdInfo);
	
	return initialSensorThresholdInfoString;
	
    }
    
    @RequestMapping(method = RequestMethod.GET , value="/specific_date_hour_sensorinfo")
    public String getSpecicificDateHourSensorInfo(@RequestParam("dateHour") String dateHour){
	List<EnvironmentStatus> specificDateHourEnvironmentStatuse = new ArrayList<>();
	specificDateHourEnvironmentStatuse = environmentStatusJpaRepository.findByTimeStartingWith(dateHour);
	Gson gson = new Gson();
	String initialSensorInfoString;
	initialSensorInfoString = gson.toJson(specificDateHourEnvironmentStatuse);
	
        return initialSensorInfoString;
    }
    
}
