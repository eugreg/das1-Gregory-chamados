package br.com.sistemas.chamados.repository;

import br.com.sistemas.chamados.entity.CategoriasEntity;
import br.com.sistemas.chamados.entity.ChamadosEntity;
import br.com.sistemas.chamados.entity.ClienteEntity;
import br.com.sistemas.chamados.entity.TecnicoEntity;
import br.com.sistemas.chamados.enums.PerfilEnum;
import br.com.sistemas.chamados.enums.PrioridadeEnum;
import br.com.sistemas.chamados.enums.StatusChamado;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class ChamadosRepositoryTest {

    private static final Pageable TODOS = PageRequest.of(0, 20, Sort.by("id"));

    @Autowired
    private ChamadosRepository chamadosRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private CategoriasRepository categoriasRepository;

    @Autowired
    private TecnicoRepository tecnicoRepository;

    private ClienteEntity clienteA;
    private ClienteEntity clienteB;
    private TecnicoEntity tecnico;
    private CategoriasEntity categoria;

    @BeforeEach
    void setUp() {
        clienteA = clienteRepository.save(new ClienteEntity("a@teste.com", "Cliente A", null));
        clienteB = clienteRepository.save(new ClienteEntity("b@teste.com", "Cliente B", null));
        tecnico = tecnicoRepository.save(new TecnicoEntity("Tecnico", "tec@teste.com", PerfilEnum.TECNICO));
        categoria = categoriasRepository.save(new CategoriasEntity("Rede", "Problemas de rede"));

        salvar(clienteA, tecnico, StatusChamado.ABERTO, PrioridadeEnum.ALTA);
        salvar(clienteA, null, StatusChamado.EM_ANDAMENTO, PrioridadeEnum.BAIXA);
        salvar(clienteB, tecnico, StatusChamado.ABERTO, PrioridadeEnum.BAIXA);
    }

    @Test
    void deveRetornarTodosSemFiltros() {
        assertEquals(3, filtrar(null, null, null, null).getTotalElements());
    }

    @Test
    void deveFiltrarPorStatus() {
        Page<ChamadosEntity> resultado = filtrar(StatusChamado.ABERTO, null, null, null);

        assertEquals(2, resultado.getTotalElements());
        assertTrue(resultado.stream().allMatch(c -> c.getStatus() == StatusChamado.ABERTO));
    }

    @Test
    void deveCombinarClienteEPrioridade() {
        Page<ChamadosEntity> resultado = filtrar(null, PrioridadeEnum.BAIXA, clienteA.getId(), null);

        assertEquals(1, resultado.getTotalElements());
        assertEquals(StatusChamado.EM_ANDAMENTO, resultado.getContent().getFirst().getStatus());
    }

    @Test
    void deveIncluirChamadoSemTecnicoQuandoNaoFiltraPorTecnico() {
        assertEquals(2, filtrar(null, null, clienteA.getId(), null).getTotalElements());
    }

    @Test
    void deveFiltrarPorTecnico() {
        assertEquals(2, filtrar(null, null, null, tecnico.getId()).getTotalElements());
    }

    @Test
    void deveRetornarVazioQuandoNadaCombina() {
        assertTrue(filtrar(StatusChamado.FECHADO, null, clienteB.getId(), null).isEmpty());
    }

    @Test
    void devePaginar() {
        Page<ChamadosEntity> pagina = chamadosRepository.filtrar(null, null, null, null, null, PageRequest.of(0, 2));

        assertEquals(2, pagina.getContent().size());
        assertEquals(3, pagina.getTotalElements());
        assertEquals(2, pagina.getTotalPages());
    }

    private Page<ChamadosEntity> filtrar(StatusChamado status, PrioridadeEnum prioridade, Long clienteId, Long tecnicoId) {
        return chamadosRepository.filtrar(status, prioridade, clienteId, tecnicoId, null, TODOS);
    }

    private void salvar(ClienteEntity cliente, TecnicoEntity tecnico, StatusChamado status, PrioridadeEnum prioridade) {
        chamadosRepository.save(new ChamadosEntity(
                "Descrição", "Título", LocalDate.now(), tecnico, cliente, categoria, status, prioridade, null));
    }
}
