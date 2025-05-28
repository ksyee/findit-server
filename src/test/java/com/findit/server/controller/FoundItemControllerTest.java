package com.findit.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.findit.server.dto.FoundItemDto;
import com.findit.server.service.FoundItemService;
import com.findit.server.service.external.PoliceApiClient;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FoundItemController.class)
@ActiveProfiles("test")
public class FoundItemControllerTest {

    @TestConfiguration
    static class MetricsConfigForTest {
        @Bean
        public MeterRegistry meterRegistry() {
            return new SimpleMeterRegistry();
        }
    }

    @TestConfiguration
    static class ServiceMockConfig {
        @Bean
        public FoundItemService foundItemService() {
            return Mockito.mock(FoundItemService.class);
        }

        @Bean
        public PoliceApiClient policeApiClient() {
            return Mockito.mock(PoliceApiClient.class);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FoundItemService foundItemService;

    @Autowired
    private PoliceApiClient policeApiClient;

    @Autowired
    private ObjectMapper objectMapper;

    private FoundItemDto foundItemDto;
    private List<FoundItemDto> foundItemDtoList;
    private LocalDateTime fixedDateTime;
    private static final DateTimeFormatter dateOnlyFormatter = DateTimeFormatter.ISO_LOCAL_DATE; // YYYY-MM-DD

    @BeforeEach
    void setUp() {
        fixedDateTime = LocalDateTime.now().withNano(0);

        foundItemDto = FoundItemDto.builder()
                .atcId("F_TEST_ATC_ID_1")
                .fdPrdtNm("아이폰 14 프로")
                .prdtClNm("전자기기")
                .depPlace("서울시 강남구 역삼동")
                .fdYmd(fixedDateTime.toLocalDate().toString())
                .fdSbjt("아이폰 14 프로 블랙 색상")
                .clrNm("블랙")
                .fdSn("1")
                .build();

        FoundItemDto foundItemDto2 = FoundItemDto.builder()
                .atcId("F_TEST_ATC_ID_2")
                .fdPrdtNm("가죽지갑")
                .prdtClNm("지갑")
                .depPlace("서울시 송파구 송파동")
                .fdYmd(fixedDateTime.toLocalDate().toString())
                .fdSbjt("가죽지갑 검정색")
                .clrNm("검정")
                .fdSn("2")
                .build();

        foundItemDtoList = Arrays.asList(foundItemDto, foundItemDto2);
    }

    @Test
    void getAllFoundItems() throws Exception {
        // Prepare a Page object for the mock response
        Page<FoundItemDto> pagedResponse = new PageImpl<>(foundItemDtoList, PageRequest.of(0, 20), foundItemDtoList.size());

        // Mock the service call with pagination arguments
        when(foundItemService.getAllFoundItems(any(Integer.class), any(Integer.class)))
                .thenReturn(pagedResponse);

        mockMvc.perform(get("/api/found-items")
                .param("page", "0")
                .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].atcId").value("F_TEST_ATC_ID_1"))
                .andExpect(jsonPath("$.data[0].fdPrdtNm").value(foundItemDto.getFdPrdtNm()))
                .andExpect(jsonPath("$.data[0].prdtClNm").value(foundItemDto.getPrdtClNm()))
                .andExpect(jsonPath("$.data[0].depPlace").value(foundItemDto.getDepPlace()))
                .andExpect(jsonPath("$.data[0].fdYmd").value(fixedDateTime.toLocalDate().toString()))
                .andExpect(jsonPath("$.data[0].fdSbjt").value(foundItemDto.getFdSbjt()))
                .andExpect(jsonPath("$.data[0].clrNm").value(foundItemDto.getClrNm()))
                .andExpect(jsonPath("$.data[0].fdSn").value(foundItemDto.getFdSn()))
                .andExpect(jsonPath("$.data[1].atcId").value("F_TEST_ATC_ID_2"))
                .andExpect(jsonPath("$.data[1].fdPrdtNm").value(foundItemDtoList.get(1).getFdPrdtNm()))
                .andExpect(jsonPath("$.data[1].prdtClNm").value(foundItemDtoList.get(1).getPrdtClNm()))
                .andExpect(jsonPath("$.data[1].depPlace").value(foundItemDtoList.get(1).getDepPlace()))
                .andExpect(jsonPath("$.data[1].fdYmd").value(fixedDateTime.toLocalDate().toString()))
                .andExpect(jsonPath("$.data[1].fdSbjt").value(foundItemDtoList.get(1).getFdSbjt()))
                .andExpect(jsonPath("$.data[1].clrNm").value(foundItemDtoList.get(1).getClrNm()))
                .andExpect(jsonPath("$.data[1].fdSn").value(foundItemDtoList.get(1).getFdSn()))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(20))
                .andExpect(jsonPath("$.totalElements").value(foundItemDtoList.size()))
                .andExpect(jsonPath("$.totalPages").value(1));
    }

    @Test
    void getFoundItemById() throws Exception {
        when(foundItemService.getFoundItemByAtcId("F_TEST_ATC_ID_1")).thenReturn(foundItemDto);

        mockMvc.perform(get("/api/found-items/F_TEST_ATC_ID_1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.atcId").value(foundItemDto.getAtcId()))
                .andExpect(jsonPath("$.data.fdPrdtNm").value(foundItemDto.getFdPrdtNm()))
                .andExpect(jsonPath("$.data.prdtClNm").value(foundItemDto.getPrdtClNm()))
                .andExpect(jsonPath("$.data.depPlace").value(foundItemDto.getDepPlace()))
                .andExpect(jsonPath("$.data.fdYmd").value(fixedDateTime.toLocalDate().toString()))
                .andExpect(jsonPath("$.data.fdSbjt").value(foundItemDto.getFdSbjt()))
                .andExpect(jsonPath("$.data.clrNm").value(foundItemDto.getClrNm()))
                .andExpect(jsonPath("$.data.fdSn").value(foundItemDto.getFdSn()));
    }

