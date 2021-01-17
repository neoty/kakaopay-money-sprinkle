package com.kakaopay.dto.reponse;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
public class ApiResponse<T> {
    private int status;
    private String message;
    private T data;

    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>defaultBuilder()
                .status(HttpStatus.OK.value())
                .message(message)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> fail(HttpStatus status, String message) {
        return ApiResponse.<T>defaultBuilder()
                .status(status.value())
                .message(message)
                .data(null)
                .build();
    }

    private static <T> ApiResponseBuilder<T> defaultBuilder() {
        return ApiResponse.<T>builder();

    }
}
