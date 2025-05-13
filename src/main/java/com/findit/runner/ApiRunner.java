package com.findit.runner;

import com.findit.service.LostItemApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApiRunner implements CommandLineRunner {

    private final LostItemApiService lostItemApiService;

    @Override
    public void run(String... args) throws Exception {
        log.info("애플리케이션 시작 - 분실물 API 호출 및 데이터 저장 시작");
        
        // API 호출 및 데이터 저장
        lostItemApiService.callLostItemApi();
        
        log.info("분실물 API 호출 및 데이터 저장 완료");
    }
}