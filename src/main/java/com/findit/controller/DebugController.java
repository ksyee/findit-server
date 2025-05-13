package com.findit.controller;

import com.findit.domain.LostItem;
import com.findit.dto.LostItemDto;
import com.findit.repository.LostItemRepository;
import com.findit.service.LostItemApiService;
import com.findit.service.LostItemService;
import com.findit.service.SaxXmlParserService;
import com.findit.util.XmlParserUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

@Slf4j
@RestController
@RequestMapping("/api/debug")
@RequiredArgsConstructor
public class DebugController {

    private final LostItemApiService lostItemApiService;
    private final LostItemService lostItemService;
    private final LostItemRepository lostItemRepository;
    private final XmlParserUtil xmlParserUtil;

    private static final String BASE_URL = "https://apis.data.go.kr/1320000/LosfundInfoInqireService/getLosfundInfoAccToClAreaPd";
    private static final String SERVICE_KEY = "xBkbbknjXif3VR72NQfRK77qi02bgoenTRuwfQbYR43eraRP8eDLB84QlyKzwQ619S%2BIFpu6hSt%2FnefORgdcNg%3D%3D";

    private final SaxXmlParserService saxXmlParserService;

    @GetMapping("/fetch-api")
    public ResponseEntity<Map<String, Object>> fetchApi() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String urlStr = BASE_URL +
                    "?serviceKey=" + SERVICE_KEY +
                    "&START_YMD=20240101&END_YMD=20240512&pageNo=1&numOfRows=10";
            
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");
            
            BufferedReader br;
            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                response.put("error", "API 호출 실패: " + conn.getResponseCode());
                return ResponseEntity.ok(response);
            }
            
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            
            br.close();
            conn.disconnect();
            
            String xmlResponse = sb.toString();
            response.put("apiResponse", xmlResponse.substring(0, Math.min(1000, xmlResponse.length())));
            
            // XML 파싱 시도
            List<LostItemDto> items = xmlParserUtil.parseLostItems(xmlResponse);
            response.put("parsedItemCount", items.size());
            if (!items.isEmpty()) {
                response.put("firstItem", items.get(0));
            }
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("디버깅 API 호출 중 오류: {}", e.getMessage(), e);
            response.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    @GetMapping("/test-db")
    public ResponseEntity<Map<String, Object>> testDb() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 테스트 데이터 생성
            LostItem testItem = new LostItem();
            testItem.setAtcId("TEST_" + System.currentTimeMillis());
            testItem.setFdPrdtNm("테스트 물품");
            testItem.setClrNm("블랙");
            testItem.setDepPlace("테스트 장소");
            testItem.setFdSbjt("테스트 설명");
            testItem.setFdSn(1);
            testItem.setFdYmd("2024-05-14");
            testItem.setPrdtClNm("테스트 분류");
            
            // DB에 저장
            LostItem savedItem = lostItemRepository.save(testItem);
            response.put("savedItem", savedItem);
            
            // DB에서 다시 조회
            List<LostItem> allItems = lostItemRepository.findAll();
            response.put("totalItemCount", allItems.size());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("데이터베이스 테스트 중 오류: {}", e.getMessage(), e);
            response.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping("/sax-parse")
    public ResponseEntity<String> saxParse() {
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
            log.info("API 응답 수신 완료, 길이: {}", xmlResponse.length());
        
            // SAX 파서로 파싱 및 저장 호출
            saxXmlParserService.parseAndSaveWithSaxParser(xmlResponse);
        
            return ResponseEntity.ok("SAX 파서로 파싱 및 저장 완료");
        
        } catch (Exception e) {
            log.error("SAX 파싱 중 오류: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("오류 발생: " + e.getMessage());
        }
    }
    @GetMapping("/manual-insert")
    public ResponseEntity<String> manualInsert() {
        try {
            List<LostItem> items = new ArrayList<>();
            
            // 수동으로 데이터 생성 (API 응답에서 가져온 샘플 데이터)
            LostItem item1 = new LostItem();
            item1.setAtcId("F2025051300002924");
            item1.setClrNm("엘로우(노랑)");
            item1.setDepPlace("광주북부경찰서");
            item1.setFdFilePathImg("https://www.lost112.go.kr/lostnfs/images/sub/img02_no_img.gif");
            item1.setFdPrdtNm("귀걸이");
            item1.setFdSbjt("귀걸이(엘로우(노랑)색)을 습득하여 보관하고 있습니다.");
            item1.setFdSn(1);
            item1.setFdYmd("2024-04-05");
            item1.setPrdtClNm("귀금속 > 귀걸이");
            items.add(item1);
            
            LostItem item2 = new LostItem();
            item2.setAtcId("F2025051300001518");
            item2.setClrNm("엘로우(밝은노랑)");
            item2.setDepPlace("여수경찰서");
            item2.setFdFilePathImg("https://www.lost112.go.kr/lostnfs/images/sub/img02_no_img.gif");
            item2.setFdPrdtNm("현대카드");
            item2.setFdSbjt("현대카드(엘로우(밝은노랑)색)을 습득하여 보관하고 있습니다.");
            item2.setFdSn(1);
            item2.setFdYmd("2024-05-09");
            item2.setPrdtClNm("카드 > 신용(체크)카드");
            items.add(item2);
            
            // 데이터베이스에 저장
            List<LostItem> savedItems = lostItemRepository.saveAll(items);
            
            return ResponseEntity.ok("수동으로 " + savedItems.size() + "개의 아이템을 DB에 저장했습니다.");
        } catch (Exception e) {
            log.error("수동 데이터 저장 중 오류: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("오류 발생: " + e.getMessage());
        }
    }
}