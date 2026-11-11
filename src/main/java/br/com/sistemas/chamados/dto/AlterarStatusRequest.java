package br.com.sistemas.chamados.dto;

import br.com.sistemas.chamados.enums.StatusChamado;
import jakarta.validation.constraints.NotNull;

public record AlterarStatusRequest(
        @NotNull StatusChamado status
) {
}
