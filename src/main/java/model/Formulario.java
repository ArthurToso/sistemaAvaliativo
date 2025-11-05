package model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

@Entity
@Table(name = "formularios")
public class Formulario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "titulo", nullable = false)
    private String titulo;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoFormulario tipo; // Usa o Enum

    @ManyToOne
    @JoinColumn(name = "processo_avaliativo_id", nullable = false)
    private ProcessoAvaliativo processoAvaliativo;

    @OneToMany(mappedBy = "formulario", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Questao> questoes = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "formularios_perfis",
            joinColumns = @JoinColumn(name = "formulario_id"),
            inverseJoinColumns = @JoinColumn(name = "perfil_id")
    )
    private Set<Perfil> perfisDestinados = new HashSet<>();

    public Formulario() {
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

    public TipoFormulario getTipo() {
        return tipo;
    }

    public void setTipo(TipoFormulario tipo) {
        this.tipo = tipo;
    }

    public ProcessoAvaliativo getProcessoAvaliativo() {
        return processoAvaliativo;
    }

    public void setProcessoAvaliativo(ProcessoAvaliativo processoAvaliativo) {
        this.processoAvaliativo = processoAvaliativo;
    }

    public Set<Questao> getQuestoes() {
        return questoes;
    }

    public void setQuestoes(Set<Questao> questoes) {
        this.questoes = questoes;
    }

    public Set<Perfil> getPerfisDestinados() {
        return perfisDestinados;
    }

    public void setPerfisDestinados(Set<Perfil> perfisDestinados) {
        this.perfisDestinados = perfisDestinados;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Formulario that = (Formulario) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}