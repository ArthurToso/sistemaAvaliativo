package model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "login", nullable = false, unique = true)
    private String login;

    @Column(name = "senha", nullable = false)
    private String senha;

    // --- ESTA É A CORREÇÃO ---
    // O erro 'mappedBy = "perfil"' na classe Perfil
    // exige que o nome deste atributo seja exatamente "perfil".
    @ManyToOne
    @JoinColumn(name = "perfil_id", nullable = false)
    private Perfil perfil;

    // Relacionamentos para o caso de um usuário ser Aluno ou Professor
    // (Lado inverso dos relacionamentos em Turma)
    @ManyToMany(mappedBy = "alunos")
    private Set<Turma> turmasComoAluno = new HashSet<>();

    @ManyToMany(mappedBy = "professores")
    private Set<Turma> turmasComoProfessor = new HashSet<>();

    // Construtor padrão exigido pelo JPA
    public Usuario() {
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    public Set<Turma> getTurmasComoAluno() {
        return turmasComoAluno;
    }

    public void setTurmasComoAluno(Set<Turma> turmasComoAluno) {
        this.turmasComoAluno = turmasComoAluno;
    }

    public Set<Turma> getTurmasComoProfessor() {
        return turmasComoProfessor;
    }

    public void setTurmasComoProfessor(Set<Turma> turmasComoProfessor) {
        this.turmasComoProfessor = turmasComoProfessor;
    }

    // equals() e hashCode()
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(id, usuario.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}