package com.findit.server.application.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * uc5d0ub7ec uc751ub2f5 uc2a4ud0a4ub9c8
 * OpenAPI ubb38uc11cud654ub97c uc704ud55c uc5d0ub7ec uc751ub2f5 ud615uc2dd uc815uc758
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "uc5d0ub7ec uc751ub2f5 uc2a4ud0a4ub9c8")
public class ErrorResponseSchema {
    
    @Schema(description = "uc5d0ub7ec uc0c1ud0dc ucf54ub4dc", example = "400")
    private int status;
    
    @Schema(description = "uc5d0ub7ec uba54uc2dcuc9c0", example = "Bad Request")
    private String message;
    
    @Schema(description = "uc5d0ub7ec ubc1cuc0dd uc2dcuac04")
    private LocalDateTime timestamp;
    
    @Schema(description = "uc5d0ub7ec uc0c1uc138 uc815ubcf4")
    private List<ValidationError> errors;
    
    /**
     * uc720ud6a8uc131 uac80uc99d uc5d0ub7ec ud074ub798uc2a4
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "uc720ud6a8uc131 uac80uc99d uc5d0ub7ec")
    public static class ValidationError {
        
        @Schema(description = "uc5d0ub7ec ubc1cuc0dd ud544ub4dc", example = "name")
        private String field;
        
        @Schema(description = "uc5d0ub7ec uba54uc2dcuc9c0", example = "ud544uc218 ud544ub4dcuc785ub2c8ub2e4.")
        private String message;
        
        @Schema(description = "uac70ubd80ub41c uac12", example = "null")
        private Object rejectedValue;
    }
}
