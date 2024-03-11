package com.recommtoon.recommtoonapi.exception.handler;

import com.recommtoon.recommtoonapi.exception.InvalidRefreshTokenException;
import com.recommtoon.recommtoonapi.exception.NotFoundException;
import com.recommtoon.recommtoonapi.exception.UnAuthorizedException;
import com.recommtoon.recommtoonapi.util.ApiUtil;
import com.recommtoon.recommtoonapi.util.ApiUtil.ApiError;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
                                                             HttpStatusCode statusCode, WebRequest request) {
        log.error(ex.getMessage(), ex);
        ApiError<String> error = ApiUtil.error(statusCode.value(), "알 수 없는 오류가 발생했습니다. 문의 부탁드립니다.");
        return super.handleExceptionInternal(ex, error, headers, statusCode, request);
    }

    @ExceptionHandler({
            IllegalStateException.class,
            UnAuthorizedException.class,
            InvalidRefreshTokenException.class
    })
    protected ResponseEntity<?> handleIllegalStateException(Exception e) {
        log.error(e.getMessage(), e);
        ApiError<String> error = ApiUtil.error(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        return ResponseEntity.status(HttpServletResponse.SC_BAD_REQUEST).body(error);
    }

    @ExceptionHandler({
            IllegalArgumentException.class,
            NotFoundException.class
    })
    protected ResponseEntity<?> handleIllegalArgumentException(Exception e) {
        log.error(e.getMessage(), e);
        ApiError<String> error = ApiUtil.error(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        return ResponseEntity.status(HttpServletResponse.SC_NOT_FOUND).body(error);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<?> handleNormalException(Exception e) {
        log.error(e.getMessage(), e);
        ApiError<String> error = ApiUtil.error(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "알 수 없는 오류가 발생했습니다. 문의 부탁드립니다.");
        return ResponseEntity.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR).body(error);
    }

}
