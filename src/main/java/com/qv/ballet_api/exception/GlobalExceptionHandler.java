package com.qv.ballet_api.exception;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import org.springframework.transaction.TransactionSystemException;
import jakarta.validation.ConstraintViolationException;

import java.util.HashMap;
import java.util.Map;

@Hidden
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<Map<String, Object>> manejarAccesoDenegado(AuthorizationDeniedException ex) {
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("codigo", 403);
        respuesta.put("error", "Acceso Denegado");
        respuesta.put("mensaje", "Tu rol no tiene permisos de Administrador para realizar esta acción.");

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(respuesta);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> manejarValidaciones(MethodArgumentNotValidException ex) {

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("codigo", 400);
        respuesta.put("error", "Bad Request");
        respuesta.put("mensaje", "La petición tiene errores de validación. Revisa los datos enviados.");

        Map<String, String> detallesCampos = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                detallesCampos.put(error.getField(), error.getDefaultMessage())
        );

        respuesta.put("detalles", detallesCampos);

        return ResponseEntity.badRequest().body(respuesta);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> manejarErroresGenerales(RuntimeException ex) {

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("codigo", 400);
        respuesta.put("error", "Petición Inválida");
        respuesta.put("mensaje", ex.getMessage());

        return ResponseEntity.badRequest().body(respuesta);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> manejarErrorDeFormato(HttpMessageNotReadableException ex) {

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("codigo", 400);
        respuesta.put("error", "Error de Formato");
        respuesta.put("mensaje", "Los datos enviados no tienen el formato correcto. Verifica las fechas (deben ser AAAA-MM-DD) y los tipos de datos.");

        return ResponseEntity.badRequest().body(respuesta);
    }


    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<Map<String, Object>> manejarErroresTransaccionJPA(TransactionSystemException ex) {

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("codigo", 400);
        respuesta.put("error", "Validación de Base de Datos");

        Throwable causa = ex.getRootCause();

        if (causa instanceof ConstraintViolationException constraintEx) {
            respuesta.put("mensaje", "Los datos enviados para actualizar no cumplen con las reglas requeridas.");

            Map<String, String> detallesCampos = new HashMap<>();
            constraintEx.getConstraintViolations().forEach(violacion ->
                    detallesCampos.put(violacion.getPropertyPath().toString(), violacion.getMessage())
            );

            respuesta.put("detalles", detallesCampos);
            return ResponseEntity.badRequest().body(respuesta);
        }

        respuesta.put("mensaje", "Error interno al procesar los datos en la base de datos.");
        return ResponseEntity.badRequest().body(respuesta);
    }
}