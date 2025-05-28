package com.findit.server.mapper;

import java.util.List;
import java.util.stream.Collectors;

/**
 * uc18cuc2a4 uac1duccb4ub97c ub300uc0c1 uac1duccb4ub85c ub9e4ud551ud558ub294 uc778ud130ud398uc774uc2a4
 * @param <S> uc18cuc2a4 uac1duccb4 ud0c0uc785
 * @param <T> ub300uc0c1 uac1duccb4 ud0c0uc785
 */
public interface DataMapper<S, T> {
    /**
     * ub2e8uc77c uc18cuc2a4 uac1duccb4ub97c ub300uc0c1 uac1duccb4ub85c ub9e4ud551
     * @param source uc18cuc2a4 uac1duccb4
     * @return ub9e4ud551ub41c ub300uc0c1 uac1duccb4
     */
    T map(S source);
    
    /**
     * uc18cuc2a4 uac1duccb4 ub9acuc2a4ud2b8ub97c ub300uc0c1 uac1duccb4 ub9acuc2a4ud2b8ub85c ub9e4ud551
     * @param sources uc18cuc2a4 uac1duccb4 ub9acuc2a4ud2b8
     * @return ub9e4ud551ub41c ub300uc0c1 uac1duccb4 ub9acuc2a4ud2b8
     */
    default List<T> mapList(List<S> sources) {
        if (sources == null) {
            return null;
        }
        return sources.stream().map(this::map).collect(Collectors.toList());
    }
}
