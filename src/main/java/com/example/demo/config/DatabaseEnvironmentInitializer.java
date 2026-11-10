package com.example.demo.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.example.demo.util.JasyptUtils;

public class DatabaseEnvironmentInitializer implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

	
    private static final String PROPERTY_SOURCE_NAME = "jasyptDecryptedProperties";

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        ConfigurableEnvironment environment = event.getEnvironment();

        // 1. 마스터 키 확인
        String masterKey = environment.getProperty("jasypt.encryptor.password");
        if (masterKey == null || masterKey.isEmpty()) {
            return;
        }

        MutablePropertySources propertySources = environment.getPropertySources();

        // 2. 현재 프로필에 맞는 DB 설정 파일 경로 및 이름 동적 결정
        String activeProfile = getActiveProfile(environment);
        String dbConfigPath = String.format("com/example/demo/database/%s-db.yml", activeProfile);
        String propertySourceName = activeProfile + "DbProperties";

        // 3. 자바 패키지 경로의 DB 설정 파일 로드
        loadDatabasePropertySource(propertySources, dbConfigPath, propertySourceName);

        // 4. ENC(...) 패턴 탐지 및 복호화
        Map<String, Object> decryptedProperties = new HashMap<>();
        for (PropertySource<?> propertySource : propertySources) {
            for (String propertyName : getPropertyNames(propertySource)) {
                Object value = propertySource.getProperty(propertyName);
                if (value instanceof String) {
                    String stringValue = (String) value;
                    if (stringValue.startsWith("ENC(") && stringValue.endsWith(")")) {
                        String encryptedValue = stringValue.substring(4, stringValue.length() - 1);
                        try {
                            String decryptedValue = JasyptUtils.decrypt(encryptedValue, masterKey);
                            decryptedProperties.put(propertyName, decryptedValue);
                        } catch (Exception e) {
                            throw new RuntimeException("Jasypt Decryption Failed for property: " + propertyName, e);
                        }
                    }
                }
            }
        }

        // 5. 복호화된 값을 최상단 우선순위로 주입
        if (!decryptedProperties.isEmpty()) {
            propertySources.addFirst(new MapPropertySource(PROPERTY_SOURCE_NAME, decryptedProperties));
        }
    }

    private String getActiveProfile(ConfigurableEnvironment environment) {
        String[] profiles = environment.getActiveProfiles();
        if (profiles.length > 0 && profiles[0] != null && !profiles[0].isEmpty()) {
            return profiles[0];
        }
        return "local";
    }

    private void loadDatabasePropertySource(MutablePropertySources propertySources, String dbConfigPath, String propertySourceName) {
        try {
            Resource resource = new ClassPathResource(dbConfigPath);
            if (resource.exists()) {
                YamlPropertySourceLoader loader = new YamlPropertySourceLoader();
                List<PropertySource<?>> loadedPropertySources = loader.load(propertySourceName, resource);
                for (PropertySource<?> propertySource : loadedPropertySources) {
                    propertySources.addLast(propertySource);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load DB config from path: " + dbConfigPath, e);
        }
    }

    private Iterable<String> getPropertyNames(PropertySource<?> propertySource) {
        if (propertySource instanceof EnumerablePropertySource) {
            String[] propertyNames = ((EnumerablePropertySource<?>) propertySource).getPropertyNames();
            return Arrays.asList(propertyNames);
        }
        return Collections.emptyList();
    }
}