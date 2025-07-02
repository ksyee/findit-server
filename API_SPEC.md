# API Specification

**Base URL:** `http://localhost:8080/api`

---

## 분실물 API (Lost Items)

### 1. 전체 조회
- Method: GET
- Endpoint: `/lost-items`
- Query params:
  - `page` (int, default 0): 페이지 번호
  - `size` (int, default 20, max 100): 페이지 크기
- Response: 200
```json
{
  "status": "success",
  "message": null,
  "data": [ {LostItemDto}, ... ],
  "page": 0,
  "size": 20,
  "totalElements": 100,
  "totalPages": 5
}
```

### 2. 단건 조회
- Method: GET
- Endpoint: `/lost-items/{atcId}`
- Path param:
  - `atcId` (string): 분실물 관리 ID
- Response: 200, 404
```json
{ 
  "status": "success",
  "message": null,
  "data": {LostItemDto} 
}
```

### 3. 등록 (Create)
- Method: POST
- Endpoint: `/lost-items`
- Body (application/json): LostItemDto
- Response: 201
```json
{ 
  "status": "success",
  "message": "Lost item created successfully",
  "data": {LostItemDto} 
}
```

### 4. 수정 (Update)
- Method: PUT
- Endpoint: `/lost-items/{atcId}`
- Path param: `atcId` (string): 분실물 관리 ID
- Body: LostItemDto
- Response: 200, 404
```json
{ 
  "status": "success",
  "message": "Lost item updated successfully",
  "data": {LostItemDto} 
}
```

### 5. 삭제 (Delete)
- Method: DELETE
- Endpoint: `/lost-items/{atcId}`
- Path param: `atcId` (string): 분실물 관리 ID
- Response: 200, 404
```json
{ 
  "status": "success",
  "message": "Lost item deleted successfully",
  "data": null
}
```

### 6. 분류명별 조회
- Method: GET
- Endpoint: `/lost-items/prdt-cl-nm/{prdtClNm}`
- Path param: `prdtClNm` (string): 물품 분류명
- Query params:
  - `page` (int, default 0)
  - `size` (int, default 20, max 100)
- Response: 200
```json
{
  "status": "success",
  "message": null,
  "data": [ {LostItemDto}, ... ],
  "page": 0,
  "size": 20,
  "totalElements": 100,
  "totalPages": 5
}
```

### 7. 장소별 조회
- Method: GET
- Endpoint: `/lost-items/lst-place`
- Query params:
  - `lstPlace` (string): 분실 장소
  - `page` (int, default 0)
  - `size` (int, default 20, max 100)
- Response: 200
```json
{
  "status": "success",
  "message": null,
  "data": [ {LostItemDto}, ... ],
  "page": 0,
  "size": 20,
  "totalElements": 100,
  "totalPages": 5
}
```

### 8. 키워드 검색
- Method: GET
- Endpoint: `/lost-items/search`
- Query params:
  - `keyword` (string): 검색 키워드
  - `page` (int, default 0)
  - `size` (int, default 20, max 100)
- Response: 200
```json
{
  "status": "success",
  "message": null,
  "data": [ {LostItemDto}, ... ],
  "page": 0,
  "size": 20,
  "totalElements": 100,
  "totalPages": 5
}
```

### 9. 최근 분실물 조회
- Method: GET
- Endpoint: `/lost-items/recent`
- Query params: 
  - `prdtClNm` (string): 물품 분류명
  - `days` (int, default 7, max 30): 조회할 일수 
  - `page` (int, default 0)
  - `size` (int, default 20, max 100)
- Response: 200
```json
{
  "status": "success",
  "message": null,
  "data": [ {LostItemDto}, ... ],
  "page": 0,
  "size": 20,
  "totalElements": 100,
  "totalPages": 5
}
```

### 10. 분실 일자 범위 조회
- Method: GET
- Endpoint: `/lost-items/lst-ymd-range`
- Query params:
  - `start` (LocalDateTime, ISO 형식): 시작 일시 (yyyy-MM-dd'T'HH:mm:ss)
  - `end` (LocalDateTime, ISO 형식): 종료 일시 (yyyy-MM-dd'T'HH:mm:ss)
  - `page` (int, default 0)
  - `size` (int, default 20, max 100)
- Response: 200, 400
```json
{
  "status": "success",
  "message": null,
  "data": [ {LostItemDto}, ... ],
  "page": 0,
  "size": 20,
  "totalElements": 100,
  "totalPages": 5
}
```

---

## 습득물 API (Found Items)

### 1. 전체 조회
- Method: GET
- Endpoint: `/found-items`
- Query params: 
  - `page` (int, default 0)
  - `size` (int, default 20, max 100)
