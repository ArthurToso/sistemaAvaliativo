package model;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "respostas")
public class Resposta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "texto_resposta", columnDefinition = "TEXT")
    private String textoResposta;

    @ManyToOne
    @JoinColumn(name = "avaliacao_respondida_id", nullable = false)
    private AvaliacaoRespondida avaliacaoRespondida;

    @ManyToOne
    @JoinColumn(name = "questao_id", nullable = false)
    private Questao questao;

    @ManyToOne
    @JoinColumn(name = "alternativa_selecionada_id") // Permite nulo
    private Alternativa alternativaSelecionada;

    public Resposta() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTextoResposta() {
        return textoResposta;
    }

    public void setTextoResposta(String textoResposta) {
        this.textoResposta = textoResposta;
    }

    public AvaliacaoRespondida getAvaliacaoRespondida() {
        return avaliacaoRespondida;
    }

    public void setAvaliacaoRespondida(AvaliacaoRespondida avaliacaoRespondida) {
        this.avaliacaoRespondida = avaliacaoRespondida;
    }

    public Questao getQuestao() {
        return questao;
    }

    public void setQuestao(Questao questao) {
        this.questao = questao;
    }

    public Alternativa getAlternativaSelecionada() {
        return alternativaSelecionada;
    }

    public void setAlternativaSelecionada(Alternativa alternativaSelecionada) {
        this.alternativaSelecionada = alternativaSelecionada;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resposta resposta = (Resposta) o;
        return Objects.equals(id, resposta.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}