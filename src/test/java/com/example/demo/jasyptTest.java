package com.example.demo;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

public class jasyptTest {
	public static void main(String[] args) {
		
		StandardPBEStringEncryptor jasypt = new StandardPBEStringEncryptor();
	    jasypt.setPassword("testKey"); // -Djasypt.encryptor.password 값
	    jasypt.setAlgorithm("PBEWithMD5AndDES");

	    String target = "12341!"; // 실제 운영 DB 비밀번호
	    
	    //	암호화
	    String encrypted = jasypt.encrypt(target); 
	    System.out.println("평문 -> 암호화 결과: " + encrypted); // H0ACLo2FmMwValO+Kt82HA==
	    
	    // 복호화
	    String decrypted = jasypt.decrypt("H0ACLo2FmMwValO+Kt82HA==");
	    System.out.println("암호화 -> 복호화 결과 ::: " + decrypted);
	    
	    
		
	}
}