- Response: 200
```json
{
  "status": "success",
  "message": null,
  "data": [ {FoundItemDto}, ... ],
  "page": 0,
  "size": 20,
  "totalElements": 100,
  "totalPages": 5
}
```

### 2. 단건 조회
- Method: GET
- Endpoint: `/found-items/{atcId}`
- Path param: 
  - `atcId` (string): 습득물 관리 ID
- Response: 200, 404
```json
{
  "status": "success",
  "message": null,
  "data": {FoundItemDto}
}
```

### 3. 등록
- Method: POST
- Endpoint: `/found-items`
- Body (application/json): FoundItemDto
- Response: 201
```json
{
  "status": "success",
  "message": "등록 성공",
  "data": {FoundItemDto}
}
```

### 4. 수정
- Method: PUT
- Endpoint: `/found-items/{atcId}`
- Path param: 
  - `atcId` (string): 습득물 관리 ID
- Body: FoundItemDto
- Response: 200, 404
```json
{
  "status": "success",
  "message": "수정 성공",
  "data": {FoundItemDto}
}
```

### 5. 삭제
- Method: DELETE
- Endpoint: `/found-items/{atcId}`
- Path param: 
  - `atcId` (string): 습득물 관리 ID
- Response: 200, 404
```json
{
  "status": "success",
  "message": "삭제 성공",
  "data": null
}
```

### 6. 카테고리별 조회
- Method: GET
- Endpoint: `/found-items/type/{itemType}`
- Path param: 
  - `itemType` (string): 카테고리
- Query params: 
  - `page` (int, default 0)
  - `size` (int, default 20, max 100)
- Response: 200
```json
{
  "status": "success",
  "message": null,
  "data": [ {FoundItemDto}, ... ],
  "page": 0,
  "size": 20,
  "totalElements": 100,
  "totalPages": 5
}
```

### 7. 위치별 조회
- Method: GET
- Endpoint: `/found-items/location`
- Query params:
  - `location` (string): 위치
  - `page` (int, default 0)
  - `size` (int, default 20, max 100)
- Response: 200
```json
{
  "status": "success",
  "message": null,
  "data": [ {FoundItemDto}, ... ],
  "page": 0,
  "size": 20,
  "totalElements": 100,
  "totalPages": 5
}
```

### 8. 키워드 검색
- Method: GET
- Endpoint: `/found-items/search`
- Query params:
  - `keyword` (string): 검색 키워드
  - `page` (int, default 0)
  - `size` (int, default 20, max 100)
- Response: 200
```json
{
  "status": "success",
  "message": null,
  "data": [ {FoundItemDto}, ... ],
  "page": 0,
  "size": 20,
  "totalElements": 100,
  "totalPages": 5
}
```

### 9. 최근 습득물 조회
- Method: GET
- Endpoint: `/found-items/recent`
- Query params:
  - `itemType` (string): 카테고리
  - `days` (int, default 7, max 30): 날짜 범위 (일)
  - `page` (int, default 0)
  - `size` (int, default 20, max 100)
- Response: 200
```json
{
  "status": "success",
  "message": null,
  "data": [ {FoundItemDto}, ... ],
  "page": 0,
  "size": 20,
  "totalElements": 100,
  "totalPages": 5
}
```

### 10. 날짜 범위별 조회
- Method: GET
- Endpoint: `/found-items/date-range`
- Query params:
  - `start` (LocalDateTime, ISO 형식): 시작 날짜 (yyyy-MM-dd'T'HH:mm:ss)
  - `end` (LocalDateTime, ISO 형식): 종료 날짜 (yyyy-MM-dd'T'HH:mm:ss)
  - `page` (int, default 0)
  - `size` (int, default 20, max 100)
- Response: 200, 400
```json
{
  "status": "success",
  "message": null,
  "data": [ {FoundItemDto}, ... ],
  "page": 0,
  "size": 20,
  "totalElements": 100,
  "totalPages": 5
}
```

---

## DTO 예시

### LostItemDto
```json
{
  "atcId": "20230615-0001",
  "prdtClNm": "지갑",
  "lstPlace": "서울역",
  "lstPrdtNm": "검정 지갑",
  "lstSbjt": "안에 신분증·현금",
  "lstYmd": "2023-06-15",
  "rnum": 1
}
```

### FoundItemDto
```json
{
  "atcId": "20230610-0003",
  "itemType": "휴대폰",
  "location": "강남역",
  "description": "검정색 스마트폰",
  "foundDate": "2023-06-10",
  "rnum": 3
}
```

## 공통 응답 형식

### ApiResponse
```json
{
  "status": "success" | "error",
  "message": "응답 메시지",
  "data": "응답 데이터 (Object 또는 List)",
  "page": 0,
  "size": 20,
  "totalElements": 100,
  "totalPages": 5
}
