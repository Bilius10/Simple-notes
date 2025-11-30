# SimpleNotes API 📝

![Linguagem](https://img.shields.io/badge/Java-17%2B-blue?style=for-the-badge&logo=java)
![Framework](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen?style=for-the-badge&logo=spring)
![Banco de Dados](https://img.shields.io/badge/PostgreSQL-darkblue?style=for-the-badge&logo=postgresql)
![Status](https://img.shields.io/badge/status-ativo-green?style=for-the-badge)

<br>

<p align="center">
  <strong>API REST para gerenciamento inteligente de notas, organização em carteiras (wallets) e colaboração social entre usuários.</strong>
</p>

---

### 📝 Índice

* [Sobre o Projeto](#sobre)
* [Funcionalidades](#funcionalidades)
* [Tecnologias e Arquitetura](#tecnologias)
* [Começando](#comecando)
* [Documentação da API](#docs)
* [Autores](#autores)

---

### <a id="sobre"></a>🧐 Sobre o Projeto

O **SimpleNotes API** é uma solução de back-end desenvolvida para facilitar a organização de informações pessoais e colaborativas. O sistema permite que usuários criem notas, agrupem-nas em "Carteiras" (Wallets) e compartilhem o acesso a essas carteiras com outros usuários, definindo permissões específicas. Além disso, a plataforma possui um módulo social completo, permitindo solicitações de amizade e notificações em tempo real sobre interações.

---

### <a id="funcionalidades"></a>✨ Funcionalidades

A plataforma foi projetada com as seguintes funcionalidades em mente:

* **Gestão de Carteiras (Wallets):** Criação de contêineres lógicos para organizar notas por contexto (ex: Trabalho, Estudos).
* **Colaboração em Tempo Real:** Associação de múltiplos usuários a uma mesma carteira com controle de permissões (Leitura, Escrita, Exclusão).
* **Notas Dinâmicas:** CRUD completo de notas vinculadas às carteiras.
* **Sistema Social:** Busca de perfis, envio e gerenciamento de solicitações de amizade (Aceitar/Recusar).
* **Segurança:** Autenticação via Token JWT, recuperação de senha por e-mail e confirmação de cadastro.
* **Notificações:** Sistema de alertas para avisar o usuários sobre novas interações ou convites.
* **Documentação Interativa:** API totalmente documentada com Swagger (OpenAPI 3).

---

### <a id="tecnologias"></a>🛠️ Tecnologias e Arquitetura

A API foi construída com um conjunto de tecnologias modernas para garantir performance, segurança e escalabilidade.

| Tecnologia                 | Propósito                                                |
| :------------------------- | :------------------------------------------------------- |
| **Spring Boot** | Framework principal para construção da API REST.         |
| **Java 17+** | Linguagem de programação base.                           |
| **PostgreSQL** | Banco de dados relacional para persistência dos dados.   |
| **Spring Security & JWT** | Controle de autenticação e autorização stateless.        |
| **Spring Data JPA** | Camada de persistência e ORM.                            |
| **Spring Mail** | Envio de e-mails para recuperação de senha/confirmação.  |
| **Flyway** | (Opcional) Versionamento e migração do banco de dados.   |
| **SpringDoc (OpenAPI 3)** | Documentação automática da API.                          |

A arquitetura do projeto segue os padrões RESTful, garantindo uma separação clara de responsabilidades e fácil integração com front-ends.

---

### <a id="comecando"></a>🚀 Começando

Para executar este projeto localmente, siga os passos abaixo.

#### **Pré-requisitos**

* Java JDK 17 ou superior
* Maven 3.8+
* PostgreSQL

## 🔐 Autenticação

A maioria dos endpoints é protegida. Para acessá-los, você deve incluir o token JWT no cabeçalho da requisição.

**Header:** `Authorization`
**Value:** `Bearer <seu_token_aqui>`

---

## <a id="docs"></a>📚 Documentação da API

Abaixo está a lista resumida dos endpoints disponíveis. Para detalhes dos esquemas JSON, acesse o Swagger UI (geralmente em `/swagger-ui.html` ou `/swagger-ui/index.html` quando a aplicação estiver rodando).

### 👤 Autenticação (`/auth`)

| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| `POST` | `/auth/register` | Registra um novo usuário. |
| `POST` | `/auth/login` | Realiza login e retorna o Token JWT. |
| `POST` | `/auth/forgot-password` | Solicita recuperação de senha (envia e-mail). |
| `PATCH` | `/auth/reset-password` | Redefine a senha usando o token recebido. |
| `PATCH` | `/auth/confirm-email` | Confirma o e-mail do usuário. |

### 💼 Carteiras (Wallets) (`/wallet`)

| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| `POST` | `/wallet` | Cria uma nova carteira. |
| `GET` | `/wallet` | Lista as carteiras (paginado). |
| `GET` | `/wallet/{id}` | Busca detalhes de uma carteira específica. |
| `DELETE`| `/wallet/{id}` | Remove uma carteira. |

### 👥 Colaboradores da Carteira (`/wallet-user`)

Gerencie quem tem acesso às suas carteiras.

| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| `POST` | `/wallet-user` | Adiciona um colaborador a uma carteira. |
| `PUT` | `/wallet-user/{id}` | Atualiza as permissões de um colaborador. |
| `GET` | `/wallet-user/{walletId}`| Lista todos os colaboradores de uma carteira. |
| `DELETE`| `/wallet-user/{id}` | Remove um colaborador. |

### 📝 Notas (`/note`)

| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| `POST` | `/note` | Cria uma nota dentro de uma carteira. |
| `GET` | `/note/{walletId}` | Lista todas as notas de uma carteira (paginado). |
| `PUT` | `/note/{noteId}` | Atualiza uma nota (requer `walletId` na query). |
| `DELETE`| `/note/{noteId}` | Remove uma nota (requer `walletId` na query). |

### 🤝 Amigos (`/friend-request`)

| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| `POST` | `/friend-request` | Envia uma solicitação de amizade. |
| `GET` | `/friend-request/friends`| Lista amigos confirmados. |
| `GET` | `/friend-request/pendings`| Lista solicitações pendentes. |
| `PATCH` | `/friend-request/{id}` | Responde a uma solicitação (ACEITAR/RECUSAR). |
| `DELETE`| `/friend-request/{id}` | Cancela/Remove uma solicitação ou amizade. |

### 🔔 Notificações (`/notification`)

| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| `GET` | `/notification` | Lista notificações do usuário. |
| `GET` | `/notification/unread` | Conta notificações não lidas. |
| `PATCH` | `/notification/mark-as-read/{id}` | Marca uma notificação específica como lida. |
| `PATCH` | `/notification/mark-all-as-read` | Marca todas como lidas. |

### 🛠️ Usuários e Admin

| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| `GET` | `/user` | Busca usuários (Ex: para adicionar amigos). |
| `GET` | `/user/{id}` | Busca detalhes de um perfil. |
| `PUT` | `/user/{id}` | Atualiza dados do perfil. |
| `DELETE`| `/user` | Deleta a conta do usuário logado. |
| `GET` | `/user-token` | (Admin) Limpa tokens expirados. |

---

## <a id="docs"></a>⚙️Features futuras

* Concertar pequenos bugs no front
* Implementar criptografia assimetrica, atualmente utilizado simetrica
* Implementar a funcionalidade de envio de documentos

---

### <a id="autores"></a>👨‍💻 Autores

Este projeto foi desenvolvido por:

* **João Vitor da Rosa de Oliveira**
