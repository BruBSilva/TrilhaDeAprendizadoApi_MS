<p align="center" width="100%">
    <img src="https://github.com/vitoriasilva13/front/blob/main/src/assets/logomarca.jpg" alt="Corvis Logo" width="300"/>
</p>

# Corvis - Trilha de Aprendizagem 📚

**Corvis** é um sistema de trilhas de aprendizagem desenvolvido para auxiliar na gestão de trilhas de conhecimento. O sistema permite que usuários se inscrevam em trilhas, as vizualizem e gerenciem seu progresso.

---

## 📑 Índice

- [Tecnologias Utilizadas](#-tecnologias-utilizadas)
- [Estrutura do Projeto](#-estrutura-do-projeto)
- [Funcionalidades Principais](#-funcionalidades-principais)
- [Instruções de Uso](#-instruções-de-uso)
  - [Clonando o repositório](#1-clonando-o-repositório)
  - [Configuração do Banco de Dados](#2-configuração-do-banco-de-dados)
  - [Executando o Back-end](#3-executando-o-back-end)
  - [Criando o usuário principal](#4-criando-o-usuario-principal)
  - [Executando o Front-end](#5-executando-front)

---

## 🚀 Tecnologias Utilizadas

### Front-end
- **React**
- **JavaScript**
- **Tailwind CSS**

### Back-end
- **Java**
- **Spring Boot**
- **JPA / Hibernate**
- **JWT**

### Banco de Dados
- **PostgreSQL**

---

## 📂 Estrutura do Projeto

Fluxograma que representa a estrutura do projeto e o fluxo de dados entre os componentes:

<img src="Diagrams/fluxograma.jpg" alt="Fluxograma do Projeto"/>

Modelagem de classes utilizada no sistema:

<img src="Diagrams/classe.png" alt="Diagrama de Classes"/>

---

## ✅ Funcionalidades principais

- Cadastro de trilhas de aprendizagem, módulos e conquistas
- Inscrição e acompanhamento de progresso por usuário
- API REST para gestão dos dados
- Interface web para visualização do progresso e trilhas disponíveis.

---

## ⚡ Instruções de Uso

### 1. **Clonando o repositório**
Primeiro, clone o repositório para sua máquina local:

```bash
    git clone --recurse-submodules https://github.com/BruBSilva/TrilhaDeAprendizadoApi_MS.git
    cd TrilhaDeAprendizadoApi_MS
```
---

### 2. **Configuração do Banco de Dados**
Certifique-se de ter o PostgreSQL instalado e configurado e crie os seguintes bancos de dados:

```sql
    CREATE DATABASE trilha-service;
    CREATE DATABASE learning-service;
    CREATE DATABASE user-service;
```

Crie as variáveis de ambiente necessárias para a conexão com o banco de dados. Você pode definir as variáveis no seu projeto ou nas variáveis de ambiente do sistema.

```
    DB_HOST=seu_host
    DB_PORT=sua_porta
    DB_PASSWORD=sua_senha
```

Essas variáveis serão utilizadas nos arquivos de configuração do Spring Boot.

```properties
    spring.datasource.url=jdbc:postgresql://${DB_HOST}:${DB_PORT}/user-service
    spring.datasource.username=postgres
    spring.datasource.password=${DB_PASSWORD}
```

### 3. **Executando o Back-end**

Execute todos os projetos exceto o `front` e verifique se as tabelas foram criadas corretamente no banco de dados.

### 4. **Crie o usuário principal**

```sql
    INSERT INTO public.usuarios (id, nome, email, senha_hash, role) --hash da senha "123"
    VALUES (1, 'MasterAdm', 'admmaster@teste.com', '202cb962ac59075b964b07152d234b70', 'ADMIN');

    INSERT INTO public.administradores (id)
    VALUES (1);
```

### 5. **Executando Front**

Para rodar o front-end, acesse o repositório 'front' e siga as instruções especificadas no arquivo `README.md`:

https://github.com/vitoriasilva13/front/

---

## 🐦‍⬛ Corvis

Este projeto é guiado por nosso corvo sábio.






