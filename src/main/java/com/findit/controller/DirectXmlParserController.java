package com.findit.controller;

import com.findit.service.DirectXmlParserService;
import com.findit.service.LostItemService;
import com.findit.util.CharsetDetector;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Slf4j
@RestController
@RequestMapping("/api/parse")
@RequiredArgsConstructor
public class DirectXmlParserController {

    private final DirectXmlParserService directXmlParserService;
    private final LostItemService lostItemService;

    private static final String BASE_URL = "https://apis.data.go.kr/1320000/LosfundInfoInqireService/getLosfundInfoAccToClAreaPd";
    private static final String SERVICE_KEY = "xBkbbknjXif3VR72NQfRK77qi02bgoenTRuwfQbYR43eraRP8eDLB84QlyKzwQ619S%2BIFpu6hSt%2FnefORgdcNg%3D%3D";

    @GetMapping("/direct-parse")
    public ResponseEntity<String> directParse() {
        try {
            String urlStr = BASE_URL +
                    "?serviceKey=" + SERVICE_KEY +
                    "&START_YMD=20240101&END_YMD=20240512&pageNo=1&numOfRows=10";
            
            log.info("API 호출 URL: {}", urlStr);
            
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");
            
            BufferedReader br;
            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                return ResponseEntity.status(500).body("API 호출 실패: " + conn.getResponseCode());
            }
            
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            
            br.close();
            conn.disconnect();
            
            String xmlResponse = sb.toString();
            log.info("API 응답 수신 완료");
            
            // 직접 파싱 및 저장 호출
            directXmlParserService.parseAndSaveWithDomParser(xmlResponse);
            
            return ResponseEntity.ok("직접 파싱 및 저장 완료");
            
        } catch (Exception e) {
            log.error("직접 파싱 중 오류: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("오류 발생: " + e.getMessage());
        }
    }
    @GetMapping("/jaxp-parse")
    public ResponseEntity<String> jaxpParse() {
        try {
            String urlStr = BASE_URL +
                    "?serviceKey=" + SERVICE_KEY +
                    "&START_YMD=20240101&END_YMD=20240512&pageNo=1&numOfRows=10";
        
            log.info("API 호출 URL: {}", urlStr);
        
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");
        
            BufferedReader br;
            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            } else {
                br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8));
                return ResponseEntity.status(500).body("API 호출 실패: " + conn.getResponseCode());
            }
        
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        
            br.close();
            conn.disconnect();
        
            String xmlResponse = sb.toString();
            log.info("API 응답 수신 완료, 길이: {}", xmlResponse.length());
        
            // JAXP 파서로 파싱 및 저장 호출
            lostItemService.saveLostItems(xmlResponse);
        
            return ResponseEntity.ok("JAXP 파서로 파싱 및 저장 완료");
        
        } catch (Exception e) {
            log.error("JAXP 파싱 중 오류: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("오류 발생: " + e.getMessage());
        }
    }
    @Autowired
private CharsetDetector charsetDetector;

@GetMapping("/detect-charset")
public ResponseEntity<String> detectCharset() {
    try {
        String urlStr = BASE_URL +
                "?serviceKey=" + SERVICE_KEY +
                "&START_YMD=20240101&END_YMD=20240512&pageNo=1&numOfRows=10";
        
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0");
        
        StringBuilder sb = new StringBuilder();
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
            }
        } else {
            return ResponseEntity.status(500).body("API 호출 실패: " + conn.getResponseCode());
        }
        
        String xmlResponse = sb.toString();
        log.info("API 응답 수신 완료, 길이: {}", xmlResponse.length());
        
        // 인코딩 감지
        charsetDetector.detectCharset(xmlResponse);
        
        return ResponseEntity.ok("인코딩 감지 완료 - 로그를 확인하세요");
        
    } catch (Exception e) {
        log.error("인코딩 감지 중 오류: {}", e.getMessage(), e);
        return ResponseEntity.internalServerError().body("오류 발생: " + e.getMessage());
    }
}
}