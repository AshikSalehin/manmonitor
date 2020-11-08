
package com.msbd.manmon.domainmodel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="sensor_threshold_info")
public class SensorThreshold {
    
    @Id
    @Column(name = "sensor_threshold_id")
    private String sensorThresholdId = "sensorThresholdId";
    
    @Column(name = "sensor_threshold_value")
    private String sensorThresholdValue;


    public SensorThreshold() {
    }

    public String getSensorThresholdId() {
	return sensorThresholdId;
    }

//    public void setSensorThresholdId(String sensorThresholdId) {
//	this.sensorThresholdId = sensorThresholdId;
//    }

    public String getSensorThresholdValue() {
	return sensorThresholdValue;
    }

    public void setSensorThresholdValue(String sensorThresholdValue) {
	this.sensorThresholdValue = sensorThresholdValue;
    }

    
}
