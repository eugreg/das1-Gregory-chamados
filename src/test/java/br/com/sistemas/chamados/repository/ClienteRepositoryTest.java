package br.com.sistemas.chamados.repository;

import br.com.sistemas.chamados.entity.ClienteEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class ClienteRepositoryTest {

    @Autowired
    private ClienteRepository clienteRepository;

    @Test
    void deveSalvarEBuscarPorId() {
        ClienteEntity salvo = clienteRepository.save(new ClienteEntity("ana@teste.com", "Ana", "4799999"));

        ClienteEntity encontrado = clienteRepository.findById(salvo.getId()).orElseThrow();

        assertNotNull(encontrado.getId());
        assertEquals("Ana", encontrado.getNome());
        assertEquals("ana@teste.com", encontrado.getEmail());
    }

    @Test
    void deveRetornarVazioParaIdInexistente() {
        assertTrue(clienteRepository.findById(999L).isEmpty());
    }

    @Test
    void deveVerificarExistenciaPorEmail() {
        clienteRepository.save(new ClienteEntity("ana@teste.com", "Ana", null));

        assertTrue(clienteRepository.existsByEmail("ana@teste.com"));
        assertFalse(clienteRepository.existsByEmail("outro@teste.com"));
    }

    @Test
    void deveIgnorarOProprioClienteNaVerificacaoDeEmail() {
        ClienteEntity ana = clienteRepository.save(new ClienteEntity("ana@teste.com", "Ana", null));
        ClienteEntity bia = clienteRepository.save(new ClienteEntity("bia@teste.com", "Bia", null));

        assertFalse(clienteRepository.existsByEmailAndIdNot("ana@teste.com", ana.getId()));
        assertTrue(clienteRepository.existsByEmailAndIdNot("ana@teste.com", bia.getId()));
    }

    @Test
    void deveRejeitarEmailDuplicado() {
        clienteRepository.saveAndFlush(new ClienteEntity("ana@teste.com", "Ana", null));

        assertThrows(DataIntegrityViolationException.class,
                () -> clienteRepository.saveAndFlush(new ClienteEntity("ana@teste.com", "Outra Ana", null)));
    }

    @Test
    void deveRejeitarNomeNulo() {
        assertThrows(DataIntegrityViolationException.class,
                () -> clienteRepository.saveAndFlush(new ClienteEntity("ana@teste.com", null, null)));
    }
}
