<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<html>
<head>
    <title>Gestão de Formulários</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        h1, h2 { color: #333; }
        form { margin-bottom: 20px; padding: 10px; border: 1px solid #ccc; border-radius: 5px; max-width: 600px; }
        form div { margin-bottom: 10px; }
        form label { display: block; font-weight: bold; margin-bottom: 5px; }
        form input, form select { padding: 8px; width: 100%; box-sizing: border-box; }
        form select[multiple] { height: 100px; }
        table { border-collapse: collapse; width: 100%; }
        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
        th { background-color: #f2f2f2; }
    </style>
</head>
<body>

<a href="home">Voltar para página inicial</a>

<h1>Gestão de Formulários</h1>

<h2>Cadastrar Novo Formulário</h2>

<form action="formularios" method="POST">
    <div>
        <label for="titulo">Título do Formulário:</label>
        <input type="text" id="titulo" name="titulo" required>
    </div>

    <div>
        <label for="processoId">Processo Avaliativo (Período):</label>
        <select id="processoId" name="processoId" required>
            <option value="">Selecione o período...</option>
            <c:forEach var="processo" items="${listaProcessos}">
                <option value="${processo.id}">${processo.titulo}</option>
            </c:forEach>
        </select>
    </div>

    <div>
        <label for="tipo">Tipo de Formulário:</label>
        <select id="tipo" name="tipo" required>
            <c:forEach var="tipoEnum" items="${tiposFormulario}">
                <option value="${tipoEnum}">${tipoEnum == 'IDENTIFICADO' ? 'Identificado (associa aluno)' : 'Anônimo (não associa aluno)'}</option>
            </c:forEach>
        </select>
    </div>

    <div>
        <label for="perfilIds">Destinado aos Perfis (Segure Ctrl para selecionar vários):</label>
        <select id="perfilIds" name="perfilIds" multiple required>
            <c:forEach var="perfil" items="${listaPerfis}">
                <option value="${perfil.id}">${perfil.nome}</option>
            </c:forEach>
        </select>
    </div>

    <button type="submit">Salvar Formulário</button>
</form>

<hr>

<h2>Formulários Cadastrados</h2>

<table>
    <thead>
    <tr>
        <th>ID</th>
        <th>Título</th>
        <th>Processo Avaliativo</th>
        <th>Tipo</th>
        <th>Ações</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="form" items="${listaFormularios}">
        <tr>
            <td>${form.id}</td>
            <td>${form.titulo}</td>
            <td>${form.processoAvaliativo.titulo}</td>
            <td>${form.tipo}</td>
            <td>
                <a href="questoes?formularioId=${form.id}">
                    Gerenciar Questões
                </a>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>

</body>
</html>