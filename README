# 💖 Amor de Mamãe – Sistema de Vendas e Fiado (S.A.M.S.V.F)

## 📝 Sobre o projeto
O **S.A.M.S.V.F** é um **sistema desktop** para automatizar o controle de estoque, vendas e clientes da loja *Amor de Mamãe* (roupas infantis).

**Funcionalidades principais:**
- Cadastro de produtos com controle de tamanhos
- Gerenciamento de vendas e fiado
- Emissão de relatórios detalhados por período
- Sistema offline, rodando direto no computador

**Tecnologias utilizadas:**  
Java | JavaFX | MySQL | Maven | jbcrypt | iTextPDF  

**IDE utilizada:** Visual Studio Code

---

## ⚙️ Pré-requisitos
- Java (22.x ou 24.x compatível com JavaFX)  
- MySQL em execução  
- Maven configurado  

---

## 💾 Banco de Dados
O script SQL completo está disponível em:  

/sql/amor_de_mamae.sql

pgsql
Copiar
Editar

**Prévia do SQL** (exemplo de criação do banco e tabela Produtos):

```sql
-- Criar banco
CREATE DATABASE IF NOT EXISTS amor_de_mamae;
USE amor_de_mamae;

-- Tabela Produtos
CREATE TABLE Produtos (
    id_produto INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    codigo CHAR(20) NOT NULL UNIQUE,
    preco DECIMAL(10,2) NOT NULL
);
Execute o script completo no MySQL Workbench ou terminal para criar todas as tabelas.

Conexão padrão (util/ConexaoMySQL.java):

java
Copiar
Editar
URL = "jdbc:mysql://localhost:3306/amor_de_mamae"
USER = "root"
PASSWORD = ""
Ajuste conforme seu ambiente.

▶️ Como Rodar
Clone o projeto:

bash
Copiar
Editar
git clone https://github.com/rafaguedes03/gerenciamentoDeLojaDeRoupas.git
Abra a pasta no VSCode ou na IDE de sua preferência

Compile com Maven:

bash
Copiar
Editar
mvn clean install
Execute a classe principal (MainController) para abrir a tela de login

Cadastre o primeiro usuário (administrador/funcionário) para iniciar

🗂️ Estrutura do projeto
swift
Copiar
Editar
/src/main/java/com/loja/amor_de_mamae/
 ├─ controller  → Controllers do JavaFX
 ├─ dao         → Acesso ao banco de dados
 ├─ model       → Models/Entidades
 └─ util        → Classes utilitárias (ex.: Conexão MySQL)

/src/main/resources/com/loja/amor_de_mamae/view → Arquivos FXML

/sql → Script SQL do banco
⚠️ Observações
Sistema offline, sem necessidade de internet

Recomenda-se backup periódico do banco de dados

Usuário inicial deve ser cadastrado pelo sistema

Foco: simplicidade e funcionalidade, ideal para pequenas lojas
