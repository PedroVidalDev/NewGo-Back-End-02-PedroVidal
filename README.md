# <p align="center"> NewGo-Back-End-02-PedroVidal </p>

## Objetivo
Projeto com o objetivo de criar um sistema de gerenciamento de estoque.

## Tecnologias
- Java
- Servlets
- Postgresql
- JSP

## Endpoints
- `/products/*` - Endpoint para realizar CRUD básico de produtos, usando os métodos POST para criação, GET para leitura, PUT para atualizar e DELETE para exclusão.

- `/LativoAlterar/products/*` - Endpoint para alterar a coluna de l_ativo de algum produto pelo seu hash.

- `/products/createBatch` - Endpoint para criar produtos em lote.

- `/products/editBatch/*` - Endpoint para editar o preço ou quantidade de produtos em lote. Variável a ser usada no * deve ser "price" ou "qnt".
