<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %> <%-- Biblioteca para formatar números --%>

<html>
<head>
    <title>Relatório Consolidado</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; background-color: #f4f4f4; }
        .container { max-width: 900px; margin: auto; background: white; padding: 30px; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
        h1, h2, h3 { color: #333; }
        .header-info { margin-bottom: 30px; padding-bottom: 10px; border-bottom: 2px solid #007bff; }

        .questao-card { margin-bottom: 30px; border: 1px solid #ddd; padding: 15px; border-radius: 5px; page-break-inside: avoid; }
        .questao-titulo { font-weight: bold; font-size: 1.1em; margin-bottom: 10px; background-color: #f9f9f9; padding: 10px; }

        table { width: 100%; border-collapse: collapse; margin-top: 10px; }
        th, td { border: 1px solid #eee; padding: 8px; text-align: left; }
        th { background-color: #f2f2f2; }

        .progress-bar { background-color: #e9ecef; height: 20px; width: 100px; display: inline-block; border-radius: 4px; overflow: hidden; vertical-align: middle; }
        .progress-fill { background-color: #007bff; height: 100%; }

        .texto-resposta { font-style: italic; color: #555; border-left: 3px solid #ccc; padding-left: 10px; margin: 5px 0; }
        .score { float: right; font-weight: bold; color: #28a745; }
    </style>
</head>
<body>

<div class="container">
    <div class="header-info">
        <a href="${pageContext.request.contextPath}/home">Voltar ao Dashboard</a>
        <h1>Relatório de Avaliação (RF16)</h1>
        <p><strong>Formulário:</strong> ${formulario.titulo}</p>
        <p><strong>Turma:</strong> ${turma.codigo} - ${turma.unidadeCurricular.nome}</p>
        <p><strong>Professor(es):</strong>
            <c:forEach var="prof" items="${turma.professores}">${prof.nome}, </c:forEach>
        </p>
    </div>

    <%-- Itera sobre cada DTO de estatística --%>
    <c:forEach var="estat" items="${estatisticas}" varStatus="loop">

        <div class="questao-card">
            <div class="questao-titulo">
                Questão ${loop.count}: ${estat.questao.enunciado}
                <span style="font-size: 0.8em; font-weight: normal;">(Total de Respostas: ${estat.totalRespostas})</span>
            </div>

                <%-- Lógica para Questões Fechadas (Única ou Múltipla) --%>
            <c:if test="${estat.questao.tipo != 'TEXTO'}">
                <table>
                    <thead>
                    <tr>
                        <th>Alternativa</th>
                        <th>Peso</th>
                        <th>Votos</th>
                        <th>Percentual (RF17)</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:set var="somaPonderada" value="0" />

                    <c:forEach var="alt" items="${estat.questao.alternativas}">
                        <c:set var="votos" value="${estat.contagemAlternativas[alt.id] != null ? estat.contagemAlternativas[alt.id] : 0}" />

                        <%-- Cálculo do Percentual --%>
                        <c:set var="percentual" value="${estat.totalRespostas > 0 ? (votos * 100.0 / estat.totalRespostas) : 0}" />

                        <%-- Cálculo para o Score (Votos * Peso) --%>
                        <c:set var="somaPonderada" value="${somaPonderada + (votos * alt.peso)}" />

                        <tr>
                            <td>${alt.texto}</td>
                            <td>${alt.peso}</td>
                            <td>${votos}</td>
                            <td>
                                <div class="progress-bar">
                                    <div class="progress-fill" style="width: ${percentual}%"></div>
                                </div>
                                <fmt:formatNumber value="${percentual}" maxFractionDigits="1"/>%
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>

                <%-- Exibição do Score Final (RF17) --%>
                <div style="margin-top: 10px; text-align: right;">
                    <strong>Score da Questão (Média Ponderada): </strong>
                    <span class="score">
                        <c:choose>
                            <c:when test="${estat.totalRespostas > 0}">
                                <fmt:formatNumber value="${somaPonderada / estat.totalRespostas}" maxFractionDigits="2"/>
                            </c:when>
                            <c:otherwise>0.0</c:otherwise>
                        </c:choose>
                    </span>
                </div>
            </c:if>

                <%-- Lógica para Questões Abertas (Texto) --%>
            <c:if test="${estat.questao.tipo == 'TEXTO'}">
                <h4>Respostas Descritivas:</h4>
                <c:if test="${empty estat.respostasTexto}">
                    <p>Sem respostas textuais.</p>
                </c:if>
                <c:forEach var="entry" items="${estat.respostasTexto}">
                    <%-- RF14/RF11: O anonimato é garantido aqui pois não estamos buscando o nome do aluno no Map, apenas o texto --%>
                    <div class="texto-resposta">"${entry.value}"</div>
                </c:forEach>
            </c:if>

        </div>
    </c:forEach>
</div>

</body>
</html>