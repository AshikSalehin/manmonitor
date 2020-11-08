
package com.msbd.manmon.domainmodel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="environment_status")
public class EnvironmentStatus {

    @Id
    @Column(name="time")
    private String time;
    
    @Column(name="sensorinfo", length = 1000)
    private String sensorInfo;
    
    
    public EnvironmentStatus(){
        
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSensorInfo() {
	return sensorInfo;
    }

    public void setSensorInfo(String sensorInfo) {
	this.sensorInfo = sensorInfo;
    }

    

}
