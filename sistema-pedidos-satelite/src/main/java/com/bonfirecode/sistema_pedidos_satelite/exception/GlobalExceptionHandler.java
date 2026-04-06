package com.bonfirecode.sistema_pedidos_satelite.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    // DTO interno para formatar a resposta visualmente igual ao padrão do Spring, mas sem o "stacktrace" assustador
    public record ErroRespostaDTO(LocalDateTime timestamp, Integer status, String erro, String caminho) {}

    // Intercepta a nossa Trava Financeira (IllegalStateException)
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErroRespostaDTO> handleIllegalStateException(IllegalStateException ex, HttpServletRequest request) {
        ErroRespostaDTO erro = new ErroRespostaDTO(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(), // Status 400: O usuário fez algo que a regra não permite
                ex.getMessage(), // Aqui vai aparecer: "Ação bloqueada! Este pedido já foi enviado..."
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    // Intercepta Buscas que não encontram nada (IllegalArgumentException)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErroRespostaDTO> handleIllegalArgumentException(IllegalArgumentException ex, HttpServletRequest request) {
        ErroRespostaDTO erro = new ErroRespostaDTO(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(), // Status 404: Não encontrado
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }
}