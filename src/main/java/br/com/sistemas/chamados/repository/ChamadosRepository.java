package br.com.sistemas.chamados.repository;

import br.com.sistemas.chamados.entity.ChamadosEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChamadosRepository extends JpaRepository<ChamadosEntity, Long> {
}
