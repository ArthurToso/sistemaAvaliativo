package controller;

import dao.FormularioDAO;
import dao.PerfilDAO;
import dao.ProcessoAvaliativoDAO;
import dao.impl.FormularioDAOImpl;
import dao.impl.PerfilDAOImpl;
import dao.impl.ProcessoAvaliativoDAOImpl;
import model.Formulario;
import model.Perfil;
import model.ProcessoAvaliativo;
import model.TipoFormulario;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@WebServlet("/formularios")
public class FormularioServlet extends HttpServlet {

    private FormularioDAO formularioDAO;
    private ProcessoAvaliativoDAO processoAvaliativoDAO;
    private PerfilDAO perfilDAO;

    @Override
    public void init() {
        // Instancia todos os DAOs necessários
        this.formularioDAO = new FormularioDAOImpl();
        this.processoAvaliativoDAO = new ProcessoAvaliativoDAOImpl();
        this.perfilDAO = new PerfilDAOImpl();
    }

    /**
     * Chamado quando a página é carregada (GET).
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Busca a lista de Formulários (para a tabela)
        List<Formulario> listaFormularios = formularioDAO.listarTodos();

        // 2. Busca listas para os dropdowns do formulário
        List<ProcessoAvaliativo> listaProcessos = processoAvaliativoDAO.listarTodos();
        List<Perfil> listaPerfis = perfilDAO.listarTodos();

        // 3. Coloca TODAS as listas no objeto 'request'
        request.setAttribute("listaFormularios", listaFormularios);
        request.setAttribute("listaProcessos", listaProcessos);
        request.setAttribute("listaPerfis", listaPerfis);

        // 4. Disponibiliza os valores do Enum TipoFormulario para o JSP (RF11)
        request.setAttribute("tiposFormulario", TipoFormulario.values());

        // 5. Encaminha para o JSP
        RequestDispatcher dispatcher = request.getRequestDispatcher("/formularios.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * Chamado quando o formulário é enviado (POST).
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Pega os dados simples
        String titulo = request.getParameter("titulo");
        Long processoId = Long.parseLong(request.getParameter("processoId"));

        // 2. Converte a String do 'tipo' para o Enum (RF11)
        TipoFormulario tipo = TipoFormulario.valueOf(request.getParameter("tipo"));

        // 3. Pega o array de IDs dos perfis (RF07)
        String[] perfilIds = request.getParameterValues("perfilIds");

        // 4. Busca os objetos completos
        ProcessoAvaliativo processo = processoAvaliativoDAO.buscarPorId(processoId);

        // 5. Monta o Set<Perfil>
        Set<Perfil> perfisDestinados = new HashSet<>();
        if (perfilIds != null) {
            for (String id : perfilIds) {
                Perfil perfil = perfilDAO.buscarPorId(Long.parseLong(id));
                if (perfil != null) {
                    perfisDestinados.add(perfil);
                }
            }
        }

        // 6. Cria o novo Formulario
        Formulario novoFormulario = new Formulario();
        novoFormulario.setTitulo(titulo);
        novoFormulario.setProcessoAvaliativo(processo);
        novoFormulario.setTipo(tipo);
        novoFormulario.setPerfisDestinados(perfisDestinados);

        // 7. Salva no banco
        formularioDAO.salvar(novoFormulario);

        // 8. Redireciona de volta (Padrão PRG)
        response.sendRedirect(request.getContextPath() + "/formularios");
    }
}