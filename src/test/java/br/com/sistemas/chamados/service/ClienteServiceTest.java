package br.com.sistemas.chamados.service;

import br.com.sistemas.chamados.dto.ClienteRequest;
import br.com.sistemas.chamados.dto.ClienteResponse;
import br.com.sistemas.chamados.entity.ClienteEntity;
import br.com.sistemas.chamados.exception.RegraNegocioException;
import br.com.sistemas.chamados.repository.ClienteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    @Test
    void deveCriarClienteComCamposNosLugaresCertos() {
        when(clienteRepository.existsByEmail("ana@teste.com")).thenReturn(false);
        when(clienteRepository.save(any(ClienteEntity.class))).thenAnswer(inv -> inv.getArgument(0));

        ClienteResponse resposta = clienteService.criar(new ClienteRequest("Ana", "ana@teste.com", "4799999"));

        assertEquals("Ana", resposta.nome());
        assertEquals("ana@teste.com", resposta.email());
        assertEquals("4799999", resposta.telefone());
    }

    @Test
    void deveImpedirEmailDuplicado() {
        when(clienteRepository.existsByEmail("ana@teste.com")).thenReturn(true);

        assertThrows(RegraNegocioException.class,
                () -> clienteService.criar(new ClienteRequest("Ana", "ana@teste.com", null)));

        verify(clienteRepository, never()).save(any());
    }
}
