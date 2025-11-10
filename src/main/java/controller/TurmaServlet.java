package controller;

import dao.CursoDAO;
import dao.TurmaDAO;
import dao.UnidadeCurricularDAO;
import dao.UsuarioDAO;
import dao.impl.CursoDAOImpl;
import dao.impl.TurmaDAOImpl;
import dao.impl.UnidadeCurricularDAOImpl;
import dao.impl.UsuarioDAOImpl;
import model.Curso;
import model.Turma;
import model.UnidadeCurricular;
import model.Usuario;

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

@WebServlet("/turmas")
public class TurmaServlet extends HttpServlet {

    private TurmaDAO turmaDAO;
    private CursoDAO cursoDAO;
    private UnidadeCurricularDAO unidadeCurricularDAO;
    private UsuarioDAO usuarioDAO;

    @Override
    public void init() {
        // Instancia todos os DAOs que vamos precisar
        this.turmaDAO = new TurmaDAOImpl();
        this.cursoDAO = new CursoDAOImpl();
        this.unidadeCurricularDAO = new UnidadeCurricularDAOImpl();
        this.usuarioDAO = new UsuarioDAOImpl();
    }

    /**
     * Chamado quando a página é carregada (GET).
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Busca a lista de TURMAS (para a tabela principal)
        List<Turma> listaTurmas = turmaDAO.listarTodos();

        // 2. Busca as listas para preencher os formulários
        List<Curso> listaCursos = cursoDAO.listarTodos();
        List<UnidadeCurricular> listaUCs = unidadeCurricularDAO.listarTodos();

        // 3. Usa o novo método para buscar Alunos e Professores
        List<Usuario> listaAlunos = usuarioDAO.listarPorPerfil("ALUNO");
        List<Usuario> listaProfessores = usuarioDAO.listarPorPerfil("PROFESSOR");


        // 4. Coloca TODAS as listas no objeto 'request'
        request.setAttribute("listaTurmas", listaTurmas);
        request.setAttribute("listaCursos", listaCursos);
        request.setAttribute("listaUCs", listaUCs);
        request.setAttribute("listaAlunos", listaAlunos);
        request.setAttribute("listaProfessores", listaProfessores);

        // 5. Encaminha a requisição para o arquivo JSP
        RequestDispatcher dispatcher = request.getRequestDispatcher("/turmas.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * Chamado quando o formulário é enviado (POST).
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Pega os dados simples do formulário
        String codigo = request.getParameter("codigo");
        String semestre = request.getParameter("semestre");

        // 2. Pega os IDs das entidades relacionadas
        Long cursoId = Long.parseLong(request.getParameter("cursoId"));
        Long unidadeCurricularId = Long.parseLong(request.getParameter("unidadeCurricularId"));

        // 3. Pega os ARRAYS de IDs dos alunos e professores
        String[] alunoIds = request.getParameterValues("alunoIds");
        String[] professorIds = request.getParameterValues("professorIds");

        // 4. Busca os objetos completos do banco
        Curso curso = cursoDAO.buscarPorId(cursoId);
        UnidadeCurricular uc = unidadeCurricularDAO.buscarPorId(unidadeCurricularId);

        // 5. Monta o Set<Usuario> de Alunos
        Set<Usuario> alunos = new HashSet<>();
        if (alunoIds != null) {
            for (String id : alunoIds) {
                Usuario aluno = usuarioDAO.buscarPorId(Long.parseLong(id));
                if (aluno != null) {
                    alunos.add(aluno);
                }
            }
        }

        // 6. Monta o Set<Usuario> de Professores
        Set<Usuario> professores = new HashSet<>();
        if (professorIds != null) {
            for (String id : professorIds) {
                Usuario professor = usuarioDAO.buscarPorId(Long.parseLong(id));
                if (professor != null) {
                    professores.add(professor);
                }
            }
        }

        // 7. Cria a nova Turma e seta todos os dados
        Turma novaTurma = new Turma();
        novaTurma.setCodigo(codigo);
        novaTurma.setSemestre(semestre);
        novaTurma.setCurso(curso);
        novaTurma.setUnidadeCurricular(uc);
        novaTurma.setAlunos(alunos);
        novaTurma.setProfessores(professores);

        // 8. Salva a nova turma (o 'merge' no DAO cuida das associações)
        turmaDAO.salvar(novaTurma);

        // 9. Redireciona de volta para o próprio servlet (padrão PRG)
        response.sendRedirect(request.getContextPath() + "/turmas");
    }
}