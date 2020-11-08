//
//package com.msbd.manmon.config;
//
//import java.util.Properties;
//import javax.activation.DataSource;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.orm.hibernate5.HibernateTransactionManager;
//import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
//import org.springframework.transaction.PlatformTransactionManager;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
//@Configuration
//@EnableTransactionManagement
//public class HibernateConfig {
//    @Bean
//    public LocalSessionFactoryBean sessionFactory() {
//	LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
//	sessionFactory.setDataSource((javax.sql.DataSource) dataSource());
//	sessionFactory.setPackagesToScan("com.msbd.manmon.domainmodel");
//	sessionFactory.setHibernateProperties(hibernateProperties());
//
//	return sessionFactory;
//    }
//
//    @Bean
//    public DataSource dataSource() {
//	BasicDataSource dataSource = new BasicDataSource();
//	dataSource.setDriverClassName("org.apache.derby.jdbc.EmbeddedDriver");
//	dataSource.setUrl("jdbc:derby:/home/shazid/Documents/Derby_Embedded_DB_Test/ManMonDB;create=true");
//	dataSource.setUsername("mangrove");
//	dataSource.setPassword("mangrove1234");
//
//	return (DataSource) dataSource;
//    }
//
//    @Bean
//    public PlatformTransactionManager hibernateTransactionManager() {
//	HibernateTransactionManager transactionManager
//	  = new HibernateTransactionManager();
//	transactionManager.setSessionFactory(sessionFactory().getObject());
//	return transactionManager;
//    }
//
//    private final Properties hibernateProperties() {
//	Properties hibernateProperties = new Properties();
//	hibernateProperties.setProperty("hibernate.hbm2ddl.auto", "create-drop");
//	hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.DerbyTenSevenDialect");
//
//	return hibernateProperties;
//    }
//}
