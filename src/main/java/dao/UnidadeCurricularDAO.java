package dao;

import model.UnidadeCurricular;

import java.util.List;

public interface UnidadeCurricularDAO {
    void salvar(UnidadeCurricular unidadeCurricular);
    void atualizar(UnidadeCurricular unidadeCurricular);
    void deletar(Long id);
    UnidadeCurricular buscarPorId(Long id);
    List<UnidadeCurricular> listarTodos();
}
