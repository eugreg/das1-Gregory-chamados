package br.com.sistemas.chamados.entity;


import br.com.sistemas.chamados.enums.PerfilEnum;
import jakarta.persistence.*;

@Entity
@Table(name = "tecnico")
public class TecnicoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Column(nullable = false, unique = true, length = 100)
    private String email;


    @Column(nullable = false, unique = true, length = 100)
    private PerfilEnum perfil;

    public TecnicoEntity() {
    }

    public TecnicoEntity(String nome, String email, PerfilEnum perfil) {
        this.nome = nome;
        this.email = email;
        this.perfil = perfil;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public PerfilEnum getPerfil() {
        return perfil;
    }

    public void setPerfil(PerfilEnum perfil) {
        this.perfil = perfil;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
