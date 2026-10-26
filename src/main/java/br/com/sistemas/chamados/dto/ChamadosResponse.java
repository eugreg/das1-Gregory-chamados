package br.com.sistemas.chamados.dto;

import br.com.sistemas.chamados.entity.ChamadosEntity;
import br.com.sistemas.chamados.enums.PrioridadeEnum;
import br.com.sistemas.chamados.enums.StatusChamado;

import java.time.LocalDate;

public record ChamadosResponse(
        Long id,
        String titulo,
        String descricao,
        LocalDate dataAbertura,
        LocalDate dataFechamento,
        PrioridadeEnum prioridade,
        StatusChamado status,
        Long clienteId,
        Long categoriaId,
        Long tecnicoId
) {
    public static ChamadosResponse de(ChamadosEntity chamado) {
        return new ChamadosResponse(
                chamado.getId(),
                chamado.getTitulo(),
                chamado.getDescricao(),
                chamado.getDataAbertura(),
                chamado.getDataFechamento(),
                chamado.getPrioridade(),
                chamado.getStatus(),
                chamado.getCliente() != null ? chamado.getCliente().getId() : null,
                chamado.getCateforia() != null ? chamado.getCateforia().getId() : null,
                chamado.getTecnico() != null ? chamado.getTecnico().getId() : null
        );
    }
}