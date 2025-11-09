package dao;

import model.Perfil;

import java.util.List;

public interface PerfilDAO {
    Perfil buscarPorId(Long id);
    List<Perfil> listarTodos();
}
