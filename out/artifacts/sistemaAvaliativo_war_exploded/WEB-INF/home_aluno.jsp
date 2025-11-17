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

<p>Abaixo estão as avaliações disponíveis para você responder:</p>

<table>
    <thead>
    <tr>
        <th>Avaliação</th>
        <th>Período (Processo)</th>
        <th>Ação</th>
    </tr>
    </thead>
    <tbody>
    <%-- Implementação do RF12: Itera sobre a lista filtrada pelo DAO --%>
    <c:forEach var="form" items="${listaFormularios}">
        <tr>
            <td>${form.titulo}</td>
            <td>${form.processoAvaliativo.titulo}</td>
            <td>
                    <%-- Este link ainda não funciona, mas é o próximo passo --%>
                <a href="responder?formularioId=${form.id}">
                    Responder
                </a>
            </td>
        </tr>
    </c:forEach>

    <c:if test="${empty listaFormularios}">
        <tr>
            <td colspan="3" class="no-forms">
                Nenhuma avaliação disponível para você no momento.
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