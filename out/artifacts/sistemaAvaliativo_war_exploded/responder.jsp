<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%-- Importa a biblioteca de funções JSTL (para 'contains') --%>
<%@ taglib uri="jakarta.tags.functions" prefix="fn" %>

<html>
<head>
    <title>Responder Avaliação</title>
    <style>

        body { font-family: Arial, sans-serif; margin: 20px; background-color: #f9f9f9; }

        h1, h2 { color: #333; }

        .breadcrumb { margin-bottom: 20px; }

        .form-container { max-width: 800px; margin: auto; background-color: #fff; padding: 20px; border-radius: 8px; box-shadow: 0 0 10px rgba(0,0,0,0.1); }

        .questao-block { margin-bottom: 25px; padding-bottom: 15px; border-bottom: 1px solid #eee; }

        .questao-enunciado { font-size: 1.1em; font-weight: bold; color: #444; }

        .questao-obrigatoria { color: red; font-size: 0.9em; margin-left: 5px; }

        .alternativa-label { display: block; margin: 5px 0 5px 20px; }

        .alternativa-label input { margin-right: 10px; }

        textarea { width: 90%; min-height: 80px; padding: 8px; border: 1px solid #ccc; border-radius: 4px; }

        .submit-btn { padding: 10px 20px; background-color: #28a745; color: white; border: none; border-radius: 4px; cursor: pointer; font-size: 1.1em; }

        .submit-btn:hover { background-color: #218838; }

    </style>
</head>
<body>

<div class="breadcrumb">
    <a href="home">Voltar para Minhas Avaliações</a>
</div>

<div class="form-container">
    <h1><c:out value="${formulario.titulo}"/></h1>

    <c:if test="${not empty avaliacaoId}">
        <p>Editando suas respostas. (RF13)</p>
    </c:if>
    <c:if test="${empty avaliacaoId}">
        <p>Preencha o formulário abaixo. (RF13)</p>
    </c:if>

    <form action="responder" method="POST">

        <input type="hidden" name="formularioId" value="${formulario.id}">
        <input type="hidden" name="turmaId" value="${turmaId}">

        <%-- Se for edição, envia o ID da avaliação de volta --%>
        <c:if test="${not empty avaliacaoId}">
            <input type="hidden" name="avaliacaoId" value="${avaliacaoId}">
        </c:if>

        <c:forEach var="questao" items="${formulario.questoes}" varStatus="loop">
            <div class="questao-block">
                <div class="questao-enunciado">
                        ${loop.count}. <c:out value="${questao.enunciado}"/>
                    <c:if test="${questao.obrigatoria}">
                        <span class="questao-obrigatoria">* (Obrigatória)</span>
                    </c:if>
                </div>

                <c:set var="inputName" value="q_${questao.id}"/>
                <c:set var="required" value="${questao.obrigatoria ? 'required' : ''}"/>

                    <%-- --- LÓGICA DE PRÉ-PREENCHIMENTO (RF13) --- --%>

                <c:if test="${questao.tipo.name() == 'TEXTO'}">
                    <%-- Pré-preenche o textarea com a resposta do mapa 'respostasTexto' --%>
                    <textarea name="${inputName}" ${required}><c:out value="${respostasTexto[questao.id]}"/></textarea>
                </c:if>

                <c:if test="${questao.tipo.name() == 'UNICA_ESCOLHA'}">
                    <c:forEach var="alt" items="${questao.alternativas}">
                        <label class="alternativa-label">
                                <%-- Marca 'checked' se o ID desta alternativa for igual ao salvo no mapa 'respostasUnica' --%>
                            <input type="radio" name="${inputName}" value="${alt.id}" ${required}
                                ${respostasUnica[questao.id] == alt.id ? 'checked' : ''}>
                            <c:out value="${alt.texto}"/>
                        </label>
                    </c:forEach>
                </c:if>

                <c:if test="${questao.tipo.name() == 'MULTIPLA_ESCOLHA'}">
                    <c:forEach var="alt" items="${questao.alternativas}">
                        <label class="alternativa-label">
                                <%-- Marca 'checked' se o ID desta alternativa ESTIVER NO CONJUNTO (Set) salvo no mapa 'respostasMulti' --%>
                            <input type="checkbox" name="${inputName}" value="${alt.id}"
                                ${fn:contains(respostasMulti[questao.id], alt.id) ? 'checked' : ''}>
                            <c:out value="${alt.texto}"/>
                        </label>
                    </c:forEach>
                </c:if>

            </div>
        </c:forEach>

        <button type="submit" class="submit-btn">
            <c:if test="${not empty avaliacaoId}">Atualizar Respostas</c:if>
            <c:if test="${empty avaliacaoId}">Enviar Respostas</c:if>
        </button>
    </form>
</div>

</body>
</html>