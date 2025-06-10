# API Specification

**Base URL:** `http://localhost:8080/api`

---

## 분실물 API (Lost Items)

### 1. 전체 조회
- Method: GET
- Endpoint: `/lost-items`
- Query params:
  - `page` (int, default 0)
  - `size` (int, default 20)
- Response: 200
```json
{
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
  - `atcId` (string)
- Response: 200, 404
```json
{ "data": {LostItemDto} }
```

### 3. 등록 (Create)
- Method: POST
- Endpoint: `/lost-items`
- Body (application/json): LostItemDto
- Response: 201
```json
{ "data": {LostItemDto} }
```

### 4. 수정 (Update)
- Method: PUT
- Endpoint: `/lost-items/{atcId}`
- Path param: `atcId`
- Body: LostItemDto
- Response: 200, 404

### 5. 삭제 (Delete)
- Method: DELETE
- Endpoint: `/lost-items/{atcId}`
- Response: 200, 404

### 6. 분류명별 조회
- Method: GET
- Endpoint: `/lost-items/prdt-cl-nm/{prdtClNm}`
- Path param: `prdtClNm`
- Query: `page`, `size`

### 7. 장소별 조회
- Method: GET
- Endpoint: `/lost-items/lst-place`
- Query params:
  - `lstPlace` (string)
  - `page`, `size`

### 8. 키워드 검색
- Method: GET
- Endpoint: `/lost-items/search`
- Query: `keyword`, `page`, `size`

### 9. 최근 분실물 조회
- Method: GET
- Endpoint: `/lost-items/recent`
- Query: `prdtClNm`

---

## 습득물 API (Found Items)

### 1. 전체 조회
- GET `/found-items` (params: page, size)

### 2. 단건 조회
- GET `/found-items/{atcId}`

### 3. 등록
- POST `/found-items` (body: FoundItemDto)

### 4. 수정
- PUT `/found-items/{atcId}`

### 5. 삭제
- DELETE `/found-items/{atcId}`

### 6. 카테고리별 조회
- GET `/found-items/type/{itemType}` (params: page, size)

### 7. 위치별 조회
- GET `/found-items/location` (query: location, page, size)

### 8. 키워드 검색
- GET `/found-items/search` (query: keyword, page, size)

### 9. 최근 습득물 조회
- GET `/found-items/recent`

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
