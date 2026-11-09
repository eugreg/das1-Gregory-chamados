package br.com.sistemas.chamados.controller;

import br.com.sistemas.chamados.entity.CategoriasEntity;
import br.com.sistemas.chamados.entity.ChamadosEntity;
import br.com.sistemas.chamados.entity.ClienteEntity;
import br.com.sistemas.chamados.entity.TecnicoEntity;
import br.com.sistemas.chamados.enums.PerfilEnum;
import br.com.sistemas.chamados.enums.PrioridadeEnum;
import br.com.sistemas.chamados.enums.StatusChamado;
import br.com.sistemas.chamados.repository.CategoriasRepository;
import br.com.sistemas.chamados.repository.ChamadosRepository;
import br.com.sistemas.chamados.repository.ClienteRepository;
import br.com.sistemas.chamados.repository.TecnicoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ChamadosConsultaTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ChamadosRepository chamadosRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private CategoriasRepository categoriasRepository;

    @Autowired
    private TecnicoRepository tecnicoRepository;

    private Map<String, Long> ids;

    private Long ultimoChamadoId;

    @BeforeEach
    void setUp() {
        ClienteEntity clienteA = clienteRepository.save(new ClienteEntity("a@teste.com", "Cliente A", "1111"));
        ClienteEntity clienteB = clienteRepository.save(new ClienteEntity("b@teste.com", "Cliente B", "2222"));
        TecnicoEntity tecnicoX = tecnicoRepository.save(new TecnicoEntity("Tecnico X", "x@teste.com", PerfilEnum.TECNICO));
        TecnicoEntity tecnicoY = tecnicoRepository.save(new TecnicoEntity("Tecnico Y", "y@teste.com", PerfilEnum.TECNICO));
        CategoriasEntity rede = categoriasRepository.save(new CategoriasEntity("Rede", "Problemas de rede"));
        CategoriasEntity hardware = categoriasRepository.save(new CategoriasEntity("Hardware", "Problemas de hardware"));

        salvar(clienteA, tecnicoX, rede, StatusChamado.ABERTO, PrioridadeEnum.ALTA);
        salvar(clienteA, tecnicoY, rede, StatusChamado.EM_ANDAMENTO, PrioridadeEnum.ALTA);
        salvar(clienteB, tecnicoX, hardware, StatusChamado.ABERTO, PrioridadeEnum.BAIXA);
        salvar(clienteB, null, rede, StatusChamado.FECHADO, PrioridadeEnum.ALTA);
        ultimoChamadoId = salvar(clienteA, null, hardware, StatusChamado.ABERTO, PrioridadeEnum.BAIXA).getId();

        ids = Map.of(
                "{A}", clienteA.getId(),
                "{B}", clienteB.getId(),
                "{X}", tecnicoX.getId(),
                "{Y}", tecnicoY.getId(),
                "{REDE}", rede.getId(),
                "{HARDWARE}", hardware.getId()
        );
    }

    static Stream<Arguments> combinacoesDeFiltros() {
        return Stream.of(
                Arguments.of("", 5),
                Arguments.of("status=ABERTO", 3),
                Arguments.of("prioridade=ALTA", 3),
                Arguments.of("status=ABERTO&prioridade=ALTA", 1),
                Arguments.of("clienteId={A}&status=ABERTO", 2),
                Arguments.of("tecnicoId={X}", 2),
                Arguments.of("tecnicoId={X}&prioridade=BAIXA", 1),
                Arguments.of("categoriaId={REDE}&clienteId={B}", 1),
                Arguments.of("status=ABERTO&prioridade=BAIXA&clienteId={A}&categoriaId={HARDWARE}", 1),
                Arguments.of("status=FECHADO&clienteId={A}", 0),
                Arguments.of("tecnicoId={Y}&status=ABERTO", 0)
        );
    }

    @ParameterizedTest
    @MethodSource("combinacoesDeFiltros")
    void deveFiltrarChamados(String filtros, int esperado) throws Exception {
        mockMvc.perform(get("/chamados?" + resolver(filtros)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(esperado)))
                .andExpect(jsonPath("$.page.totalElements").value(esperado));
    }

    @Test
    void devePaginarEOrdenar() throws Exception {
        mockMvc.perform(get("/chamados?page=0&size=2&sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].id").value(ultimoChamadoId))
                .andExpect(jsonPath("$.page.size").value(2))
                .andExpect(jsonPath("$.page.number").value(0))
                .andExpect(jsonPath("$.page.totalElements").value(5))
                .andExpect(jsonPath("$.page.totalPages").value(3));
    }

    @Test
    void deveRetornarUltimaPagina() throws Exception {
        mockMvc.perform(get("/chamados?page=2&size=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)));
    }

    @Test
    void deveRejeitarStatusInvalido() throws Exception {
        mockMvc.perform(get("/chamados?status=QUALQUER"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveListarChamadosDoCliente() throws Exception {
        mockMvc.perform(get("/clientes/" + ids.get("{A}") + "/chamados?sort=prioridade"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(3)))
                .andExpect(jsonPath("$.content[*].clienteId").value(everyItem(equalTo(ids.get("{A}").intValue()))));
    }

    @Test
    void deveRetornar404ParaClienteInexistente() throws Exception {
        mockMvc.perform(get("/clientes/999999/chamados"))
                .andExpect(status().isNotFound());
    }

    private ChamadosEntity salvar(ClienteEntity cliente, TecnicoEntity tecnico, CategoriasEntity categoria,
                                  StatusChamado status, PrioridadeEnum prioridade) {
        return chamadosRepository.save(new ChamadosEntity(
                "Descrição", "Título", LocalDate.now(), tecnico, cliente, categoria, status, prioridade, null));
    }

    private String resolver(String filtros) {
        String resultado = filtros;
        for (Map.Entry<String, Long> entrada : ids.entrySet()) {
            resultado = resultado.replace(entrada.getKey(), entrada.getValue().toString());
        }
        return resultado;
    }
}
