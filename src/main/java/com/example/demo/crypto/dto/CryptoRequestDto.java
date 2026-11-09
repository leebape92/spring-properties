package com.example.demo.crypto.dto;

import lombok.Data;

@Data
public class CryptoRequestDto {
	
	String cryptoType; // 01:암호화, 02:복호화
	String cryptoTypeCode; // 01:주민번호, 02:계좌번호, 03:핸드폰번호
	String cryptoText;
	
}
