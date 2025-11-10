package dao;

import model.Turma;
import java.util.List;

public interface TurmaDAO {
    void salvar(Turma turma);
    void atualizar(Turma turma);
    void deletar(Long id);
    Turma buscarPorId(Long id);
    Turma buscarPorIdComListas(Long id);
    List<Turma> listarTodos();
}