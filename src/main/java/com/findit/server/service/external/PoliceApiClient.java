package com.findit.server.service.external;

import com.findit.server.dto.external.PoliceApiFoundItemResponse;
import com.findit.server.dto.external.PoliceApiLostItemResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import java.util.Collections;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * 경찰청 API 클라이언트 서비스 외부 경찰청 API와 통신하여 분실물 및 습득물 데이터를 가져옴
 */
@Service
public class PoliceApiClient {
  
  private static final Logger logger = LoggerFactory.getLogger(PoliceApiClient.class);
  // API Operation Paths
  private final String lostItemListPath;
  private final String foundItemListPath;
  private final RestTemplate restTemplate;
  private final String policeApiBaseUrl;
  private final String serviceKey;
  
  /**
   * 생성자
   *
   * @param restTemplate      RestTemplate 인스턴스
   * @param policeApiBaseUrl  경찰청 API 기본 URL (e.g., http://apis.data.go.kr/1320000)
   * @param serviceKey        API 서비스 키
   * @param lostItemListPath  분실물 목록 API 경로
   * @param foundItemListPath 습득물 목록 API 경로
   */
  public PoliceApiClient(RestTemplate restTemplate,
    @Value("${police.api.base-url}") String policeApiBaseUrl,
    @Value("${police.api.service-key}") String serviceKey,
    @Value("${police.api.lost-items-url}") String lostItemListPath,
    @Value("${police.api.found-items-url}") String foundItemListPath) {
    this.restTemplate = restTemplate;
    this.policeApiBaseUrl = policeApiBaseUrl;
    this.serviceKey = serviceKey;
    this.lostItemListPath = lostItemListPath;
    this.foundItemListPath = foundItemListPath;
  }
  
  /**
   * 경찰청 API에서 분실물 데이터 목록을 가져옴 오류 발생 시 재시도 로직 포함
   *
   * @param pageNo    페이지 번호
   * @param numOfRows 한 페이지 결과 수
   * @param startYmd  검색 시작일 (YYYYMMDD, 옵션)
   * @param endYmd    검색 종료일 (YYYYMMDD, 옵션)
   * @return PoliceApiLostItemResponse 분실물 API 응답 객체
   */
  @Retryable(value = {RestClientException.class}, maxAttempts = 3, backoff = @Backoff(delay = 1000))
  public PoliceApiLostItemResponse fetchLostItems(int pageNo, int numOfRows, String startYmd,
    String endYmd) {
    logger.info("Fetching lost items from Police API with URL: {}",
      policeApiBaseUrl + lostItemListPath);
    
    // serviceKey는 인코딩하지 않고, properties에 저장된 값을 그대로 사용
    StringBuilder urlBuilder = new StringBuilder();
    urlBuilder.append(policeApiBaseUrl).append(lostItemListPath)
      .append("?serviceKey=").append(serviceKey)
      .append("&pageNo=").append(pageNo)
      .append("&numOfRows=").append(numOfRows);
    if (startYmd != null && !startYmd.trim().isEmpty() && !"null".equalsIgnoreCase(
      startYmd.trim())) {
      urlBuilder.append("&START_YMD=").append(startYmd);
    }
    if (endYmd != null && !endYmd.trim().isEmpty() && !"null".equalsIgnoreCase(endYmd.trim())) {
      urlBuilder.append("&END_YMD=").append(endYmd);
    }
    String url = urlBuilder.toString();
    URI uri;
    try {
      uri = new URI(url);
    } catch (URISyntaxException e) {
      logger.error("Error creating URI for lost items: {}", e.getMessage(), e);
      PoliceApiLostItemResponse errorResponse = new PoliceApiLostItemResponse();
      errorResponse.setItems(Collections.emptyList());
      return errorResponse;
    }
    logger.info("Fetching lost items from Police API with URL: {}", uri);
    
    try {
      ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
      
      if (response.getBody() != null) {
        logger.info("Police API raw response (lost items): {}", response.getBody());
        logger.info("Successfully fetched lost items. Total: {}, Page: {}, Rows Requested: {}",
          response.getBody(), pageNo, numOfRows);
        // TODO: response.getBody()를 PoliceApiLostItemResponse로 변환
        PoliceApiLostItemResponse lostItemResponse = new PoliceApiLostItemResponse();
        lostItemResponse.setItems(Collections.emptyList());
        return lostItemResponse;
      } else {
        logger.warn("Received null body from Police API for lost items. URL: {}", uri);
        PoliceApiLostItemResponse emptyResponse = new PoliceApiLostItemResponse();
        emptyResponse.setItems(Collections.emptyList());
        return emptyResponse;
      }
    } catch (RestClientException e) {
      logger.error("Error fetching lost items from Police API (URL: {}): {}", uri, e.getMessage(),
        e);
      throw e;
    } catch (Exception e) {
      logger.error("Unexpected error fetching lost items from Police API (URL: {}): {}", uri,
        e.getMessage(), e);
      PoliceApiLostItemResponse errorResponse = new PoliceApiLostItemResponse();
      errorResponse.setItems(Collections.emptyList());
      return errorResponse;
    }
  }
  
