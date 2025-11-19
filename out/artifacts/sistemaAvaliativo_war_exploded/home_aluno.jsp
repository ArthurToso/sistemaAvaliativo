<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<html>
<head>
    <title>Minhas Avaliações</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        h1 { color: #333; }
        table { border-collapse: collapse; width: 100%; }
        th, td { border: 1px solid #ddd; padding: 12px; text-align: left; }
        th { background-color: #f2f2f2; }
        tr:nth-child(even) { background-color: #f9f9f9; }
        .logout { margin-top: 20px; }
        .no-forms { font-style: italic; color: #777; }
    </style>
</head>
<body>

<h1>Minhas Avaliações</h1>
<h3>Bem-vindo(a), ${sessionScope.usuarioLogado.nome}</h3>

<hr>
<h2>Avaliações Pendentes</h2>

<table>
    <thead>
    <tr>
        <th>Avaliação</th>
        <th>Processo</th>
        <th>Turma</th>
        <th>Ação</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="item" items="${listaPendencias}">
        <tr>
            <td>${item[0].titulo}</td>
            <td>${item[0].processoAvaliativo.titulo}</td>
            <td>${item[1].codigo}</td>
            <td>
                <a href="responder?formularioId=${item[0].id}&turmaId=${item[1].id}">
                    Responder
                </a>
            </td>
        </tr>
    </c:forEach>
    <c:if test="${empty listaPendencias}">
        <tr>
            <td colspan="4" class="no-forms">
                Nenhuma avaliação pendente.
            </td>
        </tr>
    </c:if>
    </tbody>
</table>

<hr>
<h2>Avaliações Respondidas</h2>

<table>
    <thead>
    <tr>
        <th>Avaliação</th>
        <th>Processo</th>
        <th>Turma</th>
        <th>Ação</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="resp" items="${listaRespondidas}">
        <tr>
            <td>${resp.formulario.titulo}</td>
            <td>${resp.formulario.processoAvaliativo.titulo}</td>
            <td>${resp.turma.codigo}</td>
            <td>
                <a href="responder?formularioId=${resp.formulario.id}&turmaId=${resp.turma.id}">
                    Editar Respostas
                </a>
            </td>
        </tr>
    </c:forEach>
    <c:if test="${empty listaRespondidas}">
        <tr>
            <td colspan="4" class="no-forms">
                Nenhuma avaliação respondida editável.
            </td>
        </tr>
    </c:if>
    </tbody>
</table>

<div class="logout">
    <a href="logout">Sair (Logout)</a>
</div>

</body>
</html>