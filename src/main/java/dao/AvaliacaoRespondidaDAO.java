package dao;

import model.AvaliacaoRespondida;

import java.util.List;
import java.util.Optional;

public interface AvaliacaoRespondidaDAO {

    /**
     * Salva ou atualiza uma avaliação respondida e todas as suas respostas.
     * @param avaliacao A avaliação respondida a ser salva.
     */
    void salvar(AvaliacaoRespondida avaliacao);

    /**
     * Busca uma avaliação respondida com base no aluno, formulário e turma.
     * Usado para verificar se o aluno já respondeu (RF13).
     * @param alunoId O ID do aluno.
     * @param formularioId O ID do formulário.
     * @param turmaId O ID da turma (contexto).
     * @return Um Optional contendo a AvaliacaoRespondida se existir, ou vazio se não.
     */
    Optional<AvaliacaoRespondida> buscarPorAlunoEFormulario(Long alunoId, Long formularioId, Long turmaId);

    /**
     * Lista as avaliações já respondidas por um aluno que são editáveis (não-anônimas).
     * @param alunoId O ID do aluno.
     * @return Lista de avaliações respondidas e editáveis.
     */
    List<AvaliacaoRespondida> listarRespondidasEditaveis(Long alunoId);

    /**
     * Busca uma AvaliacaoRespondida pelo ID, e já carrega (FETCH)
     * a sua lista de Respostas.
     * @param avaliacaoId O ID da AvaliacaoRespondida.
     * @return Um Optional contendo a avaliação com as respostas.
     */
    Optional<AvaliacaoRespondida> buscarPorIdComRespostas(Long avaliacaoId);

}