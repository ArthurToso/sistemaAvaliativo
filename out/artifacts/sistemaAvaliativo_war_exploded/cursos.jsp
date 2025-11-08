<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%-- Para Jakarta (Tomcat 10+), use a linha abaixo em vez da de cima --%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<html>
<head>
    <title>Gestão de Cursos</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        h1, h2 { color: #333; }
        form { margin-bottom: 20px; padding: 10px; border: 1px solid #ccc; border-radius: 5px; }
        table { border-collapse: collapse; width: 300px; }
        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
        th { background-color: #f2f2f2; }
    </style>
</head>
<body>

    <h1>Gestão de Cursos</h1>

    <h2>Cadastrar Novo Curso</h2>

    <form action="cursos" method="POST">
        <label for="nome">Nome do Curso:</label>
        <input type="text" id="nome" name="nome" required>
        <button type="submit">Salvar</button>
    </form>

    <hr>

    <h2>Cursos Cadastrados</h2>

    <table>
        <thead>
            <tr>
                <th>ID</th>
                <th>Nome</th>
            </tr>
        </thead>
        <tbody>
            <%--
              - Aqui usamos JSTL para percorrer a lista 'listaCursos'
              - que foi enviada pelo Servlet (no request.setAttribute)
            --%>
            <c:forEach var="curso" items="${listaCursos}">
                <tr>
                    <td>${curso.id}</td>
                    <td>${curso.nome}</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>

</body>
</html>