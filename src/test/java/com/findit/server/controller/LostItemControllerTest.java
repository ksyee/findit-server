package com.findit.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.findit.server.dto.LostItemDto;
import com.findit.server.interceptor.RequestCounterInterceptor;
import com.findit.server.service.LostItemService;
import com.findit.server.service.collection.FoundItemCollectionService;
import com.findit.server.service.collection.LostItemCollectionService;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@ActiveProfiles("test")
public class LostItemControllerTest {

  private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
    "yyyy-MM-dd'T'HH:mm:ss");
  // MockMvc will be set up manually in setUp method
  private MockMvc mockMvc;
  @Autowired
  private WebApplicationContext webApplicationContext;
  @Autowired
  private ObjectMapper objectMapper;
  @Autowired
  private LostItemService lostItemService;
  @Autowired
  private LostItemCollectionService lostItemCollectionService;
  @Autowired
  private FoundItemCollectionService foundItemCollectionService;
  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;
  private LostItemDto lostItemDto;
  private List<LostItemDto> lostItemDtoList;
  private LocalDateTime fixedDateTime;
  private String fixedDateTimeString;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);

    LostItemController controller = new LostItemController(lostItemService);

    jacksonMessageConverter.setObjectMapper(this.objectMapper);

    this.mockMvc = MockMvcBuilders.standaloneSetup(controller)
      .setMessageConverters(jacksonMessageConverter)
      .build();

    fixedDateTime = LocalDateTime.now().withNano(0);
    fixedDateTimeString = fixedDateTime.format(formatter);

    lostItemDto = LostItemDto.builder()
      .atcId("L_TEST_ATC_ID_1")
      .sltPrdtNm("아이폰 14 프로")
      .prdtClNm("전자기기")
      .lstPlace("서울시 강남구 역삼동")
      .lstYmd(fixedDateTime.toLocalDate().toString())
      .sltSbjt("아이폰 14 프로 블랙 색상")
      .build();

    LostItemDto lostItemDto2 = LostItemDto.builder()
      .atcId("L_TEST_ATC_ID_2")
      .sltPrdtNm("지갑")
      .prdtClNm("지갑")
      .lstPlace("서울시 송파구 송파동")
      .lstYmd(fixedDateTime.toLocalDate().toString())
      .sltSbjt("가죽지갑 검정색")
      .build();

    lostItemDtoList = Arrays.asList(lostItemDto, lostItemDto2);
  }

  @Test
  void getAllLostItems() throws Exception {
    Page<LostItemDto> pagedResponse = new PageImpl<>(lostItemDtoList, PageRequest.of(0, 20),
      lostItemDtoList.size());

    when(lostItemService.getAllLostItems(any(Integer.class), any(Integer.class)))
      .thenReturn(pagedResponse);

    mockMvc.perform(get("/api/lost-items")
        .param("page", "0")
        .param("size", "20"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.success").value(true))
      .andExpect(jsonPath("$.data", hasSize(2)))
      .andExpect(jsonPath("$.data[0].atcId").value("L_TEST_ATC_ID_1"))
      .andExpect(jsonPath("$.data[0].prdtClNm").value(lostItemDto.getPrdtClNm()))
      .andExpect(jsonPath("$.data[0].lstYmd").value(fixedDateTime.toLocalDate().toString()))
      .andExpect(jsonPath("$.data[1].atcId").value("L_TEST_ATC_ID_2"))
      .andExpect(jsonPath("$.data[1].prdtClNm").value(lostItemDtoList.get(1).getPrdtClNm()))
      .andExpect(jsonPath("$.data[1].lstYmd").value(fixedDateTime.toLocalDate().toString()))
      .andExpect(jsonPath("$.page").value(0))
      .andExpect(jsonPath("$.size").value(20))
      .andExpect(jsonPath("$.totalElements").value(lostItemDtoList.size()))
      .andExpect(jsonPath("$.totalPages").value(1));
  }

  @Test
  void getLostItemById() throws Exception {
    when(lostItemService.getLostItemByAtcId("L_TEST_ATC_ID_1")).thenReturn(lostItemDto);

    mockMvc.perform(get("/api/lost-items/L_TEST_ATC_ID_1")
        .accept(MediaType.APPLICATION_JSON))
      .andDo(print())
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.atcId").value(lostItemDto.getAtcId()))
      .andExpect(jsonPath("$.prdtClNm").value(lostItemDto.getPrdtClNm()))
      .andExpect(jsonPath("$.lstYmd").value(fixedDateTime.toLocalDate().toString()))
      .andExpect(jsonPath("$.lstPlace").value(lostItemDto.getLstPlace()))
      .andExpect(jsonPath("$.sltSbjt").value(lostItemDto.getSltSbjt()));
  }

  @Test
  void createLostItem() throws Exception {
    when(lostItemService.createLostItem(any(LostItemDto.class))).thenReturn(lostItemDto);

    mockMvc.perform(post("/api/lost-items")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(lostItemDto)))
      .andDo(print())
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.success").value(true))
      .andExpect(jsonPath("$.message").value("Lost item created successfully"))
      .andExpect(jsonPath("$.data.atcId").value(lostItemDto.getAtcId()))
      .andExpect(jsonPath("$.data.prdtClNm").value(lostItemDto.getPrdtClNm()))
      .andExpect(jsonPath("$.data.lstYmd").value(fixedDateTime.toLocalDate().toString()))
      .andExpect(jsonPath("$.data.lstPlace").value(lostItemDto.getLstPlace()))
      .andExpect(jsonPath("$.data.sltSbjt").value(lostItemDto.getSltSbjt()));
  }

  @Test
  void updateLostItem() throws Exception {
    when(lostItemService.updateLostItem(eq("L_TEST_ATC_ID_1"), any(LostItemDto.class))).thenReturn(lostItemDto);

    mockMvc.perform(put("/api/lost-items/L_TEST_ATC_ID_1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(lostItemDto)))
      .andDo(print())
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.success").value(true))
      .andExpect(jsonPath("$.message").value("Lost item updated successfully"))
      .andExpect(jsonPath("$.data.atcId").value(lostItemDto.getAtcId()))
      .andExpect(jsonPath("$.data.prdtClNm").value(lostItemDto.getPrdtClNm()))
      .andExpect(jsonPath("$.data.lstYmd").value(fixedDateTime.toLocalDate().toString()))
      .andExpect(jsonPath("$.data.lstPlace").value(lostItemDto.getLstPlace()))
      .andExpect(jsonPath("$.data.sltSbjt").value(lostItemDto.getSltSbjt()));
  }

  @Test
  void findByItemType() throws Exception {
    Page<LostItemDto> pagedResponse = new PageImpl<>(Collections.singletonList(lostItemDto),
      PageRequest.of(0, 20), 1);

    when(lostItemService.findByPrdtClNm(eq("전자기기"), any(Integer.class), any(Integer.class)))
      .thenReturn(pagedResponse);

    mockMvc.perform(get("/api/lost-items/prdt-cl-nm/전자기기")
        .param("page", "0")
        .param("size", "20"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.success").value(true))
      .andExpect(jsonPath("$.data", hasSize(1)))
      .andExpect(jsonPath("$.data[0].prdtClNm").value("전자기기"))
      .andExpect(jsonPath("$.data[0].lstYmd").value(fixedDateTime.toLocalDate().toString()))
      .andExpect(jsonPath("$.page").value(0))
      .andExpect(jsonPath("$.size").value(20))
      .andExpect(jsonPath("$.totalElements").value(1))
      .andExpect(jsonPath("$.totalPages").value(1));
  }

  @Test
  void searchByKeyword() throws Exception {
    Page<LostItemDto> pagedResponse = new PageImpl<>(Collections.singletonList(lostItemDto),
      PageRequest.of(0, 20), 1);

    when(lostItemService.searchByKeyword(eq("아이폰"), any(Integer.class), any(Integer.class)))
      .thenReturn(pagedResponse);

    mockMvc.perform(get("/api/lost-items/search")
        .param("keyword", "아이폰")
        .param("page", "0")
        .param("size", "20"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.success").value(true))
      .andExpect(jsonPath("$.data", hasSize(1)))
      .andExpect(jsonPath("$.data[0].sltSbjt").value(lostItemDto.getSltSbjt()))
      .andExpect(jsonPath("$.data[0].lstYmd").value(fixedDateTime.toLocalDate().toString()))
      .andExpect(jsonPath("$.page").value(0))
      .andExpect(jsonPath("$.size").value(20))
      .andExpect(jsonPath("$.totalElements").value(1))
      .andExpect(jsonPath("$.totalPages").value(1));
  }

  // Test Configuration for Mock Beans and explicit MessageConverter
  @TestConfiguration
  static class LostItemControllerTestConfig {

    @Bean
    public LostItemService lostItemService() {
      return Mockito.mock(LostItemService.class);
    }

    @Bean
    public LostItemCollectionService lostItemCollectionService() {
      return Mockito.mock(LostItemCollectionService.class);
    }

    @Bean
    public FoundItemCollectionService foundItemCollectionService() {
      return Mockito.mock(FoundItemCollectionService.class);
    }

    @Bean
    public RequestCounterInterceptor requestCounterInterceptor() {
      return Mockito.mock(RequestCounterInterceptor.class);
    }

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter(
      ObjectMapper objectMapper) {
      MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
      converter.setObjectMapper(objectMapper);
      return converter;
    }
  }

  @TestConfiguration
  static class MetricsConfigForTest {

    @Bean
    MeterRegistry meterRegistry() {
      return new SimpleMeterRegistry();
    }
  }
}
