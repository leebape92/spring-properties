package com.example.demo.config;

import java.util.HashMap;
import java.util.Map;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

public class EnvironmentPostProcessorCustom implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        
    	// 1. 서버 실행 시 옵션으로 넘긴 복호화 키를 읽어옴
        // 실행 예: -Djasypt.encryptor.password=my-secret-key
        String password = environment.getProperty("jasypt.encryptor.password");
        System.out.println("password:::" + password);

        if (password == null) {
            System.out.println("⚠️ Jasypt master key is missing!");
            return;
        }

        // 2. Jasypt 복호화기 설정
        StandardPBEStringEncryptor jasypt = new StandardPBEStringEncryptor();
        jasypt.setPassword(password);
        jasypt.setAlgorithm("PBEWithMD5AndDES"); // 기본 알고리즘
        
     // 3. 실제 DB 비밀번호 (암호화하고 싶은 값)
        String targetPassword = "real_db_password_1234"; 
        
        String encrypted = jasypt.encrypt(targetPassword);
        

        Map<String, Object> dbProperties = new HashMap<>();
        String profile = environment.getProperty("spring.profiles.active", "local");
        
        
        // 비밀번호 초기화
        String encryptedPw = "";
        
        try {
            if ("prod".equals(profile)) {
            	encryptedPw = "prodPw";
                dbProperties.put("spring.datasource.username", "prod_admin");
                dbProperties.put("spring.datasource.password", jasypt.decrypt(encryptedPw));
                
            } else {
            	encryptedPw = "localDevPw";
            	dbProperties.put("spring.datasource.username", "root");
                dbProperties.put("spring.datasource.password", jasypt.decrypt(encryptedPw));
                
            }
        } catch (Exception e) {
            throw new RuntimeException("복호화에 실패했습니다. 마스터 키를 확인하세요.");
        }

        // 4. Spring Environment의 최상단(우선순위 1등)에 주입
        // 이 이름(dynamicDbConfig)은 관리용 식별자입니다.
        environment.getPropertySources().addFirst(new MapPropertySource("customDbConfig", dbProperties));
    }
}