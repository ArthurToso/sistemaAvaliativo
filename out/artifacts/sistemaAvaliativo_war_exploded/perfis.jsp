<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<html>
<head>
    <title>Gestão de Perfis de Usuário</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        h1, h2 { color: #333; }
        form { margin-bottom: 20px; padding: 10px; border: 1px solid #ccc; border-radius: 5px; }
        table { border-collapse: collapse; width: 400px; }
        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
        th { background-color: #f2f2f2; }
    </style>
</head>
<body>

<h1>Gestão de Perfis (RF01)</h1>

<h2>Cadastrar Novo Perfil</h2>

<form action="perfis" method="POST">
    <label for="nome">Nome do Perfil:</label>
    <input type="text" id="nome" name="nome" required>
    <button type="submit">Salvar</button>
</form>

<hr>

<h2>Perfis Cadastrados</h2>

<table>
    <thead>
    <tr>
        <th>ID</th>
        <th>Nome</th>
        <%-- Ações (Delete/Edit) podem ser adicionadas aqui depois --%>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="perfil" items="${listaPerfis}">
        <tr>
            <td>${perfil.id}</td>
            <td>${perfil.nome}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>

</body>
</html>