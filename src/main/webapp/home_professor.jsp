<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<html>
<head>
    <title>Área do Professor</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        h1 { color: #333; }
        table { border-collapse: collapse; width: 100%; margin-top: 15px; }
        th, td { border: 1px solid #ddd; padding: 10px; text-align: left; }
        th { background-color: #f2f2f2; }
        .btn-relatorio {
            background-color: #007bff; color: white; padding: 5px 10px;
            text-decoration: none; border-radius: 4px; font-size: 0.9em;
        }
        .btn-relatorio:hover { background-color: #0056b3; }
    </style>
</head>
<body>

<h1>Área do Professor</h1>
<h3>Bem-vindo, Prof. ${sessionScope.usuarioLogado.nome}</h3>

<p>Abaixo estão as suas turmas e os relatórios de avaliação disponíveis (RF19):</p>

<table>
    <thead>
    <tr>
        <th>Turma</th>
        <th>Disciplina</th>
        <th>Semestre</th>
        <th>Processos Avaliativos (Relatórios)</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="turma" items="${minhasTurmas}">
        <tr>
            <td>${turma.codigo}</td>
            <td>${turma.unidadeCurricular.nome}</td>
            <td>${turma.semestre}</td>
            <td>
                    <%-- A Turma tem uma lista de Processos associados (RF05) --%>
                <c:forEach var="processo" items="${turma.processosAvaliativos}">
                    <div style="margin-bottom: 8px;">
                        <strong>${processo.titulo}:</strong><br/>
                            <%-- Para cada processo, lista os formulários --%>
                        <c:forEach var="form" items="${processo.formularios}">
                            <a href="relatorio/turma?formularioId=${form.id}&turmaId=${turma.id}"
                               class="btn-relatorio">
                                Ver Resultado: ${form.titulo}
                            </a>
                            <br/><br/>
                        </c:forEach>
                    </div>
                </c:forEach>

                <c:if test="${empty turma.processosAvaliativos}">
                    <span style="color:#999">Nenhum processo avaliativo.</span>
                </c:if>
            </td>
        </tr>
    </c:forEach>

    <c:if test="${empty minhasTurmas}">
        <tr><td colspan="4">Você não está vinculado a nenhuma turma.</td></tr>
    </c:if>
    </tbody>
</table>

<div style="margin-top: 20px;">
    <a href="logout">Sair (Logout)</a>
</div>

</body>
</html>