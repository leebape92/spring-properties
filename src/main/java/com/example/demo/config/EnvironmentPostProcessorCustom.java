package com.example.demo.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;

import com.example.demo.util.JasyptUtils;


public class EnvironmentPostProcessorCustom implements EnvironmentPostProcessor {

    private static final String PROPERTY_SOURCE_NAME = "jasyptDecryptedProperties";

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, org.springframework.boot.SpringApplication application) {
        // 1. 외부 JVM 옵션 또는 환경 변수에서 마스터 키 가져오기 (예: -Djasypt.encryptor.password=키값)
        String masterKey = environment.getProperty("jasypt.encryptor.password");
        System.out.println("masterKey ::: " + masterKey);
        
        if (masterKey == null || masterKey.isEmpty()) {
            // 키가 없으면 복호화를 수행하지 않고 패스 (또는 예외 처리)
            return;
        }

        MutablePropertySources propertySources = environment.getPropertySources();
        Map<String, Object> decryptedProperties = new HashMap<>();

        // 2. 기존 설정 파일들의 프로퍼티를 순회하며 ENC(...) 패턴 탐지 및 복호화
        for (PropertySource<?> propertySource : propertySources) {
            for (String propertyName : getPropertyNames(propertySource)) {
                Object value = propertySource.getProperty(propertyName);
                if (value instanceof String) {
                    String stringValue = (String) value;
                    if (stringValue.startsWith("ENC(") && stringValue.endsWith(")")) {
                        String encryptedValue = stringValue.substring(4, stringValue.length() - 1);
                        System.out.println("encryptedValue ::: " + encryptedValue);
                        try {
                            // 복호화 수행
                            String decryptedValue = JasyptUtils.decrypt(encryptedValue, masterKey);
                            decryptedProperties.put(propertyName, decryptedValue);
                        } catch (Exception e) {
                            throw new RuntimeException("Jasypt Decryption Failed for property: " + propertyName, e);
                        }
                    }
                }
            }
        }
        
        System.out.println("decryptedProperties ::: " + decryptedProperties);

        // 3. 복호화된 값을 담은 새로운 PropertySource를 가장 높은 우선순위로 주입
        // 	1) 설정파일에서 암호화 값을 평문으로 만들어서 map decryptedProperties 담는다
        //	2) decryptedProperties 만든 값을 PROPERTY_SOURCE_NAME name으로 사용한다
        //  3) name이 PROPERTY_SOURCE_NAME을 가장 앞으로 올려줘 
        //		- 스프링이 설정값을 찾을때 맨앞부터 찾기 시작, 맨앞에 PROPERTY_SOURCE_NAME가 있으니 그 안에 담겨있는 평문 값을 사용
        if (!decryptedProperties.isEmpty()) {
            propertySources.addFirst(new MapPropertySource(PROPERTY_SOURCE_NAME, decryptedProperties));
        }
        
        System.out.println("PROPERTY_SOURCE_NAME :::" + PROPERTY_SOURCE_NAME);
    }

//    private Iterable<String> getPropertyNames(PropertySource<?> propertySource) {
//        if (propertySource instanceof org.springframework.core.env.EnumerablePropertySource) {
//            return java.util.Arrays.asList(((org.springframework.core.env.EnumerablePropertySource<?>) propertySource).getPropertyNames());
//        }
//        return java.util.Collections.emptyList();
//    }
    
    
    // 스프링 부트가 구동될 때 읽어 들인 설정 파일들의 소스(PropertySource)에서 해당 소스 내부에 들어 있는 모든 프로퍼티 키(이름)들의 목록을 뽑아내는 역할을 합니다.
    private Iterable<String> getPropertyNames(PropertySource<?> propertySource) {
        if (propertySource instanceof EnumerablePropertySource) {
            String[] propertyNames = ((EnumerablePropertySource<?>) propertySource).getPropertyNames();
            return Arrays.asList(propertyNames);
        }
        
        return Collections.emptyList();
    }
}