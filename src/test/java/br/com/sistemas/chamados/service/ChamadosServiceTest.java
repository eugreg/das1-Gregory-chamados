package br.com.sistemas.chamados.service;

import br.com.sistemas.chamados.dto.AlterarStatusRequest;
import br.com.sistemas.chamados.dto.AtribuirTecnicoRequest;
import br.com.sistemas.chamados.dto.ChamadosRequest;
import br.com.sistemas.chamados.dto.ChamadosResponse;
import br.com.sistemas.chamados.entity.ChamadosEntity;
import br.com.sistemas.chamados.entity.TecnicoEntity;
import br.com.sistemas.chamados.enums.StatusChamado;
import br.com.sistemas.chamados.exception.ChamadoFechadoException;
import br.com.sistemas.chamados.exception.TransicaoStatusInvalidaException;
import br.com.sistemas.chamados.repository.CategoriasRepository;
import br.com.sistemas.chamados.repository.ChamadosRepository;
import br.com.sistemas.chamados.repository.ClienteRepository;
import br.com.sistemas.chamados.repository.TecnicoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChamadosServiceTest {

    @Mock
    private ChamadosRepository chamadosRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private CategoriasRepository categoriasRepository;

    @Mock
    private TecnicoRepository tecnicoRepository;

    @InjectMocks
    private ChamadosService chamadosService;

    private ChamadosEntity chamado;

    @BeforeEach
    void setUp() {
        chamado = new ChamadosEntity();
        chamado.setId(1L);
        chamado.setStatus(StatusChamado.ABERTO);
    }

    @ParameterizedTest
    @CsvSource({
            "ABERTO, EM_ANDAMENTO",
            "ABERTO, FECHADO",
            "EM_ANDAMENTO, AGUARDANDO_CLIENTE",
            "EM_ANDAMENTO, FECHADO",
            "AGUARDANDO_CLIENTE, EM_ANDAMENTO",
            "AGUARDANDO_CLIENTE, FECHADO"
    })
    void devePermitirTransicoesValidas(StatusChamado atual, StatusChamado destino) {
        chamado.setStatus(atual);
        when(chamadosRepository.findById(1L)).thenReturn(Optional.of(chamado));

        ChamadosResponse resposta = chamadosService.alterarStatus(1L, new AlterarStatusRequest(destino));

        assertEquals(destino, resposta.status());
    }

    @ParameterizedTest
    @CsvSource({
            "ABERTO, ABERTO",
            "ABERTO, AGUARDANDO_CLIENTE",
            "EM_ANDAMENTO, ABERTO",
            "EM_ANDAMENTO, EM_ANDAMENTO",
            "AGUARDANDO_CLIENTE, ABERTO",
            "AGUARDANDO_CLIENTE, AGUARDANDO_CLIENTE"
    })
    void deveRejeitarTransicoesInvalidas(StatusChamado atual, StatusChamado destino) {
        chamado.setStatus(atual);
        when(chamadosRepository.findById(1L)).thenReturn(Optional.of(chamado));

        assertThrows(TransicaoStatusInvalidaException.class,
                () -> chamadosService.alterarStatus(1L, new AlterarStatusRequest(destino)));

        assertEquals(atual, chamado.getStatus());
    }

    @Test
    void devePreencherDataFechamentoAoFechar() {
        when(chamadosRepository.findById(1L)).thenReturn(Optional.of(chamado));

        ChamadosResponse resposta = chamadosService.alterarStatus(1L, new AlterarStatusRequest(StatusChamado.FECHADO));

        assertEquals(LocalDate.now(), resposta.dataFechamento());
    }

    @Test
    void naoDevePreencherDataFechamentoEmOutrasTransicoes() {
        when(chamadosRepository.findById(1L)).thenReturn(Optional.of(chamado));

        ChamadosResponse resposta = chamadosService.alterarStatus(1L, new AlterarStatusRequest(StatusChamado.EM_ANDAMENTO));

        assertNull(resposta.dataFechamento());
    }

    @Test
    void deveImpedirAlteracaoDeStatusDeChamadoFechado() {
        chamado.setStatus(StatusChamado.FECHADO);
        when(chamadosRepository.findById(1L)).thenReturn(Optional.of(chamado));

        assertThrows(ChamadoFechadoException.class,
                () -> chamadosService.alterarStatus(1L, new AlterarStatusRequest(StatusChamado.EM_ANDAMENTO)));
    }

    @Test
    void deveAtribuirTecnico() {
        TecnicoEntity tecnico = new TecnicoEntity();
        tecnico.setId(7L);
        when(chamadosRepository.findById(1L)).thenReturn(Optional.of(chamado));
        when(tecnicoRepository.findById(7L)).thenReturn(Optional.of(tecnico));

        ChamadosResponse resposta = chamadosService.atribuirTecnico(1L, new AtribuirTecnicoRequest(7L));

        assertNotNull(resposta.tecnicoId());
        assertEquals(7L, resposta.tecnicoId());
    }

    @Test
    void deveImpedirAtribuicaoEmChamadoFechado() {
        chamado.setStatus(StatusChamado.FECHADO);
        when(chamadosRepository.findById(1L)).thenReturn(Optional.of(chamado));

        assertThrows(ChamadoFechadoException.class,
                () -> chamadosService.atribuirTecnico(1L, new AtribuirTecnicoRequest(7L)));

        verifyNoInteractions(tecnicoRepository);
    }

    @Test
    void deveImpedirAtualizacaoDeChamadoFechado() {
        chamado.setStatus(StatusChamado.FECHADO);
        when(chamadosRepository.findById(1L)).thenReturn(Optional.of(chamado));

        ChamadosRequest request = new ChamadosRequest("t", "d", null, null, null, null, 1L, 1L, 1L);

        assertThrows(ChamadoFechadoException.class, () -> chamadosService.atualizar(1L, request));

        verifyNoInteractions(clienteRepository, categoriasRepository, tecnicoRepository);
    }
}
