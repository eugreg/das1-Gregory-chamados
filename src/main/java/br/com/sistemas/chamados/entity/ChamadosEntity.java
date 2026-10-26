package br.com.sistemas.chamados.entity;

import br.com.sistemas.chamados.enums.PrioridadeEnum;
import br.com.sistemas.chamados.enums.StatusChamado;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "chamados")
public class ChamadosEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    private String descricao;

    private LocalDate dataAbertura;

    private LocalDate dataFechamento;

    private PrioridadeEnum prioridade;

    private StatusChamado status;

    @ManyToOne(fetch = FetchType.LAZY)
    private ClienteEntity cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    private CategoriasEntity cateforia;

    @ManyToOne(fetch = FetchType.LAZY)
    private TecnicoEntity tecnico;

    public ChamadosEntity() {
    }

    public ChamadosEntity(String descricao, String titulo, LocalDate dataAbertura, TecnicoEntity tecnico, ClienteEntity cliente, CategoriasEntity cateforia, StatusChamado status, PrioridadeEnum prioridade, LocalDate dataFechamento) {
        this.descricao = descricao;
        this.titulo = titulo;
        this.dataAbertura = dataAbertura;
        this.tecnico = tecnico;
        this.cliente = cliente;
        this.cateforia = cateforia;
        this.status = status;
        this.prioridade = prioridade;
        this.dataFechamento = dataFechamento;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDate getDataFechamento() {
        return dataFechamento;
    }

    public void setDataFechamento(LocalDate dataFechamento) {
        this.dataFechamento = dataFechamento;
    }

    public LocalDate getDataAbertura() {
        return dataAbertura;
    }

    public void setDataAbertura(LocalDate dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    public PrioridadeEnum getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(PrioridadeEnum prioridade) {
        this.prioridade = prioridade;
    }

    public StatusChamado getStatus() {
        return status;
    }

    public void setStatus(StatusChamado status) {
        this.status = status;
    }

    public ClienteEntity getCliente() {
        return cliente;
    }

    public void setCliente(ClienteEntity cliente) {
        this.cliente = cliente;
    }

    public CategoriasEntity getCateforia() {
        return cateforia;
    }

    public void setCateforia(CategoriasEntity cateforia) {
        this.cateforia = cateforia;
    }

    public TecnicoEntity getTecnico() {
        return tecnico;
    }

    public void setTecnico(TecnicoEntity tecnico) {
        this.tecnico = tecnico;
    }
}
