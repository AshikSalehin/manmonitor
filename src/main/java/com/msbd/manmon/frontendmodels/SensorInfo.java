package com.msbd.manmon.frontendmodels;

public class SensorInfo {

    private double temperature;
    private double humidity;
    private String noiseMax;
    private String noiseMin;
    private double noiseLevel[];
    private double light;
    private String motion;
    private String smoke;
    private double water;
    private String electricity;
    
    public SensorInfo() {
    }

    public double getTemperature() {
	return temperature;
    }

    public void setTemperature(double temperature) {
	this.temperature = temperature;
    }

    public double getHumidity() {
	return humidity;
    }

    public void setHumidity(double humidity) {
	this.humidity = humidity;
    }

    public String getNoiseMax() {
	return noiseMax;
    }

    public void setNoiseMax(String noiseMax) {
	this.noiseMax = noiseMax;
    }

    public String getNoiseMin() {
	return noiseMin;
    }

    public void setNoiseMin(String noiseMin) {
	this.noiseMin = noiseMin;
    }

    public double getLight() {
	return light;
    }

    public void setLight(double light) {
	this.light = light;
    }

    public double[] getNoiseLevel() {
	return noiseLevel;
    }

    public void setNoiseLevel(double noiseLevel[]) {
	this.noiseLevel = noiseLevel;
    }

    public String getMotion() {
	return motion;
    }

    public void setMotion(String motion) {
	this.motion = motion;
    }

    public String getSmoke() {
	return smoke;
    }

    public void setSmoke(String smoke) {
	this.smoke = smoke;
    }

    public double getWaterLevel() {
	return water;
    }

    public void setWaterLevel(double waterLevel) {
	this.water = waterLevel;
    }

    public String getElectricity() {
	return electricity;
    }

    public void setElectricity(String electricity) {
	this.electricity = electricity;
    }
    
    

}
