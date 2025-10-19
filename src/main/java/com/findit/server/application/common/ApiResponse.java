package com.findit.server.application.common;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

import java.util.List;

@Schema(description = "API Response Format")
public class ApiResponse<T> {
    @Schema(description = "Success status", example = "true")
    private boolean success;
    
    @Schema(description = "Response message", example = "Success")
    private String message;
    
    @Schema(description = "Response data")
    private T data;
    
    @Schema(description = "Current page number", example = "0")
    private int page;
    
    @Schema(description = "Page size", example = "20")
    private int size;
    
    @Schema(description = "Total number of elements", example = "100")
    private long totalElements;
    
    @Schema(description = "Total number of pages", example = "5")
    private int totalPages;
    
    // Default constructor
    public ApiResponse() {
    }
    
    // Constructor with all fields except pagination
    public ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }
    
    // Constructor with pagination information
    public ApiResponse(boolean success, String message, T data, int page, int size, long totalElements, int totalPages) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
    }
    
    // Getters
    public boolean isSuccess() {
        return success;
    }
    
    public String getMessage() {
        return message;
    }
    
    public T getData() {
        return data;
    }
    
    public int getPage() {
        return page;
    }
    
    public int getSize() {
        return size;
    }
    
    public long getTotalElements() {
        return totalElements;
    }
    
    public int getTotalPages() {
        return totalPages;
    }
    
    // Setters
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public void setData(T data) {
        this.data = data;
    }
    
    public void setPage(int page) {
        this.page = page;
    }
    
    public void setSize(int size) {
        this.size = size;
    }
    
    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }
    
    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
    
    // Builder pattern implementation
    public static <T> Builder<T> builder() {
        return new Builder<>();
    }
    
    public static class Builder<T> {
        private boolean success;
        private String message;
        private T data;
        private int page;
        private int size;
        private long totalElements;
        private int totalPages;
        
        public Builder<T> success(boolean success) {
            this.success = success;
            return this;
        }
        
        public Builder<T> message(String message) {
            this.message = message;
            return this;
        }
        
        public Builder<T> data(T data) {
            this.data = data;
            return this;
        }
        
        public Builder<T> page(int page) {
            this.page = page;
            return this;
        }
        
        public Builder<T> size(int size) {
            this.size = size;
            return this;
        }
        
        public Builder<T> totalElements(long totalElements) {
            this.totalElements = totalElements;
            return this;
        }
        
        public Builder<T> totalPages(int totalPages) {
            this.totalPages = totalPages;
            return this;
        }
        
        public ApiResponse<T> build() {
            ApiResponse<T> response = new ApiResponse<>();
            response.success = this.success;
            response.message = this.message;
            response.data = this.data;
            response.page = this.page;
            response.size = this.size;
            response.totalElements = this.totalElements;
            response.totalPages = this.totalPages;
            return response;
        }
    }
    
    // Success response methods
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message("Success")
                .data(data)
                .build();
    }
    
    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<List<T>> page(Page<T> page) {
        ApiResponse<List<T>> response = success(page.getContent());
        return response.withPageMetadata(page);
    }

    public ApiResponse<T> withPageMetadata(Page<?> page) {
        this.page = page.getNumber();
        this.size = page.getSize();
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
        return this;
    }

    // Error response methods
    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .build();
    }
    
    public static <T> ApiResponse<T> error(String message, T data) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .data(data)
                .build();
    }
}