  /**
   * 경찰청 API에서 습득물 데이터 목록을 가져옴 오류 발생 시 재시도 로직 포함
   *
   * @param pageNo    페이지 번호
   * @param numOfRows 한 페이지 결과 수
   * @param startYmd  검색 시작일 (YYYYMMDD, 옵션)
   * @param endYmd    검색 종료일 (YYYYMMDD, 옵션)
   * @return PoliceApiFoundItemResponse 습득물 API 응답 객체
   */
  @Retryable(value = {RestClientException.class}, maxAttempts = 3, backoff = @Backoff(delay = 1000))
  public PoliceApiFoundItemResponse fetchFoundItems(int pageNo, int numOfRows, String startYmd,
    String endYmd) {
    logger.info("Fetching found items from Police API with URL: {}",
      policeApiBaseUrl + foundItemListPath);
    
    // serviceKey는 인코딩하지 않고, properties에 저장된 값을 그대로 사용
    StringBuilder urlBuilder = new StringBuilder();
    urlBuilder.append(policeApiBaseUrl).append(foundItemListPath)
      .append("?serviceKey=").append(serviceKey)
      .append("&pageNo=").append(pageNo)
      .append("&numOfRows=").append(numOfRows);
    if (startYmd != null && !startYmd.trim().isEmpty() && !"null".equalsIgnoreCase(
      startYmd.trim())) {
      urlBuilder.append("&START_YMD=").append(startYmd);
    }
    if (endYmd != null && !endYmd.trim().isEmpty() && !"null".equalsIgnoreCase(endYmd.trim())) {
      urlBuilder.append("&END_YMD=").append(endYmd);
    }
    String url = urlBuilder.toString();
    URI uri;
    try {
      uri = new URI(url);
    } catch (URISyntaxException e) {
      logger.error("Error creating URI for found items: {}", e.getMessage(), e);
      PoliceApiFoundItemResponse errorResponse = new PoliceApiFoundItemResponse();
      errorResponse.setItems(Collections.emptyList());
      return errorResponse;
    }
    logger.info("Fetching found items from Police API with URL: {}", uri);
    
    try {
      ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
      
      if (response.getBody() != null) {
        logger.info("Police API raw response (found items): {}", response.getBody());
        logger.info("Successfully fetched found items. Total: {}, Page: {}, Rows: {}",
          response.getBody(), pageNo, numOfRows);
        // TODO: response.getBody()를 PoliceApiFoundItemResponse로 변환
        PoliceApiFoundItemResponse foundItemResponse = new PoliceApiFoundItemResponse();
        foundItemResponse.setItems(Collections.emptyList());
        return foundItemResponse;
      } else {
        logger.warn("Received null body from Police API for found items. URL: {}", uri);
        PoliceApiFoundItemResponse emptyResponse = new PoliceApiFoundItemResponse();
        emptyResponse.setItems(Collections.emptyList());
        return emptyResponse;
      }
    } catch (RestClientException e) {
      logger.error("Error fetching found items from Police API (URL: {}): {}", uri, e.getMessage(),
        e);
      throw e;
    } catch (Exception e) {
      logger.error("Unexpected error fetching found items from Police API (URL: {}): {}", uri,
        e.getMessage(), e);
      PoliceApiFoundItemResponse errorResponse = new PoliceApiFoundItemResponse();
      errorResponse.setItems(Collections.emptyList());
      return errorResponse;
    }
  }
}
