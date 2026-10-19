package br.com.sistemas.chamados.dto;

import br.com.sistemas.chamados.entity.TecnicoEntity;
import br.com.sistemas.chamados.enums.PerfilEnum;

public record TecnicoResponse(
        Long id,
        String nome,
        String email,
        PerfilEnum perfil
) {
    public static TecnicoResponse de(TecnicoEntity tecnico){
        return new TecnicoResponse(tecnico.getId(), tecnico.getNome(), tecnico.getEmail(), tecnico.getPerfil());

    }
}
