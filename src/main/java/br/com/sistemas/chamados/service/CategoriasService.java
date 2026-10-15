package br.com.sistemas.chamados.service;

import br.com.sistemas.chamados.dto.CategoriasRequest;
import br.com.sistemas.chamados.dto.CategoriasResponse;
import br.com.sistemas.chamados.entity.CategoriasEntity;
import br.com.sistemas.chamados.entity.ClienteEntity;
import br.com.sistemas.chamados.exception.RecursoNaoEncontradoException;
import br.com.sistemas.chamados.repository.CategoriasRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class CategoriasService {


    private final CategoriasRepository categoriasRepository;

    public CategoriasService(CategoriasRepository categoriasRepository) {
        this.categoriasRepository = categoriasRepository;
    }

    public List<CategoriasResponse> listar() {
        return categoriasRepository.findAll().stream().map(CategoriasResponse::de).toList();
    }

    public CategoriasResponse criar(CategoriasRequest categoriasRequest) {
        CategoriasEntity categoriasEntity = new CategoriasEntity(categoriasRequest.nome(), categoriasRequest.descricao());
        categoriasEntity = categoriasRepository.save(categoriasEntity);

        return CategoriasResponse.de(categoriasEntity);
    }

    public CategoriasResponse buscar(Long id) {
        return CategoriasResponse.de(buscarEntity(id));
    }


    private CategoriasEntity buscarEntity(Long id) {
        return categoriasRepository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Categoria " + id + "Não Encontrado"));
    }
}
