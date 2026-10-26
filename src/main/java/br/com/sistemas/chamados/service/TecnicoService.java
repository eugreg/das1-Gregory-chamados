package br.com.sistemas.chamados.service;

import br.com.sistemas.chamados.dto.TecnicoRequest;
import br.com.sistemas.chamados.dto.TecnicoResponse;
import br.com.sistemas.chamados.entity.TecnicoEntity;
import br.com.sistemas.chamados.exception.RecursoNaoEncontradoException;
import br.com.sistemas.chamados.exception.RegraNegocioException;
import br.com.sistemas.chamados.repository.ClienteRepository;
import br.com.sistemas.chamados.repository.TecnicoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class TecnicoService {

    private final TecnicoRepository tecnicoRepositoy;

    public TecnicoService(TecnicoRepository tecnicoRepositoy) {
        this.tecnicoRepositoy = tecnicoRepositoy;
    }

    public TecnicoResponse criar(TecnicoRequest tecnicoRequest) {
        if (tecnicoRepositoy.existsByEmail(tecnicoRequest.email()))
            throw new RegraNegocioException("Ja existe cliente com e-mail " + tecnicoRequest.email());

        TecnicoEntity clienteEntity = new TecnicoEntity(tecnicoRequest.nome(), tecnicoRequest.email(), tecnicoRequest.perfil());
        TecnicoEntity salvo = tecnicoRepositoy.save(clienteEntity);

        return TecnicoResponse.de(salvo);
    }

    public List<TecnicoResponse> listar() {
        return tecnicoRepositoy.findAll().stream().map(TecnicoResponse::de).toList();
    }

    public TecnicoResponse buscar(Long id) {
        return TecnicoResponse.de(buscarEntity(id));
    }

    public TecnicoResponse atualizar(Long id, TecnicoRequest tecnicoRequest) {
        TecnicoEntity cliente = buscarEntity(id);
        if (tecnicoRepositoy.existsByEmail(tecnicoRequest.email())) {
            throw new RegraNegocioException("Ja existe com e-mail " + tecnicoRequest.email());
        }

        cliente.setEmail(tecnicoRequest.email());
        cliente.setNome(tecnicoRequest.nome());
        cliente.setPerfil(tecnicoRequest.perfil());
        return TecnicoResponse.de(cliente);
    }

    public void excluir(Long id) {
        tecnicoRepositoy.delete(buscarEntity(id));
    }

    private TecnicoEntity buscarEntity(Long id) {
        return tecnicoRepositoy.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Cliente " + id + "Não Encontrado"));
    }
}
