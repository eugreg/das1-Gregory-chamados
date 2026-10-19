package br.com.sistemas.chamados.repository;

import br.com.sistemas.chamados.entity.TecnicoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TecnicoRepository extends JpaRepository<TecnicoEntity, Long> {
    boolean existsByEmail(String email);
}
