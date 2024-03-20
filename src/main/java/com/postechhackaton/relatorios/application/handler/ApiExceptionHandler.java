package com.postechhackaton.relatorios.application.handler;

import com.postechhackaton.relatorios.application.dto.ExceptionResponse;
import com.postechhackaton.relatorios.business.exceptions.NegocioException;
import com.postechhackaton.relatorios.business.exceptions.NotFoundException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ApiExceptionHandler {

    @ExceptionHandler(NegocioException.class)
    public final ResponseEntity<ExceptionResponse> handleTo(NegocioException e) {
        return new ResponseEntity<>(new ExceptionResponse(ExceptionResponse.ErrorType.PROCESS_FAILURE,
                e.getMessage()), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(NotFoundException.class)
    public final ResponseEntity<ExceptionResponse> handleTo(NotFoundException e) {
        return new ResponseEntity<>(new ExceptionResponse(ExceptionResponse.ErrorType.RESOURCE_NOT_FOUND,
                e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public final ResponseEntity<ExceptionResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return new ResponseEntity<>(new ExceptionResponse(ExceptionResponse.ErrorType.VALIDATION_FAILURE,
                e.getMessage()), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ExceptionResponse> handleGenericException(Exception e) {
        return new ResponseEntity<>(new ExceptionResponse(ExceptionResponse.ErrorType.GENERIC_SERVER_ERROR,
                "Erro inesperado encontrado no servidor durante o processamento da solicitação"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
