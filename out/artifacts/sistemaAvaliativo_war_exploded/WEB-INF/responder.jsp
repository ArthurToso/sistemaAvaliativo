<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

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
    <p>Preencha o formulário abaixo. (RF13)</p>

    <form action="responder" method="POST">

        <%-- Campos escondidos para enviar o contexto de volta ao POST --%>
        <input type="hidden" name="formularioId" value="${formulario.id}">
        <input type="hidden" name="turmaId" value="${turmaId}">

        <%-- Loop principal: itera sobre cada Questão do Formulário --%>
            <c:forEach var="questao" items="${formulario.questoes}" varStatus="loop">

                <div class="questao-block">
                    <div class="questao-enunciado">
                            ${loop.count}. <c:out value="${questao.enunciado}"/>
                        <c:if test="${questao.obrigatoria}">
                            <span class="questao-obrigatoria">* (Obrigatória)</span>
                        </c:if>
                    </div>

                        <%-- Define o 'name' do input para ser "q_ID_DA_QUESTAO" --%>
                    <c:set var="inputName" value="q_${questao.id}"/>
                    <c:set var="required" value="${questao.obrigatoria ? 'required' : ''}"/>


                        <%-- --- LÓGICA DE EXIBIÇÃO CORRIGIDA --- --%>

                        <%-- RF09: Se for Questão de TEXTO (Compara o Enum.name() com a String) --%>
                    <c:if test="${questao.tipo.name() == 'TEXTO'}">
                        <textarea name="${inputName}" ${required}></textarea>
                    </c:if>

                        <%-- RF08: Se for Questão de RESPOSTA ÚNICA --%>
                    <c:if test="${questao.tipo.name() == 'UNICA_ESCOLHA'}">
                        <c:forEach var="alt" items="${questao.alternativas}">
                            <label class="alternativa-label">
                                <input type="radio" name="${inputName}" value="${alt.id}" ${required}>
                                <c:out value="${alt.texto}"/>
                            </label>
                        </c:forEach>
                    </c:if>

                        <%-- RF08: Se for Questão de MÚLTIPLA RESPOSTA --%>
                    <c:if test="${questao.tipo.name() == 'MULTIPLA_ESCOLHA'}">
                        <c:forEach var="alt" items="${questao.alternativas}">
                            <label class="alternativa-label">
                                    <%-- Corrigido para "checkbox" e usa o mesmo 'inputName' --%>
                                <input type="checkbox" name="${inputName}" value="${alt.id}">
                                <c:out value="${alt.texto}"/>
                            </label>
                        </c:forEach>
                    </c:if>

            </div>
        </c:forEach>

        <button type="submit" class="submit-btn">Enviar Respostas</button>

    </form>
</div>

</body>
</html>