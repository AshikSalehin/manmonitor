
package com.msbd.manmon.sensorinfohandler.services;

import com.msbd.manmon.domainmodel.EnvironmentStatus;
import com.msbd.manmon.frontendmodels.EnvironmentInfo;
import com.msbd.manmon.frontendmodels.SesnsorMinMaxInfo;


public interface SensorInfoPersistService {
    void sesnsorMinMaxInfoSave(SesnsorMinMaxInfo updatedSesnsorMinMaxInfo);
    EnvironmentStatus saveSensorInfo(EnvironmentInfo prepareEnvironmentInfo);
}