    @Test
    void createFoundItem() throws Exception {
        when(foundItemService.createFoundItem(any(FoundItemDto.class))).thenReturn(foundItemDto);

        mockMvc.perform(post("/api/found-items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(foundItemDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("등록 성공"))
                .andExpect(jsonPath("$.data.atcId").value(foundItemDto.getAtcId()))
                .andExpect(jsonPath("$.data.fdPrdtNm").value(foundItemDto.getFdPrdtNm()))
                .andExpect(jsonPath("$.data.prdtClNm").value(foundItemDto.getPrdtClNm()))
                .andExpect(jsonPath("$.data.depPlace").value(foundItemDto.getDepPlace()))
                .andExpect(jsonPath("$.data.fdYmd").value(fixedDateTime.toLocalDate().toString()))
                .andExpect(jsonPath("$.data.fdSbjt").value(foundItemDto.getFdSbjt()))
                .andExpect(jsonPath("$.data.clrNm").value(foundItemDto.getClrNm()))
                .andExpect(jsonPath("$.data.fdSn").value(foundItemDto.getFdSn()));
    }

    @Test
    void updateFoundItem() throws Exception {
        when(foundItemService.updateFoundItem(eq("F_TEST_ATC_ID_1"), any(FoundItemDto.class))).thenReturn(foundItemDto);

        mockMvc.perform(put("/api/found-items/F_TEST_ATC_ID_1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(foundItemDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("수정 성공"))
                .andExpect(jsonPath("$.data.atcId").value(foundItemDto.getAtcId()))
                .andExpect(jsonPath("$.data.fdPrdtNm").value(foundItemDto.getFdPrdtNm()))
                .andExpect(jsonPath("$.data.prdtClNm").value(foundItemDto.getPrdtClNm()))
                .andExpect(jsonPath("$.data.depPlace").value(foundItemDto.getDepPlace()))
                .andExpect(jsonPath("$.data.fdYmd").value(fixedDateTime.toLocalDate().toString()))
                .andExpect(jsonPath("$.data.fdSbjt").value(foundItemDto.getFdSbjt()))
                .andExpect(jsonPath("$.data.clrNm").value(foundItemDto.getClrNm()))
                .andExpect(jsonPath("$.data.fdSn").value(foundItemDto.getFdSn()));
    }

    @Test
    void findByItemType() throws Exception {
        Page<FoundItemDto> pagedResponse = new PageImpl<>(Arrays.asList(foundItemDto), PageRequest.of(0, 20), 1);

        when(foundItemService.findByItemType(eq("전자기기"), any(Integer.class), any(Integer.class)))
                .thenReturn(pagedResponse);

        mockMvc.perform(get("/api/found-items/type/전자기기")
                .param("page", "0")
                .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].atcId").value(foundItemDto.getAtcId()))
                .andExpect(jsonPath("$.data[0].fdPrdtNm").value(foundItemDto.getFdPrdtNm()))
                .andExpect(jsonPath("$.data[0].prdtClNm").value(foundItemDto.getPrdtClNm()))
                .andExpect(jsonPath("$.data[0].depPlace").value(foundItemDto.getDepPlace()))
                .andExpect(jsonPath("$.data[0].fdYmd").value(fixedDateTime.toLocalDate().toString()))
                .andExpect(jsonPath("$.data[0].fdSbjt").value(foundItemDto.getFdSbjt()))
                .andExpect(jsonPath("$.data[0].clrNm").value(foundItemDto.getClrNm()))
                .andExpect(jsonPath("$.data[0].fdSn").value(foundItemDto.getFdSn()))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(20))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1));
    }

    @Test
    void searchByKeyword() throws Exception {
        Page<FoundItemDto> pagedResponse = new PageImpl<>(Arrays.asList(foundItemDto), PageRequest.of(0, 20), 1);

        when(foundItemService.searchByKeyword(eq("아이폰"), any(Integer.class), any(Integer.class)))
                .thenReturn(pagedResponse);

        mockMvc.perform(get("/api/found-items/search")
                .param("keyword", "아이폰")
                .param("page", "0")
                .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].atcId").value(foundItemDto.getAtcId()))
                .andExpect(jsonPath("$.data[0].fdPrdtNm").value(foundItemDto.getFdPrdtNm()))
                .andExpect(jsonPath("$.data[0].prdtClNm").value(foundItemDto.getPrdtClNm()))
                .andExpect(jsonPath("$.data[0].depPlace").value(foundItemDto.getDepPlace()))
                .andExpect(jsonPath("$.data[0].fdYmd").value(fixedDateTime.toLocalDate().toString()))
                .andExpect(jsonPath("$.data[0].fdSbjt").value(foundItemDto.getFdSbjt()))
                .andExpect(jsonPath("$.data[0].clrNm").value(foundItemDto.getClrNm()))
                .andExpect(jsonPath("$.data[0].fdSn").value(foundItemDto.getFdSn()))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(20))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1));
    }
}
