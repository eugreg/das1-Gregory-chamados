package br.com.sistemas.chamados.dto;

import br.com.sistemas.chamados.entity.ClienteEntity;

public record ClienteResponse(
        Long id,
        String nome,
        String email,
        String telefone
) {
    public static ClienteResponse de(ClienteEntity cliente){
        return new ClienteResponse(cliente.getId(), cliente.getNome(), cliente.getEmail(), cliente.getTelefone());
    }
}
