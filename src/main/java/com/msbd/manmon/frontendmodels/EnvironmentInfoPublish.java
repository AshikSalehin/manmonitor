
package com.msbd.manmon.frontendmodels;

public class EnvironmentInfoPublish {
    private String time;
    private SensorInfo sensorInfo;
    private RelayInfo relayInfo;
    private SesnsorMinMaxInfo sesnsorMinMaxInfo;

    public EnvironmentInfoPublish() {
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public SensorInfo getSensorInfo() {
        return sensorInfo;
    }

    public void setSensorInfo(SensorInfo sensorInfo) {
        this.sensorInfo = sensorInfo;
    }

    public RelayInfo getRelayInfo() {
        return relayInfo;
    }

    public void setRelayInfo(RelayInfo relayInfo) {
        this.relayInfo = relayInfo;
    }

    public SesnsorMinMaxInfo getSesnsorMinMaxInfo() {
	return sesnsorMinMaxInfo;
    }

    public void setSesnsorMinMaxInfo(SesnsorMinMaxInfo sesnsorMinMaxInfo) {
	this.sesnsorMinMaxInfo = sesnsorMinMaxInfo;
    }
    
    
}
