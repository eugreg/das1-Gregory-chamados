
package br.com.sistemas.chamados.service;

import br.com.sistemas.chamados.dto.ChamadosRequest;
import br.com.sistemas.chamados.dto.ChamadosResponse;
import br.com.sistemas.chamados.entity.ChamadosEntity;
import br.com.sistemas.chamados.entity.ClienteEntity;
import br.com.sistemas.chamados.entity.CategoriasEntity;
import br.com.sistemas.chamados.entity.TecnicoEntity;
import br.com.sistemas.chamados.enums.StatusChamado;
import br.com.sistemas.chamados.exception.RecursoNaoEncontradoException;
import br.com.sistemas.chamados.repository.ChamadosRepository;
import br.com.sistemas.chamados.repository.ClienteRepository;
import br.com.sistemas.chamados.repository.CategoriasRepository;
import br.com.sistemas.chamados.repository.TecnicoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class ChamadosService {

    private final ChamadosRepository chamadosRepository;
    private final ClienteRepository clienteRepository;
    private final CategoriasRepository categoriasRepository;
    private final TecnicoRepository tecnicoRepository;

    public ChamadosService(
            ChamadosRepository chamadosRepository,
            ClienteRepository clienteRepository,
            CategoriasRepository categoriasRepository,
            TecnicoRepository tecnicoRepository
    ) {
        this.chamadosRepository = chamadosRepository;
        this.clienteRepository = clienteRepository;
        this.categoriasRepository = categoriasRepository;
        this.tecnicoRepository = tecnicoRepository;
    }

    public ChamadosResponse criar(ChamadosRequest request) {
        ClienteEntity cliente = buscarCliente(request.clienteId());
        CategoriasEntity categoria = buscarCategoria(request.categoriaId());
        TecnicoEntity tecnico = buscarTecnico(request.tecnicoId());

        ChamadosEntity chamado = new ChamadosEntity(
                request.descricao(),
                request.titulo(),
                LocalDate.now(),
                tecnico,
                cliente,
                categoria,
                StatusChamado.ABERTO,
                request.prioridade(),
                request.dataFechamento()
        );

        ChamadosEntity salvo = chamadosRepository.save(chamado);

        return ChamadosResponse.de(salvo);
    }

    public List<ChamadosResponse> listar() {
        return chamadosRepository.findAll()
                .stream()
                .map(ChamadosResponse::de)
                .toList();
    }

    public ChamadosResponse buscar(Long id) {
        return ChamadosResponse.de(buscarEntity(id));
    }

    public ChamadosResponse atualizar(Long id, ChamadosRequest request) {
        ChamadosEntity chamado = buscarEntity(id);

        ClienteEntity cliente = buscarCliente(request.clienteId());
        CategoriasEntity categoria = buscarCategoria(request.categoriaId());
        TecnicoEntity tecnico = buscarTecnico(request.tecnicoId());

        chamado.setTitulo(request.titulo());
        chamado.setDescricao(request.descricao());
        chamado.setDataAbertura(request.dataAbertura());
        chamado.setDataFechamento(request.dataFechamento());
        chamado.setPrioridade(request.prioridade());
        chamado.setStatus(request.status());
        chamado.setCliente(cliente);
        chamado.setCateforia(categoria);
        chamado.setTecnico(tecnico);

        return ChamadosResponse.de(chamado);
    }

    public void excluir(Long id) {
        chamadosRepository.delete(buscarEntity(id));
    }

    private ChamadosEntity buscarEntity(Long id) {
        return chamadosRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException(
                                "Chamado " + id + " não encontrado"
                        )
                );
    }

    private ClienteEntity buscarCliente(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException(
                                "Cliente " + id + " não encontrado"
                        )
                );
    }

    private CategoriasEntity buscarCategoria(Long id) {
        return categoriasRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException(
                                "Categoria " + id + " não encontrada"
                        )
                );
    }

    private TecnicoEntity buscarTecnico(Long id) {
        return tecnicoRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException(
                                "Técnico " + id + " não encontrado"
                        )
                );
    }
}
