<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<html>
<head>
    <title>Dashboard Administrativo</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        h1 { color: #333; }
        nav ul { list-style-type: none; padding: 0; }
        nav li { margin: 10px 0; }
        nav a { text-decoration: none; font-size: 1.2em; color: #007bff; }
        nav a:hover { text-decoration: underline; }
    </style>
</head>
<body>

<h1>Dashboard Administrativo</h1>

<%-- Pega o nome do usuário da sessão --%>
<h3>Bem-vindo(a), ${sessionScope.usuarioLogado.nome} (${sessionScope.usuarioLogado.perfil.nome})</h3>

<nav>
    <ul>
        <li><a href="cursos">Gerenciar Cursos</a></li>
        <li><a href="unidades">Gerenciar Unidades Curriculares</a></li>
        <li><a href="usuarios">Gerenciar Usuários</a></li>
        <li><a href="turmas">Gerenciar Turmas</a></li>
        <li><a href="processos">Gerenciar Processos Avaliativos</a></li>
        <li><a href="formularios">Gerenciar Formulários</a></li>
        <li><a href="perfis">Gerenciar Perfis</a></li>
        <hr>
        <li><a href="logout">Sair (Logout)</a></li>
    </ul>
</nav>

</body>
</html>