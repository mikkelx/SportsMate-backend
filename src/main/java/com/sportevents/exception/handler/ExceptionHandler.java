package com.sportevents.exception.handler;

import com.sportevents.exception.EventCreateException;
import com.sportevents.exception.NotFoundException;
import com.sportevents.exception.RegisterException;
import com.sportevents.exception.UnauthorizedException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler {

    //runtime ex

    @org.springframework.web.bind.annotation.ExceptionHandler(value = { NotFoundException.class})
    protected ResponseEntity<Object> handleDatabaseGet(NotFoundException exception, WebRequest request) {
        String bodyOfResponse = "Object not found, " + exception.getMessage();
        logger.error(bodyOfResponse);
        return handleExceptionInternal(exception, bodyOfResponse, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = { RegisterException.class})
    protected ResponseEntity<Object> handleDatabaseGet(RegisterException exception, WebRequest request) {
        logger.error(exception.getMessage());
        return handleExceptionInternal(exception, exception.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = { EventCreateException.class})
    protected ResponseEntity<Object> handleDatabaseGet(EventCreateException exception, WebRequest request) {
        logger.error(exception.getMessage());
        return handleExceptionInternal(exception, exception.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = { UnauthorizedException.class})
    protected ResponseEntity<Object> handleDatabaseGet(UnauthorizedException exception, WebRequest request) {
        logger.error(exception.getMessage());
        return handleExceptionInternal(exception, exception.getMessage(), new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
    }



//    @org.springframework.web.bind.annotation.ExceptionHandler(value = { RuntimeException.class})
//    protected ResponseEntity<Object> handleDatabaseGet(RuntimeException exception, WebRequest request) {
//        logger.error(exception.getMessage());
//        return handleExceptionInternal(exception, exception.getMessage(), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
//    }

}
