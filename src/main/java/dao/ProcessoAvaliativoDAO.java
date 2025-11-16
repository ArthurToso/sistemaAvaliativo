package dao;

import model.ProcessoAvaliativo;

import java.util.List;

public interface ProcessoAvaliativoDAO {
    void salvar(ProcessoAvaliativo processoAvaliativo);

    void atualizar(ProcessoAvaliativo processoAvaliativo);

    void deletar(Long id);

    ProcessoAvaliativo buscarPorId(Long id);

    List<ProcessoAvaliativo> listarTodos();
}
