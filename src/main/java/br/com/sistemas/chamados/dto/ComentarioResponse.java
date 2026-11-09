package br.com.sistemas.chamados.dto;

import br.com.sistemas.chamados.entity.ComentarioEntity;

import java.time.LocalDateTime;

public record ComentarioResponse(
        Long id,
        String texto,
        String autor,
        LocalDateTime dataCriacao
) {
    public static ComentarioResponse de(ComentarioEntity comentario) {
        return new ComentarioResponse(
                comentario.getId(),
                comentario.getTexto(),
                comentario.getAutor(),
                comentario.getDataCriacao()
        );
    }
}
