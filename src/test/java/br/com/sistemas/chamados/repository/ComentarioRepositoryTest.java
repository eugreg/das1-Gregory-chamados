package br.com.sistemas.chamados.repository;

import br.com.sistemas.chamados.entity.ChamadosEntity;
import br.com.sistemas.chamados.entity.ClienteEntity;
import br.com.sistemas.chamados.entity.ComentarioEntity;
import br.com.sistemas.chamados.enums.PrioridadeEnum;
import br.com.sistemas.chamados.enums.StatusChamado;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class ComentarioRepositoryTest {

    @Autowired
    private ComentarioRepository comentarioRepository;

    @Autowired
    private ChamadosRepository chamadosRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private EntityManager entityManager;

    private ChamadosEntity chamado;

    @BeforeEach
    void setUp() {
        ClienteEntity cliente = clienteRepository.save(new ClienteEntity("a@teste.com", "Cliente", null));
        chamado = chamadosRepository.save(new ChamadosEntity(
                "Descrição", "Título", LocalDate.now(), null, cliente, null,
                StatusChamado.ABERTO, PrioridadeEnum.MEDIA, null));
    }

    @Test
    void deveListarComentariosDoChamadoEmOrdemCronologica() {
        LocalDateTime agora = LocalDateTime.now();
        comentarioRepository.save(new ComentarioEntity("Segundo", "Técnico", chamado, agora));
        comentarioRepository.save(new ComentarioEntity("Primeiro", "Cliente", chamado, agora.minusHours(1)));

        List<ComentarioEntity> comentarios = comentarioRepository.findByChamadoIdOrderByDataCriacaoAsc(chamado.getId());

        assertEquals(List.of("Primeiro", "Segundo"), comentarios.stream().map(ComentarioEntity::getTexto).toList());
    }

    @Test
    void deveCarregarComentariosPeloChamado() {
        chamado.adicionarComentario("Olá", "Cliente");
        chamadosRepository.saveAndFlush(chamado);
        entityManager.clear();

        ChamadosEntity recarregado = chamadosRepository.findById(chamado.getId()).orElseThrow();

        assertEquals(1, recarregado.getComentarios().size());
        assertEquals("Olá", recarregado.getComentarios().getFirst().getTexto());
        assertEquals("Cliente", recarregado.getComentarios().getFirst().getAutor());
    }

    @Test
    void deveRemoverComentariosAoExcluirChamado() {
        chamado.adicionarComentario("Olá", "Cliente");
        chamadosRepository.saveAndFlush(chamado);

        chamadosRepository.delete(chamado);
        chamadosRepository.flush();

        assertTrue(comentarioRepository.findAll().isEmpty());
    }
}
