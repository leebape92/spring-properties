package com.example.demo.util;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;


// EnvironmentPostProcessor는 스프링 컨테이너가 완전히 뜨고 빈(Bean)들이 생성되기보다 훨씬 이른 단계에 동작합니다.
// 이 시점에는 스프링이 아직 JasyptUtils를 객체로 관리하지 않기 때문에, @Component로 등록하고 주입(@Autowired)받으려고 하면 빈을 찾지 못해 null이 되고 에러가 발생합니다.

public class JasyptUtils {
    
	public static String decrypt(String encryptedText, String password) {
    	System.out.println("encryptedText:::" + encryptedText);
        StandardPBEStringEncryptor jasypt = new StandardPBEStringEncryptor();
        jasypt.setPassword(password); //마스터키 셋팅
        jasypt.setAlgorithm("PBEWithMD5AndDES");
        return jasypt.decrypt(encryptedText);
    }
    
}