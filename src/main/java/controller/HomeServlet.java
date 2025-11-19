package controller;

import dao.AvaliacaoRespondidaDAO;
import dao.FormularioDAO;
import dao.impl.AvaliacaoRespondidaDAOImpl;
import dao.impl.FormularioDAOImpl;
import model.AvaliacaoRespondida;
import model.Formulario;
import model.Usuario;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {

    private FormularioDAO formularioDAO;
    private AvaliacaoRespondidaDAO avaliacaoRespondidaDAO;

    @Override
    public void init() {
        this.formularioDAO = new FormularioDAOImpl();
        this.avaliacaoRespondidaDAO = new AvaliacaoRespondidaDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // (Segurança extra) Se não há sessão, volta ao login
        if (session == null || session.getAttribute("usuarioLogado") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
        String perfil = usuarioLogado.getPerfil().getNome();

        // Roteador: Decide para qual página enviar com base no perfil
        if ("ALUNO".equals(perfil)) {

            // 1. Busca os pares [Formulario, Turma] PENDENTES
            List<Object[]> pendencias = formularioDAO.listarAvaliacoesPendentes(usuarioLogado.getId());

            // 2. Busca as avaliações JÁ RESPONDIDAS e editáveis (RF13)
            List<AvaliacaoRespondida> respondidas = avaliacaoRespondidaDAO.listarRespondidasEditaveis(usuarioLogado.getId());

            // 3. Envia AMBAS as listas para o JSP
            request.setAttribute("listaPendencias", pendencias);
            request.setAttribute("listaRespondidas", respondidas); // Nova lista

            RequestDispatcher dispatcher = request.getRequestDispatcher("/home_aluno.jsp");
            dispatcher.forward(request, response);

        } else {
            // Se for PROFESSOR, COORDENADOR ou ADMINISTRADOR,
            // encaminha para o dashboard administrativo
            RequestDispatcher dispatcher = request.getRequestDispatcher("/home_admin.jsp");
            dispatcher.forward(request, response);
        }
    }
}