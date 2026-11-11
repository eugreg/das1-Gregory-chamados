package br.com.sistemas.chamados.exception;

import br.com.sistemas.chamados.enums.StatusChamado;

public class TransicaoStatusInvalidaException extends RuntimeException {
    public TransicaoStatusInvalidaException(StatusChamado atual, StatusChamado destino) {
        super("Transição de status inválida: " + atual + " -> " + destino);
    }
}
