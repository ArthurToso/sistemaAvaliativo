package dao;

import model.dto.EstatisticaQuestao;
import java.util.List;

public interface RelatorioDAO {
    /**
     * Calcula as estatísticas de todas as questões de um formulário
     * aplicado em uma turma específica.
     */
    List<EstatisticaQuestao> gerarRelatorioTurma(Long formularioId, Long turmaId);
}