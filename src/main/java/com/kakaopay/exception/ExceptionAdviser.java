package com.kakaopay.exception;

import com.kakaopay.constant.Message;
import com.kakaopay.dto.reponse.ApiResponse;
import com.kakaopay.exception.business.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ValidationException;

@RestControllerAdvice
public class ExceptionAdviser {

    /**
     * BusinessException 처리
     *
     * @param httpServletResponse       httpServletResponse
     * @param e                         exception
     * @return                          응답
     */
    @ExceptionHandler(BusinessException.class)
    public ApiResponse<Void> businessExceptionHandler(HttpServletResponse httpServletResponse, BusinessException e) {
        httpServletResponse.setStatus(e.getException().getHttpStatus().value());
        return ApiResponse.fail(e.getException().getHttpStatus(), e.getException().getMessage());
    }

    /**
     * ValidationException 처리
     *
     * @param httpServletResponse   httpServletResponse
     * @param e                     exception
     * @return                      응답
     */
    @ExceptionHandler(value = {ValidationException.class})
    public ApiResponse<Void> validationExceptionHandler(HttpServletResponse httpServletResponse, ValidationException e) {
        httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        return ApiResponse.fail(HttpStatus.BAD_REQUEST, String.format("%s(%s)", Message.INVALID_REQUEST, e.getMessage()));
    }

    /**
     * badRequestException 처리
     * @param httpServletResponse       httpServletResponse
     * @return                          응답
     */
    @ExceptionHandler(value = {HttpMessageNotReadableException.class, MissingRequestHeaderException.class, HttpRequestMethodNotSupportedException.class})
    public ApiResponse<Void> badRequestException(HttpServletResponse httpServletResponse) {
        httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        return ApiResponse.fail(HttpStatus.BAD_REQUEST, Message.INVALID_REQUEST);
    }

    /**
     * 기타 exception
     *
     * @param httpServletResponse   httpServletResponse
     * @param e                     exception
     * @return                      응답
     */
    @ExceptionHandler(java.lang.Exception.class)
    public ApiResponse<Void> defaultExceptionHandler(HttpServletResponse httpServletResponse, java.lang.Exception e) {
        e.printStackTrace();
        httpServletResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ApiResponse.fail(HttpStatus.INTERNAL_SERVER_ERROR, Message.APPLICATION_ERROR);
    }

}
