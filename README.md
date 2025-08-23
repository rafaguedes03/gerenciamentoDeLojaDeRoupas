# 💖 Amor de Mamãe – Sistema de Vendas e Fiado (S.A.M.S.V.F)
## 📝 Sobre o projeto

### O S.A.M.S.V.F é um sistema desktop para automatizar o controle de estoque, vendas e clientes da loja Amor de Mamãe (roupas infantis).
Funcionalidades principais:

* Cadastro de produtos com controle de tamanhos;

* Gerenciamento de vendas e fiado;

* Emissão de relatórios detalhados por período;

* Sistema offline, rodando direto no computador.

---

## Ferramentas Utilizadas
![Git](https://img.shields.io/badge/git-%23F05033.svg?style=for-the-badge&logo=git&logoColor=white)
![GitHub](https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white)
![MySQL](https://img.shields.io/badge/mysql-4479A1.svg?style=for-the-badge&logo=mysql&logoColor=white)
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Maven](https://img.shields.io/badge/maven-%23007d9c.svg?style=for-the-badge&logo=apachemaven&logoColor=white)
![JavaFX](https://img.shields.io/badge/javafx-%2334E2E2.svg?style=for-the-badge&logo=java&logoColor=white)
![jBCrypt](https://img.shields.io/badge/jBCrypt-%23F05033.svg?style=for-the-badge&logo=java&logoColor=white)
![iTextPDF](https://img.shields.io/badge/iTextPDF-%23000000.svg?style=for-the-badge&logo=java&logoColor=white)

---

## ⚙️ Pré-requisitos

* Java (22.x ou 24.x compatível com JavaFX)

* MySQL em execução

* Maven configurado
  
---

## 💾 Banco de Dados

O script SQL completo está disponível em:

```
/sql/amor_de_mamae.sql
```

Prévia do SQL (exemplo de criação do banco e tabela Produtos):
```
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
```

Execute o script completo no MySQL Workbench ou terminal para criar todas as tabelas.

Conexão padrão (util/ConexaoMySQL.java):
```
URL = "jdbc:mysql://localhost:3306/amor_de_mamae"
USER = "root"
PASSWORD = ""
```

Ajuste conforme seu ambiente.

---

## ▶️ Como Rodar

Clone o projeto:
```
git clone https://github.com/rafaguedes03/gerenciamentoDeLojaDeRoupas.git
```

### Abra a pasta no VSCode ou qualquer outra IDE da sua preferência.

Compile com Maven:
* Observacao: O comando deve ser executado na pasta onde contem o arquivo ```pom.xml``` provavelmente em  ```gerenciamentoDeLojaDeRoupas\sistema```
```
mvn clean install
```

Execute a classe principal (Main.java) para iniciar o sistema.

Cadastre o primeiro usuário (administrador/funcionario) para iniciar.
---
## 🗂️ Estrutura do projeto
/src/main/java/com/loja/amor_de_mamae/

 ├─ controller  → Controllers do JavaFX
 
 ├─ dao         → Acesso ao banco de dados
 
 ├─ model       → Models/Entidades
 
 └─ util        → Classes utilitárias (ex.: Conexão MySQL)
 
/src/main/resources/com/loja/amor_de_mamae/view → Arquivos FXML

/sql → Script SQL do banco
---
## ⚠️ Observações

* Sistema offline, sem necessidade de internet.

* Usuário inicial deve ser cadastrado pelo sistema.

* Foco: simplicidade e funcionalidade, ideal para pequenas lojas.
