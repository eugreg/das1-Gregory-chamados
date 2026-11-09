package br.com.sistemas.chamados.dto;

import br.com.sistemas.chamados.enums.PrioridadeEnum;
import br.com.sistemas.chamados.enums.StatusChamado;

public record ChamadosFiltro(
        StatusChamado status,
        PrioridadeEnum prioridade,
        Long clienteId,
        Long tecnicoId,
        Long categoriaId
) {
    public static ChamadosFiltro doCliente(Long clienteId) {
        return new ChamadosFiltro(null, null, clienteId, null, null);
    }
}
