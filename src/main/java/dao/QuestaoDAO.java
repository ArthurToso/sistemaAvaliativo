package dao;

import model.Questao;
import java.util.List;

public interface QuestaoDAO {
    void salvar(Questao questao);

    void atualizar(Questao questao);

    void deletar(Long id);

    Questao buscarPorId(Long id);

    List<Questao> listarPorFormulario(Long formularioId);
}
