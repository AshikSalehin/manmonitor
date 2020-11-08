
package com.msbd.manmon.domainmodel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="sensor_min_max_status")
public class SensorMinMaxStatus {
    @Id
    @Column(name="date")
    private String time;
    
    @Column(name="sensor_min_max_status", length = 500)
    private String sensorMinMaxStatus;
    

    public SensorMinMaxStatus() {
    }

    public String getTime() {
	return time;
    }

    public void setTime(String time) {
	String date = time.substring(0, 7);
	this.time = date;
    }

    public String getSensorMinMaxStatus() {
	return sensorMinMaxStatus;
    }

    public void setSensorMinMaxStatus(String sensorMinMaxStatus) {
	this.sensorMinMaxStatus = sensorMinMaxStatus;
    }
    
    
}
