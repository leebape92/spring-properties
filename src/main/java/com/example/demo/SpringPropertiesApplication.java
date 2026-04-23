package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class SpringPropertiesApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringPropertiesApplication.class, args);
	}

}


//@SpringBootApplication
//public class SpringPropertiesApplication {
//
//    public static void main(String[] args) {
//        System.out.println("*****************************************");
//        System.out.println("빌드 반영 확인! 보안 로직을 시작합니다.");
//        System.out.println("*****************************************");
//
//        SpringApplication app = new SpringApplication(SpringPropertiesApplication.class);
//        
//        // 직접 호출 대신 '초기화기'로 등록 (이게 가장 안전합니다)
//        app.addInitializers(new ApplicationContextInitializer<ConfigurableApplicationContext>() {
//            @Override
//            public void initialize(ConfigurableApplicationContext context) {
//                // 1. 운영 본부(context)에서 환경 설정 도구를 꺼냅니다.
//                org.springframework.core.env.ConfigurableEnvironment env = context.getEnvironment();
//                
//                // 2. 우리가 만든 커스텀 프로세서를 생성합니다.
//                EnvironmentPostProcessorCustom processor = new EnvironmentPostProcessorCustom();
//                
//                // 3. 설정을 실행합니다.
//                processor.postProcessEnvironment(env, app);
//            }
//        });
//
//        app.run(args);
//    }
//}