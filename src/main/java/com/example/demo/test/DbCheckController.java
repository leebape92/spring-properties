package com.example.demo.test;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class DbCheckController {

    private final JdbcTemplate jdbcTemplate; // 스프링이 자동으로 연동해준 도구

    @GetMapping("/check-db")
    public String check() {
        try {
            // 현재 시간을 조회하는 아주 가벼운 쿼리를 날려봅니다. (MySQL 기준)
            String now = jdbcTemplate.queryForObject("SELECT NOW()", String.class);
            return "연결 성공! DB 시간: " + now;
        } catch (Exception e) {
            return "연결 실패: " + e.getMessage();
        }
    }
}
