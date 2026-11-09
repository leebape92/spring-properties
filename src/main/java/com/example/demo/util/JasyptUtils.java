package com.example.demo.util;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JasyptUtils {
	
	// Jasypt 복호화 함수
    public String stringEncryptor(String encryptedText, String password) {
        StandardPBEStringEncryptor jasypt = new StandardPBEStringEncryptor();
        jasypt.setPassword(password);
        jasypt.setAlgorithm("PBEWithMD5AndDES");
        return jasypt.decrypt(encryptedText);
    }
}