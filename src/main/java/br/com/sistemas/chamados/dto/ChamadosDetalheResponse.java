package br.com.sistemas.chamados.dto;

import br.com.sistemas.chamados.entity.ChamadosEntity;
import br.com.sistemas.chamados.enums.PrioridadeEnum;
import br.com.sistemas.chamados.enums.StatusChamado;

import java.time.LocalDate;
import java.util.List;

public record ChamadosDetalheResponse(
        Long id,
        String titulo,
        String descricao,
        LocalDate dataAbertura,
        LocalDate dataFechamento,
        PrioridadeEnum prioridade,
        StatusChamado status,
        Long clienteId,
        Long categoriaId,
        Long tecnicoId,
        List<ComentarioResponse> comentarios
) {
    public static ChamadosDetalheResponse de(ChamadosEntity chamado) {
        ChamadosResponse resumo = ChamadosResponse.de(chamado);
        return new ChamadosDetalheResponse(
                resumo.id(),
                resumo.titulo(),
                resumo.descricao(),
                resumo.dataAbertura(),
                resumo.dataFechamento(),
                resumo.prioridade(),
                resumo.status(),
                resumo.clienteId(),
                resumo.categoriaId(),
                resumo.tecnicoId(),
                chamado.getComentarios().stream().map(ComentarioResponse::de).toList()
        );
    }
}
