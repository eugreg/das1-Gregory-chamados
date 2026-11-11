package br.com.sistemas.chamados.controller;

import br.com.sistemas.chamados.dto.ChamadosDetalheResponse;
import br.com.sistemas.chamados.dto.ChamadosFiltro;
import br.com.sistemas.chamados.dto.ChamadosRequest;
import br.com.sistemas.chamados.dto.ChamadosResponse;
import br.com.sistemas.chamados.dto.ComentarioRequest;
import br.com.sistemas.chamados.dto.ComentarioResponse;
import br.com.sistemas.chamados.enums.PrioridadeEnum;
import br.com.sistemas.chamados.enums.StatusChamado;
import br.com.sistemas.chamados.exception.RecursoNaoEncontradoException;
import br.com.sistemas.chamados.service.ChamadosService;
import br.com.sistemas.chamados.service.ComentarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChamadosController.class)
class ChamadosControllerTest {

    private static final String CHAMADO_VALIDO = """
            {
              "titulo": "Sem internet",
              "descricao": "Rede caiu no setor",
              "prioridade": "ALTA",
              "clienteId": 1,
              "categoriaId": 2,
              "tecnicoId": 3
            }
            """;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ChamadosService chamadosService;

    @MockitoBean
    private ComentarioService comentarioService;

    @Test
    void deveCriarChamado() throws Exception {
        when(chamadosService.criar(any(ChamadosRequest.class))).thenReturn(resumo());

        mockMvc.perform(post("/chamados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(CHAMADO_VALIDO))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/chamados/10"))
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.status").value("ABERTO"));
    }

    @Test
    void deveRejeitarChamadoSemCamposObrigatorios() throws Exception {
        mockMvc.perform(post("/chamados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detalhes", hasSize(6)));

        verify(chamadosService, never()).criar(any());
    }

    @Test
    void deveRetornar404QuandoClienteDoChamadoNaoExiste() throws Exception {
        when(chamadosService.criar(any(ChamadosRequest.class)))
                .thenThrow(new RecursoNaoEncontradoException("Cliente 1 não encontrado"));

        mockMvc.perform(post("/chamados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(CHAMADO_VALIDO))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.erro").value("Cliente 1 não encontrado"));
    }

    @Test
    void deveRetornar404ParaChamadoInexistente() throws Exception {
        when(chamadosService.buscar(99L)).thenThrow(new RecursoNaoEncontradoException("Chamado 99 não encontrado"));

        mockMvc.perform(get("/chamados/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void deveExibirComentariosNoDetalhe() throws Exception {
        when(chamadosService.buscar(10L)).thenReturn(detalhe(List.of(
                new ComentarioResponse(1L, "Reiniciei o roteador", "Técnico", LocalDateTime.of(2026, 11, 9, 10, 0)),
                new ComentarioResponse(2L, "Voltou, obrigado", "Cliente", LocalDateTime.of(2026, 11, 9, 11, 0)))));

        mockMvc.perform(get("/chamados/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.comentarios", hasSize(2)))
                .andExpect(jsonPath("$.comentarios[0].texto").value("Reiniciei o roteador"))
                .andExpect(jsonPath("$.comentarios[1].autor").value("Cliente"));
    }

    @Test
    void deveListarComFiltroEPaginacao() throws Exception {
        when(chamadosService.listar(any(ChamadosFiltro.class), any(Pageable.class)))
                .thenReturn(Page.empty());

        mockMvc.perform(get("/chamados?status=ABERTO&clienteId=1&page=0&size=5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(0)));

        verify(chamadosService).listar(
                eq(new ChamadosFiltro(StatusChamado.ABERTO, null, 1L, null, null)),
                any(Pageable.class));
    }

    @Test
    void deveAdicionarComentario() throws Exception {
        when(comentarioService.adicionar(eq(10L), any(ComentarioRequest.class)))
                .thenReturn(new ComentarioResponse(1L, "Reiniciei o roteador", "Técnico", LocalDateTime.now()));

        mockMvc.perform(post("/chamados/10/comentarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"texto": "Reiniciei o roteador", "autor": "Técnico"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.texto").value("Reiniciei o roteador"))
                .andExpect(jsonPath("$.autor").value("Técnico"));
    }

    @Test
    void deveRejeitarComentarioInvalido() throws Exception {
        mockMvc.perform(post("/chamados/10/comentarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"texto": " ", "autor": ""}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detalhes", hasSize(2)));

        verify(comentarioService, never()).adicionar(any(), any());
    }

    @Test
    void deveRetornar404AoComentarEmChamadoInexistente() throws Exception {
        when(comentarioService.adicionar(eq(99L), any(ComentarioRequest.class)))
                .thenThrow(new RecursoNaoEncontradoException("Chamado 99 não encontrado"));

        mockMvc.perform(post("/chamados/99/comentarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"texto": "Olá", "autor": "Cliente"}
                                """))
                .andExpect(status().isNotFound());
    }

    private ChamadosResponse resumo() {
        return new ChamadosResponse(10L, "Sem internet", "Rede caiu no setor", LocalDate.now(), null,
                PrioridadeEnum.ALTA, StatusChamado.ABERTO, 1L, 2L, 3L);
    }

    private ChamadosDetalheResponse detalhe(List<ComentarioResponse> comentarios) {
        return new ChamadosDetalheResponse(10L, "Sem internet", "Rede caiu no setor", LocalDate.now(), null,
                PrioridadeEnum.ALTA, StatusChamado.ABERTO, 1L, 2L, 3L, comentarios);
    }
}
