package br.com.sistemas.chamados.dto;

import br.com.sistemas.chamados.enums.PrioridadeEnum;
import br.com.sistemas.chamados.enums.StatusChamado;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record ChamadosRequest(
        @NotBlank(message = "Título é obrigatório")
        @Size(max = 255, message = "Título deve ter no máximo 255 caracteres")
        String titulo,

        @NotBlank(message = "Descrição é obrigatória")
        String descricao,

        LocalDate dataAbertura,

        LocalDate dataFechamento,

        @NotNull(message = "Prioridade é obrigatória")
        PrioridadeEnum prioridade,

        StatusChamado status,

        @NotNull(message = "Cliente é obrigatório")
        Long clienteId,

        @NotNull(message = "Categoria é obrigatória")
        Long categoriaId,

        @NotNull(message = "Técnico é obrigatório")
        Long tecnicoId
) {
}
