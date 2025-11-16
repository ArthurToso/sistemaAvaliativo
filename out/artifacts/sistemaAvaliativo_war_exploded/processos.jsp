<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<html>
<head>
    <title>Gestão de Processos Avaliativos</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        h1, h2 { color: #333; }
        form { margin-bottom: 20px; padding: 10px; border: 1px solid #ccc; border-radius: 5px; max-width: 500px; }
        form div { margin-bottom: 10px; }
        form label { display: inline-block; width: 100px; }
        form input { padding: 5px; width: 250px; }
        table { border-collapse: collapse; width: 100%; }
        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
        th { background-color: #f2f2f2; }
    </style>
</head>
<body>

<h1>Gestão de Processos Avaliativos</h1>

<h2>Cadastrar Novo Processo</h2>

<form action="processos" method="POST">
    <div>
        <label for="titulo">Título:</label>
        <input type="text" id="titulo" name="titulo" required>
    </div>
    <div>
        <label for="dataInicio">Data de Início:</label>
        <input type="date" id="dataInicio" name="dataInicio" required>
    </div>
    <div>
        <label for="dataFim">Data de Fim:</label>
        <input type="date" id="dataFim" name="dataFim" required>
    </div>
    <button type="submit">Salvar</button>
</form>

<hr>

<h2>Processos Cadastrados</h2>

<table>
    <thead>
    <tr>
        <th>ID</th>
        <th>Título</th>
        <th>Data de Início</th>
        <th>Data de Fim</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="processo" items="${listaProcessos}">
        <tr>
            <td>${processo.id}</td>
            <td>${processo.titulo}</td>
            <td>${processo.dataInicio}</td>
            <td>${processo.dataFim}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>

</body>
</html>