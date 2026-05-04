package com.example.demo;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

public class jasyptTest {
	public static void main(String[] args) {
		
		StandardPBEStringEncryptor jasypt = new StandardPBEStringEncryptor();
	    jasypt.setPassword("testKey"); // -Djasypt.encryptor.password 값
	    jasypt.setAlgorithm("PBEWithMD5AndDES");

	    String target = "12341!"; // 실제 운영 DB 비밀번호
	    String encrypted = jasypt.encrypt(target);
	    
	    System.out.println("암호화 결과: " + encrypted);
	    // 1JcZr/Pu+sIZcfKWjkXswn54JmnxdbMJ0Jsx8Y5CZhMkCqESLAaKIQ==
	    // 실행 결과 예: "6vXpW9L... (복잡한 문자열)"
		
	}
}
