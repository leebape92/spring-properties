package com.example.demo.infra;

import java.util.HashMap;
import java.util.Map;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

public class DbPropertyFactory {
    public static Map<String, Object> getDbProperties(String profile, StandardPBEStringEncryptor jasypt) {
        Map<String, Object> props = new HashMap<>();
        
        if ("prod".equals(profile)) {
        	props.put("spring.datasource.url", "jdbc:mysql://localhost:3306/test");
            props.put("spring.datasource.username", "prod_admin");
            props.put("spring.datasource.password", jasypt.decrypt("KTuJmTidTkkUA/hZaZxd+Q=="));
        } else {
        	props.put("spring.datasource.url", "jdbc:mysql://localhost:3306/test");
        	props.put("spring.datasource.driver-class-name", "com.mysql.cj.jdbc.Driver");
            props.put("spring.datasource.username", "junho");
            props.put("spring.datasource.password", jasypt.decrypt("aewjYBAbZyJ9Jsu6fy0g4Q=="));
        }
        return props;
    }
}