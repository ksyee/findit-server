# FindIt 도메인 유비쿼터스 언어

| 용어 | 설명 | 값 객체 / 애그리게이트 |
| --- | --- | --- |
| `LostItem` | 분실 신고가 들어온 물품. 식별자, 분류, 분실 위치, 분실일자, 비고를 갖는 애그리게이트 루트 | `LostItem` / `LostItemId`, `LostDate`, `ItemCategory`, `LocationName` |
| `FoundItem` | 습득 신고가 들어온 물품. 식별자, 물품명, 분류, 습득 위치, 습득일자 등을 보유 | `FoundItem` / `FoundItemId`, `FoundDate`, `ItemName`, `ItemCategory`, `LocationName` |
| `ItemCategory` | 경찰청이 제공하는 물품 분류 체계. 최대 100자의 문자열로 식별 | 값 객체 `ItemCategory` |
| `LocationName` | 분실/습득이 발생한 장소. 공백 제거 후 255자 제한을 둔 값 객체 | 값 객체 `LocationName` |
| `LostDate` / `FoundDate` | 분실/습득이 발생한 날짜. 다양한 문자열 입력을 허용해 `LocalDate`로 표준화 | 값 객체 `LostDate`, `FoundDate` |
| `ItemName` | 습득물의 명칭. 공백을 허용하지 않는 255자 이하 문자열 | 값 객체 `ItemName` |
| `RNUM` | 경찰청 데이터에서 내려오는 행 번호. 선택 값으로 도메인 모델에서는 존재 확인 용도로만 사용 | `LostItem` 필드 `rnum` |
| `FD_SN` | 습득물 일련번호. `FoundItem`의 보조 식별자로 활용 | `FoundItem` 필드 `fdSn` |

## 도메인 규칙 요약

- 모든 식별자는 공백을 허용하지 않고, 영구적으로 엔티티를 구분한다.
- 분실/습득일자는 `yyyyMMdd` 또는 `yyyy-MM-dd` 형식 중 하나로 수집되며, 내부에서는 `LocalDate`로 보관하고 기본 ISO 포맷으로 저장한다.
- 장소/물품명과 같은 텍스트 속성은 생성 시 공백 트리밍 및 길이 검증을 통과해야 한다.
- 애그리게이트는 `create`/`updateCoreDetails` 등의 도메인 메서드를 통해서만 상태가 변경된다.
