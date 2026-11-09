package br.com.sistemas.chamados.repository;

import br.com.sistemas.chamados.entity.ComentarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComentarioRepository extends JpaRepository<ComentarioEntity, Long> {
    List<ComentarioEntity> findByChamadoIdOrderByDataCriacaoAsc(Long chamadoId);
}
