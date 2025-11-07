package dao;

import model.Curso;

import java.util.List;

public interface CursoDAO {
    void salvar(Curso curso);
    void atualizar(Curso curso);
    void deletar(Long id);
    Curso buscarPorId(Long id);
    List<Curso> listarTodos();
}
