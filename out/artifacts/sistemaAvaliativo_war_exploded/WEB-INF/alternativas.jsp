<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<html>
<head>
    <title>Gestão de Alternativas</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        h1, h2 { color: #333; }
        .breadcrumb { margin-bottom: 20px; }
        form { margin-bottom: 20px; padding: 10px; border: 1px solid #ccc; border-radius: 5px; max-width: 700px; }
        form div { margin-bottom: 10px; }
        form label { display: inline-block; width: 100px; }
        form input { padding: 8px; width: 250px; }
        table { border-collapse: collapse; width: 100%; }
        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
        th { background-color: #f2f2f2; }
    </style>
</head>
<body>

<div class="breadcrumb">
    <%-- Link para voltar à tela de questões (passando o ID do formulário pai da questão) --%>
    <a href="questoes?formularioId=${questao.formulario.id}">
        Voltar para Questões
    </a>
</div>

<h1>Gestão de Alternativas</h1>

<%-- Exibe o enunciado da questão que estamos editando --%>
<h2>Questão: <c:out value="${questao.enunciado}"/></h2>

<h2>Adicionar Nova Alternativa</h2>

<form action="alternativas" method="POST">

    <%-- CAMPO ESCONDIDO para enviar o ID da questão pai --%>
    <input type="hidden" name="questaoId" value="${questao.id}">

    <div>
        <label for="texto">Texto da Alternativa:</label>
        <input type="text" id="texto" name="texto" required style="width: 400px;">
    </div>

    <div>
        <%-- Implementação do RF17 --%>
        <label for="peso">Peso:</label>
        <input type="number" id="peso" name="peso" value="1.0" step="0.1" required>
    </div>

    <button type="submit">Salvar Alternativa</button>
</form>

<hr>

<h2>Alternativas Cadastradas</h2>

<table>
    <thead>
    <tr>
        <th>ID</th>
        <th>Texto</th>
        <th>Peso</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="alt" items="${listaAlternativas}">
        <tr>
            <td>${alt.id}</td>
            <td><c:out value="${alt.texto}"/></td>
            <td>${alt.peso}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>

</body>
</html>