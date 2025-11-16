package dao;

import model.Perfil;

import java.util.List;

public interface PerfilDAO {
    void salvar(Perfil perfil);
    void atualizar(Perfil perfil);
    void deletar(Long id);
    Perfil buscarPorId(Long id);
    List<Perfil> listarTodos();
}
