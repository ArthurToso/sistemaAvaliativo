package dao;

import model.Formulario;
import java.util.List;

public interface FormularioDAO {

    void salvar(Formulario formulario);

    void atualizar(Formulario formulario);

    void deletar(Long id);

    Formulario buscarPorId(Long id);

    List<Formulario> listarTodos();

    List<Object[]> listarAvaliacoesPendentes(Long alunoId);
}