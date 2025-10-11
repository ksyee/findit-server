package com.findit.server.presentation.founditem;

import com.findit.server.application.founditem.dto.FoundItemDto;
import com.findit.server.exception.InvalidRequestException;
import com.findit.server.application.common.ApiResponse;
import com.findit.server.application.founditem.FoundItemApplicationService;
import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/found-items")
@RequiredArgsConstructor
@Tag(name = "Found Items", description = "습득물 API")
@Validated
@Timed(value = "api.found_items", description = "Found items API timer")
public class FoundItemController {

    private final FoundItemApplicationService foundItemService;
    
    @GetMapping
    @Operation(summary = "습득물 목록 조회", description = "전체 습득물 목록을 조회합니다.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<ApiResponse<List<FoundItemDto>>> getAllFoundItems(
            @Parameter(description = "페이지 번호", required = false) @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "페이지 크기", required = false) @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size) {
        Page<FoundItemDto> foundItemsPage = foundItemService.getAllFoundItems(page, size);
        List<FoundItemDto> foundItems = foundItemsPage.getContent();
        
        ApiResponse<List<FoundItemDto>> response = ApiResponse.success(foundItems);
        response.setPage(page);
        response.setSize(size);
        response.setTotalElements(foundItemsPage.getTotalElements());
        response.setTotalPages(foundItemsPage.getTotalPages());
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{atcId}")
    @Operation(summary = "습득물 상세 조회", description = "특정 atcId로 습득물 상세 정보를 조회합니다.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "습득물을 찾을 수 없음"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<ApiResponse<FoundItemDto>> getFoundItemByAtcId(
            @Parameter(description = "습득물 관리 ID (ATC_ID)", required = true) @PathVariable String atcId) {
        try {
            FoundItemDto foundItem = foundItemService.getFoundItemByAtcId(atcId);
            return ResponseEntity.ok(ApiResponse.success(foundItem));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PostMapping
    @Operation(summary = "습득물 등록", description = "새로운 습득물을 등록합니다.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "등록 성공", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<ApiResponse<FoundItemDto>> createFoundItem(
            @Parameter(description = "등록할 습득물 정보", required = true) @RequestBody @Valid FoundItemDto foundItemDto) {
        FoundItemDto createdFoundItem = foundItemService.createFoundItem(foundItemDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("등록 성공", createdFoundItem));
    }
    
    @PutMapping("/{atcId}")
    @Operation(summary = "습득물 수정", description = "특정 atcId의 습득물을 수정합니다.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "수정 성공", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "습득물을 찾을 수 없음"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<ApiResponse<FoundItemDto>> updateFoundItem(
            @Parameter(description = "습득물 관리 ID (ATC_ID)", required = true) @PathVariable String atcId,
            @Parameter(description = "수정할 습득물 정보", required = true) @RequestBody @Valid FoundItemDto foundItemDto) {
        try {
            FoundItemDto updatedFoundItem = foundItemService.updateFoundItem(atcId, foundItemDto);
            return ResponseEntity.ok(ApiResponse.success("수정 성공", updatedFoundItem));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @DeleteMapping("/{atcId}")
    @Operation(summary = "습득물 삭제", description = "특정 atcId의 습득물을 삭제합니다.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "삭제 성공", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "습득물을 찾을 수 없음"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<ApiResponse<Void>> deleteFoundItem(
            @Parameter(description = "습득물 관리 ID (ATC_ID)", required = true) @PathVariable String atcId) {
        try {
            foundItemService.deleteFoundItem(atcId);
            return ResponseEntity.ok(ApiResponse.success("삭제 성공", null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @GetMapping("/type/{itemType}")
    @Operation(summary = "카테고리별 습득물 목록 조회", description = "특정 카테고리의 습득물 목록을 조회합니다.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<ApiResponse<List<FoundItemDto>>> findByItemType(
            @Parameter(description = "카테고리", required = true) @PathVariable String itemType,
            @Parameter(description = "페이지 번호", required = false) @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "페이지 크기", required = false) @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size) {
        Page<FoundItemDto> foundItemsPage = foundItemService.findByItemType(itemType, page, size);
        List<FoundItemDto> foundItems = foundItemsPage.getContent();
        
        ApiResponse<List<FoundItemDto>> response = ApiResponse.success(foundItems);
        response.setPage(page);
        response.setSize(size);
        response.setTotalElements(foundItemsPage.getTotalElements());
        response.setTotalPages(foundItemsPage.getTotalPages());
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/location")
    @Operation(summary = "위치별 습득물 목록 조회", description = "특정 위치의 습득물 목록을 조회합니다.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<ApiResponse<List<FoundItemDto>>> findByLocation(
            @Parameter(description = "위치", required = true) @RequestParam String location,
            @Parameter(description = "페이지 번호", required = false) @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "페이지 크기", required = false) @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size) {
        
        Page<FoundItemDto> foundItemsPage = foundItemService.findByLocation(location, page, size);
        List<FoundItemDto> foundItems = foundItemsPage.getContent();
        
        ApiResponse<List<FoundItemDto>> response = ApiResponse.success(foundItems);
        response.setPage(page);
        response.setSize(size);
        response.setTotalElements(foundItemsPage.getTotalElements());
        response.setTotalPages(foundItemsPage.getTotalPages());
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/search")
    @Operation(summary = "키워드 검색", description = "키워드로 습득물을 검색합니다.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<ApiResponse<List<FoundItemDto>>> searchByKeyword(
            @Parameter(description = "키워드", required = true) @RequestParam String keyword,
            @Parameter(description = "페이지 번호", required = false) @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "페이지 크기", required = false) @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size) {
        
        Page<FoundItemDto> foundItemsPage = foundItemService.searchByKeyword(keyword, page, size);
        List<FoundItemDto> foundItems = foundItemsPage.getContent();
        
        ApiResponse<List<FoundItemDto>> response = ApiResponse.success(foundItems);
        response.setPage(page);
        response.setSize(size);
        response.setTotalElements(foundItemsPage.getTotalElements());
        response.setTotalPages(foundItemsPage.getTotalPages());
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/recent")
    @Operation(summary = "최근 습득물 목록 조회", description = "최근 등록된 습득물 목록을 조회합니다.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<ApiResponse<List<FoundItemDto>>> findRecentFoundItems(
            @Parameter(description = "카테고리", required = true) @RequestParam String itemType,
            @Parameter(description = "날짜 범위 (일)", required = false) @RequestParam(defaultValue = "7") @Min(1) @Max(30) int days,
            @Parameter(description = "페이지 번호", required = false) @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "페이지 크기", required = false) @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size) {
        
        Page<FoundItemDto> foundItemsPage = foundItemService.findRecentFoundItems(itemType, days, page, size);
        List<FoundItemDto> foundItems = foundItemsPage.getContent();
        
        ApiResponse<List<FoundItemDto>> response = ApiResponse.success(foundItems);
        response.setPage(page);
        response.setSize(size);
        response.setTotalElements(foundItemsPage.getTotalElements());
        response.setTotalPages(foundItemsPage.getTotalPages());
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/date-range")
    @Operation(summary = "날짜 범위별 습득물 목록 조회", description = "특정 날짜 범위의 습득물 목록을 조회합니다.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<ApiResponse<List<FoundItemDto>>> findByDateRange(
            @Parameter(description = "시작 날짜", required = true) @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @Parameter(description = "종료 날짜", required = true) @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @Parameter(description = "페이지 번호", required = false) @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "페이지 크기", required = false) @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size) {
        
        if (start.isAfter(end)) {
            throw new InvalidRequestException("시작 날짜가 종료 날짜보다 이후일 수 없습니다.");
        }
        
        Page<FoundItemDto> foundItemsPage = foundItemService.findByDateRange(start, end, page, size);
        List<FoundItemDto> foundItems = foundItemsPage.getContent();
        
        ApiResponse<List<FoundItemDto>> response = ApiResponse.success(foundItems);
        response.setPage(page);
        response.setSize(size);
        response.setTotalElements(foundItemsPage.getTotalElements());
        response.setTotalPages(foundItemsPage.getTotalPages());
        
        return ResponseEntity.ok(response);
    }
}
