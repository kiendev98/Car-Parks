package com.wego.interview.carpark.inbound.web;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.ZonedDateTime;

@RestControllerAdvice
@Slf4j
public class GlobalControllerExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidInputException.class)
    public HttpErrorInfo handleInvalidInputException(HttpServletRequest request, InvalidInputException exception) {
        String path = request.getRequestURI();
        String message = exception.getMessage();
        log.debug("Returning HTTP status: {} for path: {}, message: {}", HttpStatus.BAD_REQUEST, path, message);
        return new HttpErrorInfo(
                path,
                HttpStatus.BAD_REQUEST,
                message
        );
    }

    @RequiredArgsConstructor
    @Data
    public static class HttpErrorInfo {
        private final ZonedDateTime timestamp = ZonedDateTime.now();
        private final String path;
        private final HttpStatus httpStatus;
        private final String message;
    }
}
