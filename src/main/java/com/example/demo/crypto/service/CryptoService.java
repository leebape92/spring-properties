package com.example.demo.crypto.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.demo.crypto.dto.CryptoRequestDto;
import com.example.demo.crypto.dto.CryptoResponseDto;
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

//         1. 코드값에 따른 사전 검증 (길이 체크 등)
//        validateByTypeCode(typeCode, plainText, true);
        String plainText = cryptoRequestDto.getCryptoTypeCode();
         
        // 2. 공통 암호화 수행
        return aesCryptoUtils.encrypt(plainText);
    }

    // 복호화 통합 진입점
    public String decrypt(@RequestBody CryptoRequestDto cryptoRequestDto) {
    	if (cryptoRequestDto.getCryptoText() == null || cryptoRequestDto.getCryptoText().isBlank()) {
            throw new IllegalArgumentException("복호화할 데이터가 없습니다.");
        }
    	
    	String cipherText = cryptoRequestDto.getCryptoTypeCode();
        // 복호화는 공통 수행 (필요시 코드별 추가 검증 가능)
        return aesCryptoUtils.decrypt(cipherText);
    }

    // 코드값 및 렝스(길이) 검증 메서드 (직관적인 if-else)
//    private void validateByTypeCode(String typeCode, String text, boolean isEncrypt) {
//        if (!isEncrypt) return; // 복호화 때는 평문 길이 검증 불필요
//
//        if ("01".equals(typeCode)) {
//            // 주민번호 예시 (예: 13자리)
//            if (text.length() != 13) {
//                throw new IllegalArgumentException("주민번호는 13자리여야 합니다.");
//            }
//        } else if ("02".equals(typeCode)) {
//            // 계좌번호 예시 (예: 10~14자리 등 가변 길이 체크)
//            if (text.length() < 10 || text.length() > 14) {
//                throw new IllegalArgumentException("계좌번호 길이를 확인해주세요.");
//            }
//        } else if ("03".equals(typeCode)) {
//            // 핸드폰번호 예시 (예: 10~11자리)
//            if (text.length() < 10 || text.length() > 11) {
//                throw new IllegalArgumentException("핸드폰번호는 10~11자리여야 합니다.");
//            }
//        } else {
//            throw new IllegalArgumentException("지원하지 않는 코드값입니다: " + typeCode);
//        }
//    }
}