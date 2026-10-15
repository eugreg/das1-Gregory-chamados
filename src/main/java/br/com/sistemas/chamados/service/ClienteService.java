package br.com.sistemas.chamados.service;

import br.com.sistemas.chamados.dto.ClienteRequest;
import br.com.sistemas.chamados.dto.ClienteResponse;
import br.com.sistemas.chamados.entity.ClienteEntity;
import br.com.sistemas.chamados.exception.RecursoNaoEncontradoException;
import br.com.sistemas.chamados.exception.RegraNegocioException;
import br.com.sistemas.chamados.repository.ClienteRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ClienteService {


    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public ClienteResponse criar(ClienteRequest clienteRequest) {
        if (clienteRepository.existsByEmail(clienteRequest.email()))
            throw new RegraNegocioException("Ja existe cliente com e-mail " + clienteRequest.email());

        ClienteEntity clienteEntity = new ClienteEntity(clienteRequest.nome(), clienteRequest.email(), clienteRequest.telefone());
        ClienteEntity salvo = clienteRepository.save(clienteEntity);

        return ClienteResponse.de(salvo);
    }

    public List<ClienteResponse> listar() {
        return clienteRepository.findAll().stream().map(ClienteResponse::de).toList();
    }

    public ClienteResponse buscar(Long id) {
        return ClienteResponse.de(buscarEntity(id));
    }

    public ClienteResponse atualizar(Long id, ClienteRequest clienteRequest) {
        ClienteEntity cliente = buscarEntity(id);
        if (clienteRepository.existsByEmail(clienteRequest.email())) {
            throw new RegraNegocioException("Ja existe com e-mail " + clienteRequest.email());
        }

        cliente.setEmail(clienteRequest.email());
        cliente.setNome(clienteRequest.nome());
        cliente.setTelefone(clienteRequest.telefone());
        return ClienteResponse.de(cliente);
    }

    public void excluir(Long id) {
        clienteRepository.delete(buscarEntity(id));
    }

    private ClienteEntity buscarEntity(Long id) {
        return clienteRepository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Cliente " + id + "Não Encontrado"));
    }
}
