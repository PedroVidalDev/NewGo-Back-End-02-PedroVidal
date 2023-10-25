# <p align="center"> NewGo-Back-End-02-PedroVidal </p>

## Objetivo
Projeto com o objetivo de criar um sistema de gerenciamento de estoque.

## Tecnologias
- Java
- Servlets
- Postgresql
- JSP

## Mapeamento
- /products/*
Endpoint para realizar CRUD básico de produtos.

- /LativoAlterar/products/*
Endpoint para alterar a coluna de l_ativo de algum produto.

- /products/createBatch
Endpoint para criar produtos em lote.

- /products/editBatch/*
Endpoint para editar o preço ou quantidade de produtos em lote. Variável a ser usada no * deve ser "price" ou "qnt".
