package com.findit.server.mapper;

import java.util.List;
import java.util.stream.Collectors;

/**
 * API 응답 객체를 내부 도메인 엔티티로 매핑하기 위한 제네릭 인터페이스
 * @param <S> 소스 API DTO 타입
 * @param <T> 타겟 엔티티 타입
 */
public interface ApiMapper<S, T> {
    /**
     * 소스 객체를 타겟 객체로 매핑합니다.
     * @param source 매핑할 소스 객체
     * @return 매핑된 타겟 객체
     */
    T map(S source);

    /**
     * 소스 객체 리스트를 타겟 객체 리스트로 매핑합니다.
     * @param sourceList 매핑할 소스 객체 리스트
     * @return 매핑된 타겟 객체 리스트, 소스 리스트가 null이거나 비어있으면 빈 리스트 반환
     */
    default List<T> mapList(List<S> sourceList) {
        if (sourceList == null || sourceList.isEmpty()) {
            return List.of(); // Java 9+ or Collections.emptyList(); for older versions
        }
        return sourceList.stream()
                .map(this::map) // 각 요소에 대해 map 메서드 호출
                .collect(Collectors.toList());
    }
}
