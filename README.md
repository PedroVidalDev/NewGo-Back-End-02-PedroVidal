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
<p>Endpoint para realizar CRUD básico de produtos.</p>

- /LativoAlterar/products/*
<p>Endpoint para alterar a coluna de l_ativo de algum produto.</p>

- /products/createBatch
<p>Endpoint para criar produtos em lote.</p>

- /products/editBatch/*
<p>Endpoint para editar o preço ou quantidade de produtos em lote. Variável a ser usada no * deve ser "price" ou "qnt".</p>
