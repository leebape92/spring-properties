package com.example.demo.config;

import java.util.Map;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import com.example.demo.infra.DbPropertyFactory;
import com.example.demo.util.JasyptUtil;

public class EnvironmentPostProcessorCustom implements EnvironmentPostProcessor {
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        String password = environment.getProperty("jasypt.encryptor.password");
        if (password == null) return;

        String profile = environment.getProperty("spring.profiles.active", "local");
        
        // 분리된 클래스들 호출
        StandardPBEStringEncryptor jasypt = JasyptUtil.getEncryptor(password);
        Map<String, Object> dbProperties = DbPropertyFactory.getDbProperties(profile, jasypt);

        environment.getPropertySources().addFirst(new MapPropertySource("customDbConfig", dbProperties));
    }
}