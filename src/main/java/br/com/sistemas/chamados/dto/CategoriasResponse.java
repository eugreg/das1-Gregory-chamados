package br.com.sistemas.chamados.dto;

import br.com.sistemas.chamados.entity.CategoriasEntity;

public record CategoriasResponse(
        Long id,
        String nome,
        String descricao
) {
    public static CategoriasResponse de(CategoriasEntity categoria) {
        return new CategoriasResponse(categoria.getId(), categoria.getNome(), categoria.getDescricao());
    }
}
