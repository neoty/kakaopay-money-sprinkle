package com.melong.kakaopay.exception;

import com.melong.kakaopay.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ApiResponse<Void> businessExceptionHandler(HttpServletResponse httpServletResponse, BusinessException e) {
        httpServletResponse.setStatus(e.getException().getHttpStatus().value());
        return ApiResponse.fail(e.getException().getHttpStatus(), e.getException().getMessage());
    }
}
