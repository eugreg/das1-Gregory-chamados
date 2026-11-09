package br.com.sistemas.chamados.enums;

import java.util.Set;

public enum StatusChamado {
    ABERTO,
    EM_ANDAMENTO,
    AGUARDANDO_CLIENTE,
    FECHADO;

    public Set<StatusChamado> proximosPermitidos() {
        return switch (this) {
            case ABERTO -> Set.of(EM_ANDAMENTO, FECHADO);
            case EM_ANDAMENTO -> Set.of(AGUARDANDO_CLIENTE, FECHADO);
            case AGUARDANDO_CLIENTE -> Set.of(EM_ANDAMENTO, FECHADO);
            case FECHADO -> Set.of();
        };
    }

    public boolean podeIrPara(StatusChamado destino) {
        return proximosPermitidos().contains(destino);
    }
}
