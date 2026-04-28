package com.example.demo.infra;

import java.util.HashMap;
import java.util.Map;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

public class DbPropertyFactory {
    public static Map<String, Object> getDbProperties(String profile, StandardPBEStringEncryptor jasypt) {
        Map<String, Object> props = new HashMap<>();
        
        if ("prod".equals(profile)) {
            props.put("spring.datasource.username", "prod_admin");
            props.put("spring.datasource.password", jasypt.decrypt("KTuJmTidTkkUA/hZaZxd+Q=="));
        } else {
            props.put("spring.datasource.username", "root");
            props.put("spring.datasource.password", jasypt.decrypt("KTuJmTidTkkUA/hZaZxd+Q=="));
        }
        return props;
    }
}