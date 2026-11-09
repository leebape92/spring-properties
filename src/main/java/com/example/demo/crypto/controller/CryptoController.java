package com.example.demo.crypto.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.crypto.dto.CryptoRequestDto;
import com.example.demo.crypto.service.CryptoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/crypto/test")
@RequiredArgsConstructor
public class CryptoController {


	private final CryptoService cryptoService;

    @PostMapping("/encrypt")
    public ResponseEntity<String> encrypt(@RequestBody CryptoRequestDto cryptoRequestDto) {
    	String encryptResult = cryptoService.encrypt(cryptoRequestDto);
        return ResponseEntity.ok(encryptResult);
    }
    
    @PostMapping("/decrypt")
    public ResponseEntity<String> decrypt(@RequestBody CryptoRequestDto cryptoRequestDto) {
        String decryptResult = cryptoService.decrypt(cryptoRequestDto);
        return ResponseEntity.ok(decryptResult);
    }
    
}