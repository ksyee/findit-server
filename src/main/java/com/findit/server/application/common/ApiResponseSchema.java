package com.findit.server.application.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * API uc751ub2f5 uc2a4ud0a4ub9c8
 * OpenAPI ubb38uc11cud654ub97c uc704ud55c uc751ub2f5 ud615uc2dd uc815uc758
 * 
 * @param <T> uc751ub2f5 ub370uc774ud130 ud0c0uc785
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "API uc751ub2f5 uc2a4ud0a4ub9c8")
public class ApiResponseSchema<T> {
    
    @Schema(description = "uc751ub2f5 ub370uc774ud130")
    private List<T> data;
    
    @Schema(description = "uc751ub2f5 uc0c1ud0dc ucf54ub4dc", example = "200")
    private int status;
    
    @Schema(description = "uc751ub2f5 uba54uc2dcuc9c0", example = "Success")
    private String message;
    
    @Schema(description = "ud398uc774uc9c0 uc815ubcf4")
    private PageInfo pageInfo;
    
    /**
     * ud398uc774uc9c0 uc815ubcf4 ud074ub798uc2a4
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "ud398uc774uc9c0 uc815ubcf4")
    public static class PageInfo {
        
        @Schema(description = "ud604uc7ac ud398uc774uc9c0 ubc88ud638", example = "0")
        private int page;
        
        @Schema(description = "ud398uc774uc9c0 ud06cuae30", example = "20")
        private int size;
        
        @Schema(description = "uc804uccb4 uc694uc18c uc218", example = "100")
        private long totalElements;
        
        @Schema(description = "uc804uccb4 ud398uc774uc9c0 uc218", example = "5")
        private int totalPages;
    }
}
