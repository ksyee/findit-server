package com.findit.service;

import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class LostItemApiService {

    private final WebClient webClient;
    private final LostItemService lostItemService;

    private static final String BASE_URL = "https://apis.data.go.kr/1320000/LosfundInfoInqireService/getLosfundInfoAccToClAreaPd";
    private static final String SERVICE_KEY = "xBkbbknjXif3VR72NQfRK77qi02bgoenTRuwfQbYR43eraRP8eDLB84QlyKzwQ619S%2BIFpu6hSt%2FnefORgdcNg%3D%3D";

    /**
     * HttpURLConnection을 사용하여 API 호출 후 결과를 DB에 저장
     */
    public void fetchAndSaveLostItems() {
        try {
            // 오늘부터 3개월 전까지의 데이터 조회
            LocalDate endDate = LocalDate.now();
            LocalDate startDate = endDate.minusMonths(3);
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            String startYmd = startDate.format(formatter);
            String endYmd = endDate.format(formatter);
            int pageNo = 1;
            int numOfRows = 50; // 한 번에 가져올 데이터 수
            
            String urlStr = BASE_URL +
                    "?serviceKey=" + SERVICE_KEY +
                    "&START_YMD=" + startYmd +
                    "&END_YMD=" + endYmd +
                    "&pageNo=" + pageNo +
                    "&numOfRows=" + numOfRows;
            
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
                log.error("API 호출 실패: {}", conn.getResponseCode());
            }
            
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            
            br.close();
            conn.disconnect();
            
            String response = sb.toString();
            log.info("API 응답 수신 완료");
            
            // 응답 XML을 파싱하여 DB에 저장
            lostItemService.saveLostItems(response);
            
        } catch (Exception e) {
            log.error("API 호출 중 오류 발생: {}", e.getMessage(), e);
        }
    }

    /**
     * WebClient를 사용하여 API 호출 후 결과를 DB에 저장 (WebClient 사용 방식)
     */
    public void fetchAndSaveLostItemsUsingWebClient() {
        // 오늘부터 3개월 전까지의 데이터 조회
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusMonths(3);
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String startYmd = startDate.format(formatter);
        String endYmd = endDate.format(formatter);
        
        String urlStr = UriComponentsBuilder.fromUriString(BASE_URL)
                .queryParam("serviceKey", SERVICE_KEY)
                .queryParam("START_YMD", startYmd)
                .queryParam("END_YMD", endYmd)
                .queryParam("pageNo", 1)
                .queryParam("numOfRows", 100)
                .build(false)
                .toUriString();
        
        log.info("API 호출 URL: {}", urlStr);
        
        webClient
                .get()
                .uri(urlStr)
                .accept(MediaType.APPLICATION_XML)
                .retrieve()
                .bodyToMono(String.class)
                .subscribe(
                        response -> {
                            log.info("API 응답 수신 완료");
                            // 응답 XML을 파싱하여 DB에 저장
                            lostItemService.saveLostItems(response);
                        },
                        error -> {
                            log.error("API 호출 실패: {}", error.getMessage(), error);
                        }
                );
    }

    /**
     * 테스트용 API 호출 메서드
     */
    public void callLostItemApi() {
        try {
            String urlStr = BASE_URL +
                    "?serviceKey=" + SERVICE_KEY +
                    "&START_YMD=20240101&END_YMD=20240512&pageNo=1&numOfRows=10";
            
            log.info("url: {}", urlStr);
            
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");
            
            BufferedReader br;
            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            
            br.close();
            conn.disconnect();
            
            log.info("응답 바디:");
            log.info(sb.toString());
            
            // API 호출 성공 시 데이터 저장
            lostItemService.saveLostItems(sb.toString());
            
        } catch (Exception e) {
            log.error("API 호출 중 오류 발생: {}", e.getMessage(), e);
        }
    }
}