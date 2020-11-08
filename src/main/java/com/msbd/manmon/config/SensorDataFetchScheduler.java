//package com.msbd.manmon.config;
//
//import com.google.gson.Gson;
//import com.msbd.manmon.domainmodel.EnvironmentStatus;
//import com.msbd.manmon.domainmodel.LastRelayStatus;
//import com.msbd.manmon.domainmodel.SensorMinMaxStatus;
//import com.msbd.manmon.domainmodel.DatabseConstraints;
//import com.msbd.manmon.frontendmodels.EnvironmentInfo;
//import com.msbd.manmon.frontendmodels.EnvironmentInfoPublish;
//import com.msbd.manmon.frontendmodels.RelayInfo;
//import com.msbd.manmon.frontendmodels.SesnsorMinMaxInfo;
//import com.msbd.manmon.services.DataPreparationService;
//import com.msbd.manmon.uttils.SensorHttpHandler;
//import java.text.SimpleDateFormat;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//import org.springframework.messaging.handler.annotation.SendTo;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import com.msbd.manmon.repository.EnvironmentStatusCrudRepository;
//import com.msbd.manmon.repository.EnvironmentStatusJpaRepository;
//import com.msbd.manmon.repository.RelayStatusCrudRepository;
//import com.msbd.manmon.repository.SesnsorMinMaxStatusCrudRepository;
//import java.time.Instant;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.time.temporal.TemporalAccessor;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.GregorianCalendar;
//import java.util.List;
//import java.util.Optional;
//import javax.annotation.PostConstruct;
//import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
//import java.util.logging.Level;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.annotation.EnableScheduling;
////@Configuration
////@EnableScheduling
//
//@Component
//public class SensorDataFetchScheduler {
//
//    @Autowired
//    private SimpMessagingTemplate msgTemplate;
//    @Autowired
//    DataPreparationService dataPreparationService;
//    @Autowired
//    EnvironmentStatusCrudRepository environmentStatusCrudRepository;
//    @Autowired
//    RelayStatusCrudRepository relayStatusCrudRepository;
//    @Autowired
//    SesnsorMinMaxStatusCrudRepository sesnsorMinMaxStatusCrudRepository;
//    @Autowired
//    EnvironmentStatusJpaRepository environmentStatusJpaRepository;
//    @Autowired
//
//    private Logger log = LoggerFactory.getLogger(SensorDataFetchScheduler.class);
//    private SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
//    //fetch currentdate minmax from DB
//    private SesnsorMinMaxInfo currentSesnsorMinMaxInfo = new SesnsorMinMaxInfo();
//    private String currentDate;
//    private Long totalRowSensorStatusTable;
//    private Long totalRowSensorMinMaxStatusTable;
//    private final DatabseConstraints databseConstraints = new DatabseConstraints();
//    private Integer databseConstraintsMaxDay;
//
////    @PostConstruct
//    public void init() throws Exception {
//	DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
//	LocalDateTime now = LocalDateTime.now();
//	currentDate = dateFormatter.format(now);
//	dataPreparationService.seInitialRelayStatus();
//	this.currentSesnsorMinMaxInfo = dataPreparationService.getCurrentSesnsorMinMaxInfo(currentDate);
////	if(environmentStatusCrudRepository.count() > 0){
////	    totalRowSensorStatusTable = environmentStatusCrudRepository.count();
////	}
////	else {
////	    totalRowSensorStatusTable = (long) 1;
////	}
////	
////	totalRowSensorMinMaxStatusTable = sesnsorMinMaxStatusCrudRepository.count();
//	databseConstraintsMaxDay = databseConstraints.getMaxDay();
//
//	Calendar cal = new GregorianCalendar();
//	//DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
//	cal.setTime(Date.from(Instant.now()));
//
//	cal.add(Calendar.DAY_OF_MONTH, -30);
//	Date thirtyDayAgo = cal.getTime();
//
//	//dtf.format((TemporalAccessor) thirtyDayAgo);
//    }
//
////    @Scheduled(fixedRate = 10000)
////    @SendTo("/topic/sensorinfo")
//    public void fetchSensorData() {
//
//	try {
//	    
//	    
//	    
//	    //	log.i-nfo("The time is now {}", dateFormat.format(new Date()));
//	    String sensorInfo = SensorHttpHandler.fetchDataFromControllerOnHttpGet("sensorinfo");
////
//	    final String validityString = "{\"time\":";
//
//	    if (sensorInfo.startsWith(validityString)) {
//
//		//Integer noiseLevelInsertIndex = sensorInfo.indexOf(temperatureString)-1;
//		String previousSensorInfo = sensorInfo;
//		sensorInfo = dataPreparationService.prepareSensorInfo(sensorInfo);
//
//		EnvironmentInfo prepareEnvironmentInfo = new EnvironmentInfo();
//		prepareEnvironmentInfo = dataPreparationService.prepareEnvironmentInfo(sensorInfo);
//
//		// compare with fetched minmax and prepare updated minmax
//		SesnsorMinMaxInfo updatedSesnsorMinMaxInfo = new SesnsorMinMaxInfo();
//		updatedSesnsorMinMaxInfo = dataPreparationService.prepareSesnsorMinMaxInfo(prepareEnvironmentInfo, currentSesnsorMinMaxInfo);
//		if (updatedSesnsorMinMaxInfo != currentSesnsorMinMaxInfo && updatedSesnsorMinMaxInfo.getTime() == currentSesnsorMinMaxInfo.getTime()) {
//
//		    Gson gson = new Gson();
//		    String updatedSesnsorMinMaxInfoString;
//		    updatedSesnsorMinMaxInfoString = gson.toJson(updatedSesnsorMinMaxInfo);
//
//		    SensorMinMaxStatus sesnsorMinMaxStatusForSave = new SensorMinMaxStatus();
//		    sesnsorMinMaxStatusForSave.setTime(updatedSesnsorMinMaxInfo.getTime());
//		    sesnsorMinMaxStatusForSave.setSensorMinMaxStatus(updatedSesnsorMinMaxInfoString);
//
//		    SensorMinMaxStatus savedSesnsorMinMaxStatus = new SensorMinMaxStatus();
//		    savedSesnsorMinMaxStatus = sesnsorMinMaxStatusCrudRepository.save(sesnsorMinMaxStatusForSave);
//
//		    currentSesnsorMinMaxInfo.setHumidityMin(updatedSesnsorMinMaxInfo.getHumidityMin());
//		    currentSesnsorMinMaxInfo.setHumidityMax(updatedSesnsorMinMaxInfo.getHumidityMax());
//		    currentSesnsorMinMaxInfo.setTemperatureMin(updatedSesnsorMinMaxInfo.getTemperatureMin());
//		    currentSesnsorMinMaxInfo.setTemperatureMax(updatedSesnsorMinMaxInfo.getTemperatureMax());
//		    currentSesnsorMinMaxInfo.setLightMin(updatedSesnsorMinMaxInfo.getLightMin());
//		    currentSesnsorMinMaxInfo.setLightMax(updatedSesnsorMinMaxInfo.getLightMax());
//
//		} else if (updatedSesnsorMinMaxInfo.getTime() != currentSesnsorMinMaxInfo.getTime()) {
//		    Gson gson = new Gson();
//		    String updatedSesnsorMinMaxInfoString;
//		    updatedSesnsorMinMaxInfoString = gson.toJson(updatedSesnsorMinMaxInfo);
//
//		    SensorMinMaxStatus sesnsorMinMaxStatusForSave = new SensorMinMaxStatus();
//		    sesnsorMinMaxStatusForSave.setTime(updatedSesnsorMinMaxInfo.getTime());
//		    sesnsorMinMaxStatusForSave.setSensorMinMaxStatus(updatedSesnsorMinMaxInfoString);
//
//		    SensorMinMaxStatus savedSesnsorMinMaxStatus = new SensorMinMaxStatus();
//		    savedSesnsorMinMaxStatus = sesnsorMinMaxStatusCrudRepository.save(sesnsorMinMaxStatusForSave);
//
//		    currentSesnsorMinMaxInfo.setHumidityMin(updatedSesnsorMinMaxInfo.getHumidityMin());
//		    currentSesnsorMinMaxInfo.setHumidityMax(updatedSesnsorMinMaxInfo.getHumidityMax());
//		    currentSesnsorMinMaxInfo.setTemperatureMin(updatedSesnsorMinMaxInfo.getTemperatureMin());
//		    currentSesnsorMinMaxInfo.setTemperatureMax(updatedSesnsorMinMaxInfo.getTemperatureMax());
//		    currentSesnsorMinMaxInfo.setLightMin(updatedSesnsorMinMaxInfo.getLightMin());
//		    currentSesnsorMinMaxInfo.setLightMax(updatedSesnsorMinMaxInfo.getLightMax());
//		    currentSesnsorMinMaxInfo.setTime(updatedSesnsorMinMaxInfo.getTime());
//		}
//
//		EnvironmentInfoPublish environmentInfoPublish = new EnvironmentInfoPublish();
//		environmentInfoPublish = dataPreparationService.prepareEnvironmentInfoPublish(prepareEnvironmentInfo, updatedSesnsorMinMaxInfo);
//
//		String environmentStatusToPublish = dataPreparationService.environmentInfoPublishToJson(environmentInfoPublish);
//
//		EnvironmentStatus persistableEnvironmentStatus = new EnvironmentStatus();
//		persistableEnvironmentStatus.setTime(prepareEnvironmentInfo.getTime());
//		persistableEnvironmentStatus.setSensorInfo(sensorInfo);
////		persistableEnvironmentStatus.setSensorInfo("{\"time\": \"20201018125833\", \"sensorInfo\": {\"light\": 1.6666666666666667, \"noiseMin\": \"1\", \"noiseMax\": \"1\", \"temperature\": \"28.90\", \"humidity\": \"71.20\", \"motion\": \"0\", \"smoke\": \"0\", \"water\": 0.0, \"electricity\": \"1\"}}");
//		EnvironmentStatus savedData = new EnvironmentStatus();
//
//		//	if(totalRowSensorStatusTable == (databseConstraintsMaxDay * 24 * 60 * 6)){
//		//	   
//		//	    environmentStatusJpaRepository.deleteFirst8640();
//		//	    savedData = environmentStatusCrudRepository.save(persistableEnvironmentStatus);
//		//	}
//		//	else{
//		//	    savedData = environmentStatusCrudRepository.save(persistableEnvironmentStatus);
//		//	}
//		savedData = environmentStatusCrudRepository.save(persistableEnvironmentStatus);
//
//    //	    List<EnvironmentStatus> fetchedData = new ArrayList<>();
//    //	    //fetchedData = environmentStatusCrudRepository.findById(preparedData.getTime()).get();
//    //	    fetchedData = (List<EnvironmentStatus>) environmentStatusCrudRepository.findAll();
//		long numberOfRow = environmentStatusCrudRepository.count();
//		RelayInfo receivedRelayInfo = new RelayInfo();
//		receivedRelayInfo = prepareEnvironmentInfo.getRelayInfo();
//
//		LastRelayStatus storedRelayStatus = relayStatusCrudRepository.findById("relayStatusId").get();
//		RelayInfo storedRelayInfo = new RelayInfo();
//		storedRelayInfo = dataPreparationService.prepareRelayInfo(storedRelayStatus.getLastRelayStatus());
//
//		if (receivedRelayInfo.getReplay01() != storedRelayInfo.getReplay01()) {
//		    if (storedRelayInfo.getReplay01() == "1") {
//			SensorHttpHandler.fetchDataFromControllerOnHttpGet("setlampup");
//		    } else if (storedRelayInfo.getReplay01() == "0") {
//			SensorHttpHandler.fetchDataFromControllerOnHttpGet("setlampdown");
//		    }
//
//		}
//		if (receivedRelayInfo.getReplay02() != storedRelayInfo.getReplay02()) {
//		    if (storedRelayInfo.getReplay02() == "1") {
//			SensorHttpHandler.fetchDataFromControllerOnHttpGet("setdehumidifierup");
//		    } else if (storedRelayInfo.getReplay02() == "0") {
//			SensorHttpHandler.fetchDataFromControllerOnHttpGet("setdehumidifierdown");
//		    }
//		}
//
//		log.info("Response data: " + numberOfRow + "  " + environmentStatusToPublish);
//		msgTemplate.convertAndSend("/topic/sensorinfo", environmentStatusToPublish);
//	    }
//
//	} catch (Exception ex) {
//	    java.util.logging.Logger.getLogger(SensorDataFetchScheduler.class.getName()).log(Level.SEVERE, null, ex);
//	}
//
//    }
//}
