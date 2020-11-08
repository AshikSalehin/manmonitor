//
//package com.msbd.manmon.config;
//
//import java.sql.DriverManager;
//import java.sql.SQLException;
//import javax.annotation.PreDestroy;
//import org.springframework.context.annotation.Configuration;
//
////@Configuration
//public class DerbyShutdownConfig {
////    @PreDestroy
//    public void shutdown() {
//        final String SHUTDOWN_CODE = "XJ015";
//        System.out.println("SHUTTING DOWN");
//
//        try {
//            DriverManager.getConnection("jdbc:derby:;shutdown=true");
//        } catch (SQLException e) {
//            // Derby 10.9.1.0 shutdown raises a SQLException with code "XJ015"
//            if (!SHUTDOWN_CODE.equals(e.getSQLState())) {
//                e.printStackTrace();
//            }
//        }
//    }
//}
