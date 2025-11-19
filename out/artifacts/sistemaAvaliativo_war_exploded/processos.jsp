<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<html>
<head>
    <title>Gestão de Processos Avaliativos</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        h1, h2 { color: #333; }
        form { margin-bottom: 20px; padding: 10px; border: 1px solid #ccc; border-radius: 5px; max-width: 500px; }
        form div { margin-bottom: 10px; }
        form label { display: block; font-weight: bold; margin-bottom: 5px; } /* Mudei label para block */
        form input, form select { padding: 8px; width: 100%; box-sizing: border-box; } /* Mudei estilo */
        form select[multiple] { height: 150px; } /* Mudei estilo */
        table { border-collapse: collapse; width: 100%; }
        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
        th { background-color: #f2f2f2; }
    </style>
</head>
<body>

<a href="home">Voltar para página inicial</a>

<h1>Gestão de Processos Avaliativos</h1>

<h2>Cadastrar Novo Processo</h2>

<form action="processos" method="POST">
    <div>
        <label for="titulo">Título:</label>
        <input type="text" id="titulo" name="titulo" required>
    </div>
    <div>
        <label for="dataInicio">Data de Início:</label>
        <input type="date" id="dataInicio" name="dataInicio" required>
    </div>
    <div>
        <label for="dataFim">Data de Fim:</label>
        <input type="date" id="dataFim" name="dataFim" required>
    </div>

    <div>
        <label for="turmaIds">Turmas (Segure Ctrl para selecionar várias):</label>
        <select id="turmaIds" name="turmaIds" multiple required>
            <c:forEach var="turma" items="${listaTurmas}">
                <%-- Mostra o código da turma, nome do curso e nome da disciplina --%>
                <option value="${turma.id}">
                        ${turma.codigo} - ${turma.curso.nome} - ${turma.unidadeCurricular.nome}
                </option>
            </c:forEach>
        </select>
    </div>
    <button type="submit">Salvar</button>
</form>

<hr>

<h2>Processos Cadastrados</h2>

<table>
    <thead>
    <tr>
        <th>ID</th>
        <th>Título</th>
        <th>Data de Início</th>
        <th>Data de Fim</th>
        <th>Turmas Associadas</th> </tr>
    </thead>
    <tbody>
    <c:forEach var="processo" items="${listaProcessos}">
        <tr>
            <td>${processo.id}</td>
            <td>${processo.titulo}</td>
            <td>${processo.dataInicio}</td>
            <td>${processo.dataFim}</td>
            <td>
                <c:forEach var="turma" items="${processo.turmas}">
                    ${turma.codigo} <br/>
                </c:forEach>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>

</body>
</html>