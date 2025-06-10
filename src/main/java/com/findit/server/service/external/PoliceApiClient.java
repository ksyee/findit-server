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
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import java.io.StringReader;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

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
      // Raw XML 응답 수신 및 로깅
      ResponseEntity<String> xmlResponse = restTemplate.getForEntity(uri, String.class);
      String xmlBody = xmlResponse.getBody();
      if (xmlBody == null) xmlBody = "";
      // BOM 제거 후 trim
      xmlBody = xmlBody.replace("\uFEFF", "").trim();
      logger.info("[Raw XML 분실물 응답]\n{}", xmlBody);
      // JSON 응답 처리 (XML 대신 JSON이 반환된 경우)
      if (xmlBody.startsWith("{")) {
        try {
          ObjectMapper objectMapper = new ObjectMapper();
          JsonNode rootNode = objectMapper.readTree(xmlBody).path("response");
          JsonNode headerNode = rootNode.path("header");
          String resultCodeJson = headerNode.path("resultCode").asText();
          String resultMsgJson = headerNode.path("resultMsg").asText();
          PoliceApiLostItemResponse jsonResponse = new PoliceApiLostItemResponse();
          PoliceApiLostItemResponse.Header header = new PoliceApiLostItemResponse.Header();
          header.setResultCode(resultCodeJson);
          header.setResultMsg(resultMsgJson);
          jsonResponse.setHeader(header);
          jsonResponse.setItems(Collections.emptyList());
          return jsonResponse;
        } catch (Exception e) {
          logger.error("JSON 파싱 오류(분실물): {}. Response JSON:\n{}", e.getMessage(), xmlBody, e);
          PoliceApiLostItemResponse errorResponseJson = new PoliceApiLostItemResponse();
          errorResponseJson.setItems(Collections.emptyList());
          return errorResponseJson;
        }
      }
      // 수동 JAXB 언마샬링
      PoliceApiLostItemResponse lostItemResponse;
      try {
        JAXBContext jc = JAXBContext.newInstance(PoliceApiLostItemResponse.class);
        Unmarshaller um = jc.createUnmarshaller();
        lostItemResponse = (PoliceApiLostItemResponse) um.unmarshal(new StringReader(xmlBody));
      } catch (JAXBException e) {
        logger.error("XML 언마샬링 오류(분실물): {}. Response XML:\n{}", e.getMessage(), xmlBody, e);
        lostItemResponse = new PoliceApiLostItemResponse();
        lostItemResponse.setItems(Collections.emptyList());
      }
      logger.info("Parsed lost items. Total: {}, Page: {}, Rows: {}",
        lostItemResponse.getTotalCount(), lostItemResponse.getPageNo(), lostItemResponse.getNumOfRows());
      return lostItemResponse;
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
    logger.info("Fetching found items from Police API with URL: {}", policeApiBaseUrl + foundItemListPath);
    
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
      // Raw XML 응답 수신 및 로깅
      ResponseEntity<String> xmlResponse = restTemplate.getForEntity(uri, String.class);
      String xmlBody = xmlResponse.getBody();
      if (xmlBody == null) xmlBody = "";
      // BOM 제거 후 trim
      xmlBody = xmlBody.replace("\uFEFF", "").trim();
      logger.info("[Raw XML 습득물 응답]\n{}", xmlBody);
      // JSON 응답 처리 (XML 대신 JSON이 반환된 경우)
      if (xmlBody.startsWith("{")) {
        try {
          ObjectMapper objectMapper = new ObjectMapper();
          JsonNode rootNode = objectMapper.readTree(xmlBody).path("response");
          JsonNode headerNode = rootNode.path("header");
          String resultCodeJson = headerNode.path("resultCode").asText();
          String resultMsgJson = headerNode.path("resultMsg").asText();
          PoliceApiFoundItemResponse jsonResponse = new PoliceApiFoundItemResponse();
          PoliceApiFoundItemResponse.Header header = new PoliceApiFoundItemResponse.Header();
          header.setResultCode(resultCodeJson);
          header.setResultMsg(resultMsgJson);
          jsonResponse.setHeader(header);
          jsonResponse.setItems(Collections.emptyList());
          return jsonResponse;
        } catch (Exception e) {
          logger.error("JSON 파싱 오류(습득물): {}. Response JSON:\n{}", e.getMessage(), xmlBody, e);
          PoliceApiFoundItemResponse errorResponseJson = new PoliceApiFoundItemResponse();
          errorResponseJson.setItems(Collections.emptyList());
          return errorResponseJson;
        }
      }
      // 수동 JAXB 언마샬링
      PoliceApiFoundItemResponse foundItemResponse;
      try {
        JAXBContext jc = JAXBContext.newInstance(PoliceApiFoundItemResponse.class);
        Unmarshaller um = jc.createUnmarshaller();
        foundItemResponse = (PoliceApiFoundItemResponse) um.unmarshal(new StringReader(xmlBody));
      } catch (JAXBException e) {
        logger.error("XML 언마샬링 오류(습득물): {}. Response XML:\n{}", e.getMessage(), xmlBody, e);
        foundItemResponse = new PoliceApiFoundItemResponse();
        foundItemResponse.setItems(Collections.emptyList());
      }
      logger.info("Parsed found items. Total: {}, Page: {}, Rows: {}",
        foundItemResponse.getTotalCount(), foundItemResponse.getPageNo(), foundItemResponse.getNumOfRows());
      return foundItemResponse;
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
