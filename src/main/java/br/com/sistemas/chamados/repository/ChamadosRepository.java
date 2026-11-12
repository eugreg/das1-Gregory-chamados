package br.com.sistemas.chamados.repository;

import br.com.sistemas.chamados.entity.ChamadosEntity;
import br.com.sistemas.chamados.enums.PrioridadeEnum;
import br.com.sistemas.chamados.enums.StatusChamado;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChamadosRepository extends JpaRepository<ChamadosEntity, Long> {

    @Query("""
            select c from ChamadosEntity c
            where (:status is null or c.status = :status)
              and (:prioridade is null or c.prioridade = :prioridade)
              and (:clienteId is null or c.cliente.id = :clienteId)
              and (:tecnicoId is null or c.tecnico.id = :tecnicoId)
              and (:categoriaId is null or c.cateforia.id = :categoriaId)
            """)
    Page<ChamadosEntity> filtrar(
            @Param("status") StatusChamado status,
            @Param("prioridade") PrioridadeEnum prioridade,
            @Param("clienteId") Long clienteId,
            @Param("tecnicoId") Long tecnicoId,
            @Param("categoriaId") Long categoriaId,
            Pageable pageable
    );
}
