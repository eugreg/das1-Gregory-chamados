package br.com.sistemas.chamados.service;

import br.com.sistemas.chamados.dto.CategoriasRequest;
import br.com.sistemas.chamados.dto.CategoriasResponse;
import br.com.sistemas.chamados.entity.CategoriasEntity;
import br.com.sistemas.chamados.entity.ClienteEntity;
import br.com.sistemas.chamados.exception.RecursoNaoEncontradoException;
import br.com.sistemas.chamados.exception.RegraNegocioException;
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
        if (categoriasRepository.existsByNome(categoriasRequest.nome()))
            throw new RegraNegocioException("Ja existe categoria com esse nome " + categoriasRequest.nome());

        CategoriasEntity categoriasEntity = new CategoriasEntity(categoriasRequest.nome(), categoriasRequest.descricao());
        categoriasEntity = categoriasRepository.save(categoriasEntity);

        return CategoriasResponse.de(categoriasEntity);
    }

    public CategoriasResponse buscar(Long id) {
        return CategoriasResponse.de(buscarEntity(id));
    }

    public CategoriasResponse atualizar(Long id, CategoriasRequest categoriasRequest) {
        CategoriasEntity categoriasEntity = buscarEntity(id);


        categoriasEntity.setNome(categoriasRequest.nome());
        categoriasEntity.setDescricao(categoriasRequest.descricao());
        CategoriasEntity salvo = categoriasRepository.save(categoriasEntity);
        return CategoriasResponse.de(salvo);
    }

    public void remover(Long id) {
        categoriasRepository.delete(buscarEntity(id));
    }

    private CategoriasEntity buscarEntity(Long id) {
        return categoriasRepository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Categoria " + id + "Não Encontrado"));
    }
}
