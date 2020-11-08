//
//package com.msbd.manmon.domainmodel.previous;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.Table;
//
//@Entity
//@Table(name="sensor_threshold_info")
//public class SensorThresholdPrevious {
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    @Column
//    private String sensorName;
//    
//    @Column
//    private double lowValue;
//    
//    @Column
//    private double highValue;
//
//    public SensorThresholdPrevious() {
//    }
//
//    public String getSensorName() {
//        return sensorName;
//    }
//
//    public void setSensorName(String sensorName) {
//        this.sensorName = sensorName;
//    }
//
//    public double getLowValue() {
//        return lowValue;
//    }
//
//    public void setLowValue(double lowValue) {
//        this.lowValue = lowValue;
//    }
//
//    public double getHighValue() {
//        return highValue;
//    }
//
//    public void setHighValue(double highValue) {
//        this.highValue = highValue;
//    }
//}
