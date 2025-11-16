package dao;

import model.Alternativa;
import java.util.List;

public interface AlternativaDAO {

    void salvar(Alternativa alternativa);

    void atualizar(Alternativa alternativa);

    void deletar(Long id);

    Alternativa buscarPorId(Long id);

    List<Alternativa> listarPorQuestao(Long questaoId);
}