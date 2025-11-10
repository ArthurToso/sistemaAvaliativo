<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<html>
<head>
    <title>Gestão de Unidades Curriculares</title>
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

<h1>Gestão de Unidades Curriculares (Disciplinas)</h1>

<h2>Cadastrar Nova Unidade</h2>

<form action="unidades" method="POST">
    <label for="nome">Nome da Unidade:</label>
    <input type="text" id="nome" name="nome" required>
    <button type="submit">Salvar</button>
</form>

<hr>

<h2>Unidades Cadastradas</h2>

<table>
    <thead>
    <tr>
        <th>ID</th>
        <th>Nome</th>
    </tr>
    </thead>
    <tbody>
    <%-- Itera sobre a lista 'listaUCs' enviada pelo Servlet --%>
    <c:forEach var="uc" items="${listaUCs}">
        <tr>
            <td>${uc.id}</td>
            <td>${uc.nome}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>

</body>
</html>