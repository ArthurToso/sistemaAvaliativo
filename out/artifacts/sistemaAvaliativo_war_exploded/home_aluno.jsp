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
        <th>Turma</th>
        <th>Ação</th>
    </tr>
    </thead>
    <tbody>
    <%-- Implementação do RF12: Itera sobre a lista filtrada pelo DAO --%>
    <<c:forEach var="item" items="${listaPendencias}">
        <%--
           item[0] é o objeto Formulario
           item[1] é o objeto Turma
        --%>
        <tr>
            <td>${item[0].titulo}</td>
            <td>${item[0].processoAvaliativo.titulo}</td>
            <td>${item[1].codigo}</td> <%-- 3. EXIBA A TURMA --%>
            <td>
                    <%-- 4. CONSTRUA O LINK CORRETO com &turmaId=... --%>
                <a href="responder?formularioId=${item[0].id}&turmaId=${item[1].id}">
                    Responder
                </a>
            </td>
        </tr>
    </c:forEach>

    <c:if test="${empty listaPendencias}">
        <tr>
            <td colspan="4" class="no-forms">
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