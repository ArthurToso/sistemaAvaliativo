# Sistema de Avaliação Institucional (Java Web)

Este projeto foi desenvolvido como requisito para a disciplina de **Linguagem de Programação Orientada a Objetos 2**. Trata-se de um sistema web completo para gestão e aplicação de avaliações institucionais, permitindo que alunos avaliem turmas e professores, e que gestores visualizem relatórios consolidados.

## 🚀 Tecnologias Utilizadas

O projeto foi construído seguindo a arquitetura **MVC (Model-View-Controller)** e utilizando as seguintes tecnologias:

* **Linguagem:** Java 17
* **Web:** Servlets, JSP, JSTL
* **Persistência:** JPA (Java Persistence API) com implementação **Hibernate**
* **Banco de Dados:** MySQL 8
* **Servidor de Aplicação:** Apache Tomcat 10.1+
* **Gerenciamento de Dependências:** Maven

## ⚙️ Pré-requisitos para Rodar

Para executar este projeto localmente, você precisará de:

1.  **JDK 17** ou superior instalado.
2.  **Apache Tomcat 10.1** (ou superior). 
3.  **MySQL Server** rodando na porta 3306.

## 🛠️ Configuração e Instalação

### 1. Banco de Dados
Antes de iniciar a aplicação, prepare o banco de dados:

1.  Abra seu cliente MySQL (Workbench, DBeaver, etc.).
2.  Crie o schema vazio:
    ```sql
    CREATE DATABASE sistema_avaliativo CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
    ```
3.  **(Opcional)** Se quiser popular o banco com dados de teste, execute o script `script_banco_dados.sql` localizado na raiz do projeto.

### 2. Configuração da Aplicação
Verifique o arquivo de configuração de persistência:
* Local: `src/main/resources/META-INF/persistence.xml`
* Certifique-se de que o **usuário** e **senha** (`javax.persistence.jdbc.user` e `password`) correspondem às credenciais do seu MySQL local.
* A propriedade `hibernate.hbm2ddl.auto` está configurada como `update` para criar/atualizar as tabelas automaticamente.

### 3. Executando no IntelliJ
1.  Abra o projeto no IntelliJ.
2.  Aguarde o Maven baixar as dependências.
3.  Configure o **Run/Debug Configuration** apontando para o seu **Tomcat 10 Local**.
4.  Na aba "Deployment", certifique-se de adicionar o artefato `sistemaAvaliativo:war exploded`.
5.  Execute o servidor.
6.  Acesse no navegador: `http://localhost:8080/sistemaAvaliativo_war_exploded/`

## ✅ Funcionalidades Implementadas

O sistema atende aos seguintes Requisitos Funcionais:

### 🔐 Acesso e Segurança
- [x] **RF01/RF02:** Autenticação e perfis de usuário (Aluno, Professor, Admin).
- [x] **Filtro de Segurança:** Controle de acesso via `AutenticacaoFilter`, impedindo acesso direto a páginas restritas sem login.

### 📚 Gestão Acadêmica (Admin)
- [x] **RF04:** CRUD de Cursos e Unidades Curriculares.
- [x] **RF05/RF06:** Gestão de Turmas (vinculando Cursos, Disciplinas, Professores e Alunos) e Processos Avaliativos.

### 📝 Criação de Avaliações (Admin)
- [x] **RF07:** Criação de Formulários vinculados a Processos e Perfis.
- [x] **RF08/RF09:** Suporte a questões de Múltipla Escolha (Única/Múltipla) e Texto Livre.
- [x] **RF10:** Configuração de obrigatoriedade por questão.
- [x] **RF11:** Configuração de formulários Identificados ou Anônimos.

### 🎓 Área do Aluno
- [x] **RF12:** Dashboard personalizado exibindo apenas avaliações das turmas em que o aluno está matriculado.
- [x] **RF13:** Bloqueio de respostas duplicadas e permissão de edição de respostas já enviadas.

### 📊 Relatórios (Professor/Admin)
- [x] **RF16/RF19:** Relatórios consolidados por turma.
- [x] **RF17:** Cálculo automático de percentuais e Score ponderado das questões.
- [x] **RF20:** Visualização de dados brutos (respeitando o anonimato quando configurado).

## 👤 Credenciais de Teste (Sugestão)

Se você utilizou o script de dados fornecido, pode testar com:

* **Admin:** `arthur@gmail.com` / `1234`
* **Professor:** `ArthurProfessor` / `1234`
* **Aluno:** `ArthurAluno` / `1234`

---
Desenvolvido por **Arthur Borges Toso**.