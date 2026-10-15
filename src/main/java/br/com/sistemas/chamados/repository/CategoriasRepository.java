package br.com.sistemas.chamados.repository;

import br.com.sistemas.chamados.entity.CategoriasEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CategoriasRepository extends JpaRepository<CategoriasEntity, Long> {
    boolean existsByNome(String nome);
}
