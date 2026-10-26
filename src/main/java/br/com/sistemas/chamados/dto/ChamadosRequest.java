    package br.com.sistemas.chamados.dto;

    import br.com.sistemas.chamados.enums.PrioridadeEnum;
    import br.com.sistemas.chamados.enums.StatusChamado;

    import java.time.LocalDate;

    public record ChamadosRequest(
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
    }