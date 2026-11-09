package br.com.sistemas.chamados.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "comentarios")
public class ComentarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 1000)
    private String texto;

    @Column(nullable = false)
    private LocalDateTime dataCriacao;

    @Column(nullable = false, length = 100)
    private String autor;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "chamado_id", nullable = false)
    private ChamadosEntity chamado;

    public ComentarioEntity() {
    }

    public ComentarioEntity(String texto, String autor, ChamadosEntity chamado) {
        this(texto, autor, chamado, LocalDateTime.now());
    }

    public ComentarioEntity(String texto, String autor, ChamadosEntity chamado, LocalDateTime dataCriacao) {
        this.texto = texto;
        this.autor = autor;
        this.chamado = chamado;
        this.dataCriacao = dataCriacao;
    }

    public Long getId() {
        return id;
    }

    public String getTexto() {
        return texto;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public String getAutor() {
        return autor;
    }

    public ChamadosEntity getChamado() {
        return chamado;
    }
}
