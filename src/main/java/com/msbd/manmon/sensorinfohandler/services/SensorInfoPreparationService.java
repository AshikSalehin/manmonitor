
package com.msbd.manmon.sensorinfohandler.services;

import com.msbd.manmon.domainmodel.LastRelayStatus;
import com.msbd.manmon.frontendmodels.EnvironmentInfo;
import com.msbd.manmon.frontendmodels.EnvironmentInfoPublish;
import com.msbd.manmon.frontendmodels.RelayInfo;
import com.msbd.manmon.frontendmodels.SesnsorMinMaxInfo;

public interface SensorInfoPreparationService {
    String getCurrentDate();
    RelayInfo getLastRelayInfo();
    void setInitialRelayStatus();
    SesnsorMinMaxInfo getCurrentSesnsorMinMaxInfo(String currentDate);
    String prepareSensorInfo(String sensorInfoMain);
    EnvironmentInfo prepareEnvironmentInfo(String responseData);
    SesnsorMinMaxInfo prepareSesnsorMinMaxInfo(EnvironmentInfo environmentInfo, SesnsorMinMaxInfo sesnsorMinMaxInfo, boolean state);
    EnvironmentInfoPublish prepareEnvironmentInfoPublish(EnvironmentInfo environmentInfo, SesnsorMinMaxInfo sesnsorMinMaxInfo);
    String environmentInfoPublishToString(EnvironmentInfoPublish environmentInfoPublish);
    RelayInfo isRelayStatusAlright(RelayInfo currentRelayInfo);
    
    
    
}
