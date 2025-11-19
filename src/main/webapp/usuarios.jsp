<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<html>
<head>
    <title>Gestão de Usuários</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        h1, h2 { color: #333; }
        form { margin-bottom: 20px; padding: 10px; border: 1px solid #ccc; border-radius: 5px; max-width: 500px; }
        form div { margin-bottom: 10px; }
        form label { display: inline-block; width: 100px; }
        form input, form select { padding: 5px; width: 250px; }
        table { border-collapse: collapse; width: 100%; }
        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
        th { background-color: #f2f2f2; }
    </style>
</head>
<body>

<h1>Gestão de Usuários</h1>

<h2>Cadastrar Novo Usuário</h2>

<form action="usuarios" method="POST">
    <div>
        <label for="nome">Nome:</label>
        <input type="text" id="nome" name="nome" required>
    </div>
    <div>
        <label for="email">Email:</label>
        <input type="email" id="email" name="email" required>
    </div>
    <div>
        <label for="login">Login:</label>
        <input type="text" id="login" name="login" required>
    </div>
    <div>
        <label for="senha">Senha:</label>
        <input type="password" id="senha" name="senha" required>
    </div>
    <div>
        <label for="perfilId">Perfil:</label>
        <select id="perfilId" name="perfilId" required>
            <option value="">Selecione um perfil...</option>
            <c:forEach var="perfil" items="${listaPerfis}">
                <option value="${perfil.id}">${perfil.nome}</option>
            </c:forEach>
        </select>
    </div>
    <button type="submit">Salvar</button>
</form>

<hr>

<h2>Usuários Cadastrados</h2>

<table>
    <thead>
    <tr>
        <th>ID</th>
        <th>Nome</th>
        <th>Email</th>
        <th>Login</th>
        <th>Perfil</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="usuario" items="${listaUsuarios}">
        <tr>
            <td>${usuario.id}</td>
            <td>${usuario.nome}</td>
            <td>${usuario.email}</td>
            <td>${usuario.login}</td>
            <td>${usuario.perfil.nome}</td>

        </tr>
    </c:forEach>
    </tbody>
</table>

</body>
</html>