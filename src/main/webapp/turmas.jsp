<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<html>
<head>
    <title>Gestão de Turmas</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        h1, h2 { color: #333; }
        form { margin-bottom: 20px; padding: 10px; border: 1px solid #ccc; border-radius: 5px; max-width: 600px; }
        form div { margin-bottom: 10px; }
        form label { display: block; font-weight: bold; margin-bottom: 5px; }
        form input, form select { padding: 8px; width: 100%; box-sizing: border-box; }
        form select[multiple] { height: 120px; }
        table { border-collapse: collapse; width: 100%; }
        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
        th { background-color: #f2f2f2; }
    </style>
</head>
<body>

<h1>Gestão de Turmas</h1>

<h2>Cadastrar Nova Turma</h2>

<form action="turmas" method="POST">
    <div>
        <label for="codigo">Código da Turma:</label>
        <input type="text" id="codigo" name="codigo" required>
    </div>
    <div>
        <label for="semestre">Semestre:</label>
        <input type="text" id="semestre" name="semestre" required placeholder="Ex: 2025.1">
    </div>
    <div>
        <label for="cursoId">Curso:</label>
        <select id="cursoId" name="cursoId" required>
            <option value="">Selecione o curso...</option>
            <c:forEach var="curso" items="${listaCursos}">
                <option value="${curso.id}">${curso.nome}</option>
            </c:forEach>
        </select>
    </div>
    <div>
        <label for="unidadeCurricularId">Unidade Curricular (Disciplina):</label>
        <select id="unidadeCurricularId" name="unidadeCurricularId" required>
            <option value="">Selecione a unidade...</option>
            <c:forEach var="uc" items="${listaUCs}">
                <option value="${uc.id}">${uc.nome}</option>
            </c:forEach>
        </select>
    </div>
    <div>
        <label for="alunoIds">Alunos (Segure Ctrl/Cmd para selecionar vários):</label>
        <select id="alunoIds" name="alunoIds" multiple required>
            <c:forEach var="aluno" items="${listaAlunos}">
                <option value="${aluno.id}">${aluno.nome}</option>
            </c:forEach>
        </select>
    </div>
    <div>
        <label for="professorIds">Professores (Segure Ctrl/Cmd para selecionar vários):</label>
        <select id="professorIds" name="professorIds" multiple required>
            <c:forEach var="prof" items="${listaProfessores}">
                <option value="${prof.id}">${prof.nome}</option>
            </c:forEach>
        </select>
    </div>
    <button type="submit">Salvar Turma</button>
</form>

<hr>

<h2>Turmas Cadastradas</h2>

<table>
    <thead>
    <tr>
        <th>ID</th>
        <th>Código</th>
        <th>Semestre</th>
        <th>Curso</th>
        <th>Unidade Curricular</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="turma" items="${listaTurmas}">
        <tr>
            <td>${turma.id}</td>
            <td>${turma.codigo}</td>
            <td>${turma.semestre}</td>
            <td>${turma.curso.nome}</td>
            <td>${turma.unidadeCurricular.nome}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>

</body>
</html>