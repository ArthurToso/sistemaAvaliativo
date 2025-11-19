package model.dto;

import model.Questao;
import java.util.HashMap;
import java.util.Map;

public class EstatisticaQuestao {

    private Questao questao;
    private int totalRespostas;
    // Mapa: ID da Alternativa -> Quantidade de votos
    private Map<Long, Long> contagemAlternativas = new HashMap<>();
    // Lista de respostas de texto (para questões abertas)
    private Map<Long, String> respostasTexto = new HashMap<>(); // ID da Resposta -> Texto

    public EstatisticaQuestao(Questao questao) {
        this.questao = questao;
    }

    public void adicionarVotoAlternativa(Long alternativaId) {
        this.contagemAlternativas.put(
                alternativaId,
                this.contagemAlternativas.getOrDefault(alternativaId, 0L) + 1
        );
        this.totalRespostas++;
    }

    public void adicionarRespostaTexto(Long respostaId, String texto) {
        this.respostasTexto.put(respostaId, texto);
        this.totalRespostas++;
    }

    // Getters
    public Questao getQuestao() { return questao; }
    public int getTotalRespostas() { return totalRespostas; }
    public Map<Long, Long> getContagemAlternativas() { return contagemAlternativas; }
    public Map<Long, String> getRespostasTexto() { return respostasTexto; }
}