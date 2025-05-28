package com.findit.server.controller;

import com.findit.server.dto.LostItemDto;
import com.findit.server.exception.InvalidRequestException;
import com.findit.server.service.LostItemService;
import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/lost-items")
@RequiredArgsConstructor
@Tag(name = "Lost Items", description = "분실물 관리 API")
@Validated
@Timed(value = "api.lost_items", description = "Lost items API timer")
public class LostItemController {

    private final LostItemService lostItemService;
    
    @GetMapping
    @Operation(summary = "모든 분실물 조회", description = "등록된 모든 분실물을 페이지별로 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = com.findit.server.dto.ApiResponse.class))),
        @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<com.findit.server.dto.ApiResponse<List<LostItemDto>>> getAllLostItems(
            @Parameter(description = "페이지 번호", required = false) @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "페이지 크기", required = false) @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size) {
        Page<LostItemDto> lostItemsPage = lostItemService.getAllLostItems(page, size);
        List<LostItemDto> lostItems = lostItemsPage.getContent();
        
        com.findit.server.dto.ApiResponse<List<LostItemDto>> response = com.findit.server.dto.ApiResponse.success(lostItems);
        response.setPage(page);
        response.setSize(size);
        response.setTotalElements(lostItemsPage.getTotalElements());
        response.setTotalPages(lostItemsPage.getTotalPages());
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping(value = "/{atcId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "분실물 상세 조회", description = "특정 ID의 분실물 상세 정보를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = LostItemDto.class))),
        @ApiResponse(responseCode = "404", description = "분실물을 찾을 수 없음"),
        @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<LostItemDto> getLostItemByAtcId(
            @Parameter(description = "분실물 관리 ID", required = true) @PathVariable String atcId) {
        try {
            LostItemDto lostItem = lostItemService.getLostItemByAtcId(atcId);
            return ResponseEntity.ok(lostItem);
        } catch (RuntimeException e) { 
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); 
        }
    }
    
    @PostMapping
    @Operation(summary = "분실물 등록", description = "새로운 분실물 정보를 등록합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "등록 성공", content = @Content(schema = @Schema(implementation = com.findit.server.dto.ApiResponse.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<com.findit.server.dto.ApiResponse<LostItemDto>> createLostItem(
            @Parameter(description = "분실물 정보", required = true) @RequestBody @Valid LostItemDto lostItemDto) {
        LostItemDto createdLostItem = lostItemService.createLostItem(lostItemDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(com.findit.server.dto.ApiResponse.success("Lost item created successfully", createdLostItem));
    }
    
    @PutMapping("/{atcId}")
    @Operation(summary = "분실물 정보 수정", description = "특정 ID의 분실물 정보를 수정합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "수정 성공", content = @Content(schema = @Schema(implementation = com.findit.server.dto.ApiResponse.class))),
        @ApiResponse(responseCode = "404", description = "분실물을 찾을 수 없음"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<com.findit.server.dto.ApiResponse<LostItemDto>> updateLostItem(
            @Parameter(description = "분실물 관리 ID", required = true) @PathVariable String atcId,
            @Parameter(description = "수정할 분실물 정보", required = true) @RequestBody @Valid LostItemDto lostItemDto) {
        try {
            LostItemDto updatedLostItem = lostItemService.updateLostItem(atcId, lostItemDto);
            return ResponseEntity.ok(com.findit.server.dto.ApiResponse.success("Lost item updated successfully", updatedLostItem));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(com.findit.server.dto.ApiResponse.error(e.getMessage()));
        }
    }
    
    @DeleteMapping("/{atcId}")
    @Operation(summary = "분실물 삭제", description = "특정 ID의 분실물 정보를 삭제합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "삭제 성공", content = @Content(schema = @Schema(implementation = com.findit.server.dto.ApiResponse.class))),
        @ApiResponse(responseCode = "404", description = "분실물을 찾을 수 없음"),
        @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<com.findit.server.dto.ApiResponse<Void>> deleteLostItem(
            @Parameter(description = "분실물 관리 ID", required = true) @PathVariable String atcId) {
        try {
            lostItemService.deleteLostItem(atcId);
            return ResponseEntity.ok(com.findit.server.dto.ApiResponse.success("Lost item deleted successfully", null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(com.findit.server.dto.ApiResponse.error(e.getMessage()));
        }
    }
    
    @GetMapping("/prdt-cl-nm/{prdtClNm}")
    @Operation(summary = "물품 분류명으로 조회", description = "특정 물품 분류명에 해당하는 분실물을 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = com.findit.server.dto.ApiResponse.class))),
        @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<com.findit.server.dto.ApiResponse<List<LostItemDto>>> findByPrdtClNm(
            @Parameter(description = "물품 분류명", required = true) @PathVariable String prdtClNm,
            @Parameter(description = "페이지 번호", required = false) @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "페이지 크기", required = false) @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size) {
        Page<LostItemDto> lostItemsPage = lostItemService.findByPrdtClNm(prdtClNm, page, size);
        List<LostItemDto> lostItems = lostItemsPage.getContent();
        
        com.findit.server.dto.ApiResponse<List<LostItemDto>> response = com.findit.server.dto.ApiResponse.success(lostItems);
        response.setPage(page);
        response.setSize(size);
        response.setTotalElements(lostItemsPage.getTotalElements());
        response.setTotalPages(lostItemsPage.getTotalPages());
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/lst-place")
    @Operation(summary = "분실 장소별 분실물 조회", description = "특정 분실 장소의 물품 목록을 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = com.findit.server.dto.ApiResponse.class))),
        @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<com.findit.server.dto.ApiResponse<List<LostItemDto>>> findByLstPlace(
            @Parameter(description = "분실 장소", required = true) @RequestParam String lstPlace,
            @Parameter(description = "페이지 번호 (0부터 시작)", required = false) @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "페이지 크기", required = false) @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size) {
        Page<LostItemDto> lostItemsPage = lostItemService.findByLstPlaceContaining(lstPlace, page, size);
        List<LostItemDto> lostItems = lostItemsPage.getContent();
        
        com.findit.server.dto.ApiResponse<List<LostItemDto>> response = com.findit.server.dto.ApiResponse.success(lostItems);
        response.setPage(page);
        response.setSize(size);
        response.setTotalElements(lostItemsPage.getTotalElements());
        response.setTotalPages(lostItemsPage.getTotalPages());
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/search")
    @Operation(summary = "키워드로 분실물 검색", description = "키워드가 포함된 분실물 목록을 검색합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "검색 성공", content = @Content(schema = @Schema(implementation = com.findit.server.dto.ApiResponse.class))),
        @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<com.findit.server.dto.ApiResponse<List<LostItemDto>>> searchByKeyword(
            @Parameter(description = "검색 키워드", required = true) @RequestParam String keyword,
            @Parameter(description = "페이지 번호 (0부터 시작)", required = false) @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "페이지 크기", required = false) @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size) {
        Page<LostItemDto> lostItemsPage = lostItemService.searchByKeyword(keyword, page, size);
        List<LostItemDto> lostItems = lostItemsPage.getContent();
        
        com.findit.server.dto.ApiResponse<List<LostItemDto>> response = com.findit.server.dto.ApiResponse.success(lostItems);
        response.setPage(page);
        response.setSize(size);
        response.setTotalElements(lostItemsPage.getTotalElements());
        response.setTotalPages(lostItemsPage.getTotalPages());
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/recent")
    @Operation(summary = "최근 분실물 조회", description = "특정 분류명의 최근 분실물 목록을 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = com.findit.server.dto.ApiResponse.class))),
        @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<com.findit.server.dto.ApiResponse<List<LostItemDto>>> findRecentLostItems(
            @Parameter(description = "물품 분류명", required = true) @RequestParam String prdtClNm,
            @Parameter(description = "조회할 일수 (기본값: 7일)", required = false) @RequestParam(defaultValue = "7") @Min(1) @Max(30) int days,
            @Parameter(description = "페이지 번호 (0부터 시작)", required = false) @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "페이지 크기", required = false) @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size) {
        Page<LostItemDto> lostItemsPage = lostItemService.findRecentLostItems(prdtClNm, days, page, size);
        List<LostItemDto> lostItems = lostItemsPage.getContent();
        
        com.findit.server.dto.ApiResponse<List<LostItemDto>> response = com.findit.server.dto.ApiResponse.success(lostItems);
        response.setPage(page);
        response.setSize(size);
        response.setTotalElements(lostItemsPage.getTotalElements());
        response.setTotalPages(lostItemsPage.getTotalPages());
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/lst-ymd-range")
    @Operation(summary = "분실 일자 범위별 분실물 조회", description = "특정 분실 일자 범위 내의 분실물 목록을 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = com.findit.server.dto.ApiResponse.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<com.findit.server.dto.ApiResponse<List<LostItemDto>>> findByLstYmdRange(
            @Parameter(description = "시작 일시 (yyyy-MM-dd'T'HH:mm:ss)", required = true) @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @Parameter(description = "종료 일시 (yyyy-MM-dd'T'HH:mm:ss)", required = true) @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @Parameter(description = "페이지 번호 (0부터 시작)", required = false) @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "페이지 크기", required = false) @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size) {
        if (start.isAfter(end)) {
            throw new InvalidRequestException("시작 일시가 종료 일시보다 이후일 수 없습니다.");
        }
        
        Page<LostItemDto> lostItemsPage = lostItemService.findByLstYmdBetween(start, end, page, size);
        List<LostItemDto> lostItems = lostItemsPage.getContent();
        
        com.findit.server.dto.ApiResponse<List<LostItemDto>> response = com.findit.server.dto.ApiResponse.success(lostItems);
        response.setPage(page);
        response.setSize(size);
        response.setTotalElements(lostItemsPage.getTotalElements());
        response.setTotalPages(lostItemsPage.getTotalPages());
        
        return ResponseEntity.ok(response);
    }
}
