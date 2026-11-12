package br.com.sistemas.chamados.web;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.support.RequestContextUtils;

import java.util.stream.Collectors;

@ControllerAdvice(basePackageClasses = WebExceptionHandler.class)
@Order(Ordered.HIGHEST_PRECEDENCE)
public class WebExceptionHandler {

    @ExceptionHandler(BindException.class)
    public String validacao(BindException ex, HttpServletRequest request) {
        String mensagem = ex.getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));
        return voltar(request, mensagem);
    }

    @ExceptionHandler(RuntimeException.class)
    public String erro(RuntimeException ex, HttpServletRequest request) {
        return voltar(request, ex.getMessage());
    }

    private String voltar(HttpServletRequest request, String mensagem) {
        RequestContextUtils.getOutputFlashMap(request).put("erro", mensagem);
        String origem = request.getHeader("Referer");
        return "redirect:" + (origem != null ? origem : "/web");
    }
}
