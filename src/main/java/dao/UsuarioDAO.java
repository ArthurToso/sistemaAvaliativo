package dao;

import model.Usuario;

import java.util.List;

public interface UsuarioDAO{
    void salvar(Usuario usuario);
    void atualizar(Usuario usuario);
    void deletar(Long id);
    Usuario buscarPorId(Long id);
    List<Usuario> listarTodos();
    Usuario buscarPorLogin(String login);
}
