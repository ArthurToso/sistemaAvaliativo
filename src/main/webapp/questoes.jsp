<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<html>
<head>
    <title>Gestão de Questões</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        h1, h2 { color: #333; }
        .breadcrumb { margin-bottom: 20px; }
        form { margin-bottom: 20px; padding: 10px; border: 1px solid #ccc; border-radius: 5px; max-width: 700px; }
        form div { margin-bottom: 10px; }
        form label { display: block; font-weight: bold; margin-bottom: 5px; }
        form input[type="text"], form select, form textarea { padding: 8px; width: 100%; box-sizing: border-box; }
        form textarea { height: 80px; }
        form input[type="checkbox"] { width: auto; }
        table { border-collapse: collapse; width: 100%; }
        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
        th { background-color: #f2f2f2; }
    </style>
</head>
<body>

<div class="breadcrumb">
    <a href="formularios">Voltar para Formulários</a>
</div>

<h1>Gestão de Questões</h1>

<%-- Exibe o título do formulário que estamos editando --%>
<h2>Formulário: <c:out value="${formulario.titulo}"/></h2>

<h2>Adicionar Nova Questão</h2>

<form action="questoes" method="POST">

    <%-- CAMPO ESCONDIDO para enviar o ID do formulário pai de volta ao POST --%>
    <input type="hidden" name="formularioId" value="${formulario.id}">

    <div>
        <label for="enunciado">Enunciado:</label>
        <textarea id="enunciado" name="enunciado" required></textarea>
    </div>

    <div>
        <label for="tipo">Tipo da Questão (RF08, RF09):</label>
        <select id="tipo" name="tipo" required>
            <c:forEach var="tipoEnum" items="${tiposQuestao}">
                <option value="${tipoEnum}">${tipoEnum.name()}</option>
            </c:forEach>
        </select>
    </div>

    <div>
        <%-- Implementação do RF10 --%>
        <input type="checkbox" id="obrigatoria" name="obrigatoria" checked>
        <label for="obrigatoria" style="display: inline-block;">Questão Obrigatória</label>
    </div>

    <button type="submit">Salvar Questão</button>
</form>

<hr>

<h2>Questões Cadastradas</h2>

<table>
    <thead>
    <tr>
        <th>ID</th>
        <th>Enunciado</th>
        <th>Tipo</th>
        <th>Obrigatória</th>
        <th>Ações</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="q" items="${listaQuestoes}">
        <tr>
            <td>${q.id}</td>
            <td><c:out value="${q.enunciado}"/></td>
            <td>${q.tipo}</td>
            <td>${q.obrigatoria ? 'Sim' : 'Não'}</td>
            <td>
                    <%-- Se for múltipla escolha, permite add alternativas --%>
                <c:if test="${q.tipo == 'UNICA_ESCOLHA' || q.tipo == 'MULTIPLA_ESCOLHA'}">
                    <a href="alternativas?questaoId=${q.id}">
                        Gerenciar Alternativas
                    </a>
                </c:if>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>

</body>
</html>