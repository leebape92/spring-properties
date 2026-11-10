package com.example.demo.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;

@Component
public class AesCryptoUtils {
	
	// AES: 데이터를 암호화하는 핵심 알고리즘
	// CBC: 앞 블록과 뒤 블록을 엮어서 매번 무작위로 다른 암호문이 나오도록 만드는 안전한 운용 방식 IvParameterSpec 초깃값
	// PKCS5Padding: 데이터의 남는 빈 공간을 안전하게 채워주는 규칙
	private static final String ALGORITHM = "AES/CBC/PKCS5Padding";

	// 암호를 잠그고 풀 때 사용하는 key 32바이트 입력필요
    private final SecretKeySpec keySpec = new SecretKeySpec("12345678901234567890123456789012".getBytes(), "AES");
    
    // 암호화 결과값 중복 방지
    private final IvParameterSpec ivSpec = new IvParameterSpec("1234567890123456".getBytes());

    public String encrypt(String plainText) {
        if (plainText == null || plainText.isBlank()) return plainText;
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
            // 평문 문자열을 암호화된 바이트 데이터로 변환
            byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("Encryption failed", e);
        }
    }

    public String decrypt(String cipherText) {
        if (cipherText == null || cipherText.isBlank()) return cipherText;
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            byte[] decodedBytes = Base64.getDecoder().decode(cipherText);
            byte[] decrypted = cipher.doFinal(decodedBytes);
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Decryption failed", e);
        }
    }
	
}
