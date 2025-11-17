package filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Usuario;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Este filtro intercepta requisições para proteger páginas restritas.
 */
// O WebFilter mapeia quais URLs este filtro deve monitorar.
// "/*" significa TODAS as URLs da aplicação.
@WebFilter("/*")
public class AutenticacaoFilter implements Filter {

    // Lista de URLs que NÃO precisam de login (páginas públicas)
    private Set<String> urlsPermitidas = new HashSet<>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Adiciona as URLs públicas que qualquer um pode acessar
        urlsPermitidas.add("/login");
        urlsPermitidas.add("/logout");
        urlsPermitidas.add("/"); // Permite acesso à raiz (index.jsp)
        urlsPermitidas.add("/index.jsp");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession(false); // Pega a sessão, não cria uma nova

        // Pega o caminho da URL solicitada (ex: /login, /cursos)
        String caminho = request.getServletPath();

        // 1. Verifica se a URL é pública (está na lista de permitidas)
        boolean urlPermitida = false;
        for (String url : urlsPermitidas) {
            if (caminho.startsWith(url)) {
                urlPermitida = true;
                break;
            }
        }

        // 2. Verifica se o usuário está logado
        boolean logado = (session != null && session.getAttribute("usuarioLogado") != null);

        if (logado || urlPermitida) {
            // Se (está logado) OU (a página é pública), deixa a requisição passar.
            filterChain.doFilter(request, response);
        } else {
            // Se (não está logado) E (a página NÃO é pública), redireciona para o login.
            response.sendRedirect(request.getContextPath() + "/login");
        }
    }

    @Override
    public void destroy() {}
}