package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.example.demo.config.DatabaseEnvironmentInitializer;

@SpringBootApplication
public class SpringPropertiesApplication {

//	public static void main(String[] args) {
//		SpringApplication.run(SpringPropertiesApplication.class, args);
//	}

	
	
	public static void main(String[] args) {
        SpringApplication app = new SpringApplication(SpringPropertiesApplication.class);
        
        // 💡 spring.factories 없이 자바 코드로 이벤트 리스너(초기화 로직) 직접 등록
        app.addListeners(new DatabaseEnvironmentInitializer());
        
        app.run(args);
    }
	
}