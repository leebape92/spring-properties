package com.example.demo.crypto.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.demo.crypto.dto.CryptoRequestDto;
import com.example.demo.util.AesCryptoUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CryptoService {

    private final AesCryptoUtils aesCryptoUtils; // 공통 암복호화 유틸

    // 암호화 통합 진입점
    public String encrypt(@RequestBody CryptoRequestDto cryptoRequestDto) {
        if (cryptoRequestDto.getCryptoText() == null || cryptoRequestDto.getCryptoText().isBlank()) {
            throw new IllegalArgumentException("암호화할 데이터가 없습니다.");
        }
        
        String plainText = null;
        
        String cryptoTypeCode = cryptoRequestDto.getCryptoTypeCode();
        String cryptoText = cryptoRequestDto.getCryptoText();
        
        validateByTypeCode(cryptoTypeCode,cryptoText);
        
        if("01".equals(cryptoTypeCode)){
        	plainText = "RRN_ENCRYPT_" + cryptoRequestDto.getCryptoText();
        } else if("02".equals(cryptoRequestDto.getCryptoTypeCode())){
        	plainText = "ACCOUNT_ENCRYPT_" + cryptoRequestDto.getCryptoText();
        } else if("03".equals(cryptoRequestDto.getCryptoTypeCode())){
        	plainText = "PHONE_ENCRYPT_" + cryptoRequestDto.getCryptoText();
        }
        
        return aesCryptoUtils.encrypt(plainText);
    }
    
    // IllegalArgumentException
    // 자바(Java) 등 프로그래밍 언어에서 메서드에 전달된 인수(Argument, 파라미터)가 부적절하거나 유효하지 않은 경우에 발생하는 런타임 예외
    
    // 복호화 통합 진입점
    public String decrypt(@RequestBody CryptoRequestDto cryptoRequestDto) {
    	if (cryptoRequestDto.getCryptoText() == null || cryptoRequestDto.getCryptoText().isBlank()) {
            throw new IllegalArgumentException("복호화할 데이터가 없습니다.");
        }
    	
    	
    	String decryptedText = aesCryptoUtils.decrypt(cryptoRequestDto.getCryptoText());
    	System.out.println("decryptedText:::" + decryptedText);
    	
    	String cipherText = "";
        String cryptoTypeCode = cryptoRequestDto.getCryptoTypeCode();

        // 2. 타입 코드별 접두사 검증 및 제거 (틀리면 예외 발생)
        if ("01".equals(cryptoTypeCode)) {
            if (!decryptedText.startsWith("RRN_ENCRYPT_")) {
                throw new IllegalArgumentException("주민번호(01) 타입에 맞지 않는 암호문이거나 키가 일치하지 않습니다.");
            }
            cipherText = decryptedText.replace("RRN_ENCRYPT_", "");
            
        } else if ("02".equals(cryptoTypeCode)) {
            if (!decryptedText.startsWith("ACCOUNT_ENCRYPT_")) {
                throw new IllegalArgumentException("계좌번호(02) 타입에 맞지 않는 암호문이거나 키가 일치하지 않습니다.");
            }
            cipherText = decryptedText.replace("ACCOUNT_ENCRYPT_", "");
            
        } else if ("03".equals(cryptoTypeCode)) {
            if (!decryptedText.startsWith("PHONE_ENCRYPT_")) {
                throw new IllegalArgumentException("핸드폰번호(03) 타입에 맞지 않는 암호문이거나 키가 일치하지 않습니다.");
            }
            cipherText = decryptedText.replace("PHONE_ENCRYPT_", "");
            
        } else {
            throw new IllegalArgumentException("지원하지 않는 코드값입니다: " + cryptoTypeCode);
        }

        return cipherText;
    }

    private void validateByTypeCode(String cryptoTypeCode, String cryptoText) {
        if ("01".equals(cryptoTypeCode)) {
            if (cryptoText.length() != 13) {
                throw new IllegalArgumentException("주민번호는 13자리여야 합니다.");
            }
        } else if ("02".equals(cryptoTypeCode)) {
            if (cryptoText.length() < 10 || cryptoText.length() > 14) {
                throw new IllegalArgumentException("계좌번호 길이를 확인해주세요.");
            }
        } else if ("03".equals(cryptoTypeCode)) {
            if (cryptoText.length() < 10 || cryptoText.length() > 11) {
                throw new IllegalArgumentException("핸드폰번호는 10~11자리여야 합니다.");
            }
        } else {
            throw new IllegalArgumentException("지원하지 않는 코드값입니다: " + cryptoTypeCode);
        }
    }
}