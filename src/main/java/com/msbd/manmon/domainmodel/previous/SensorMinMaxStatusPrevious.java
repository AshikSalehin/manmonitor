//
//package com.msbd.manmon.domainmodel.previous;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.Id;
//import javax.persistence.Table;
//
//@Entity
//@Table(name="sensor_min_max_status")
//public class SensorMinMaxStatusPrevious {
//    @Id
//    @Column(name="date")
//    private String time;
//    
//    @Column(name="temperature_min")
//    private double temperatureMin;
//    
//    @Column(name="temperature_max")
//    private double temperatureMax;
//    
//    @Column(name="humidity_min")
//    private double humidityMin;
//    
//    @Column(name="humidity_max")
//    private double humidityMax;
//    
//    @Column(name="light_min")
//    private double lightMin;
//    
//    @Column(name="light_max")
//    private double lightMax;
//
//    public SensorMinMaxStatusPrevious() {
//    }
//
//    public String getTime() {
//	return time;
//    }
//
//    public void setTime(String time) {
//	String date = time.substring(0, 7);
//	this.time = date;
//    }
//
//    public double getTemperatureMin() {
//	return temperatureMin;
//    }
//
//    public void setTemperatureMin(double temperatureMin) {
//	this.temperatureMin = temperatureMin;
//    }
//
//    public double getTemperatureMax() {
//	return temperatureMax;
//    }
//
//    public void setTemperatureMax(double temperatureMax) {
//	this.temperatureMax = temperatureMax;
//    }
//
//    public double getHumidityMin() {
//	return humidityMin;
//    }
//
//    public void setHumidityMin(double humidityMin) {
//	this.humidityMin = humidityMin;
//    }
//
//    public double getHumidityMax() {
//	return humidityMax;
//    }
//
//    public void setHumidityMax(double humidityMax) {
//	this.humidityMax = humidityMax;
//    }
//
//    public double getLightMin() {
//	return lightMin;
//    }
//
//    public void setLightMin(double lightMin) {
//	this.lightMin = lightMin;
//    }
//
//    public double getLightMax() {
//	return lightMax;
//    }
//
//    public void setLightMax(double lightMax) {
//	this.lightMax = lightMax;
//    }    
//    
//}
