<html>
<head>
    <meta charset="x-UTF-16LE-BOM">
    <link rel="stylesheet" href="WEB-INF/assets/css/style.css" />
    <title> NewGO - Servlet </title>
</head>

<style>
    @import url('https://fonts.googleapis.com/css2?family=Raleway:wght@100&display=swap');

    *{
        padding: 0;
        margin: 0;
        text-decoration: none;
        list-style: none;
    }

    html{
        font-size: 16px;
    }

    body{
        font-family: "Raleway";
    }

    main{
        position: relative;
        display: flex;
        width: 100%;
        height: 100vh;
        align-items: center;
        justify-content: center;
        background-color: #05001d;
    }

    form{
        position: relative;
        display: flex;
        flex-direction: column;
    }

    div.formDiv{
        position: relative;
        display: flex;
        flex-direction: column;
        width: 30%;
        height: 90%;
        background-color: #0a081e;
        border-radius: 20px;
        align-items: center;
        justify-content: center;
    }

    p.formTitle{
        color: #fff;
        font-weight: bold;
        font-size: 4rem;
    }

    label{
        color: rgba(255, 255, 255, 0.5);
        font-weight: bold;
        font-size: 1rem;
    }

    input{
        border: 0;
        height: 25px;
        font-size: 15px;
    }

    input:focus-visible {
        outline: none;
    }

    input:active{
        border: 0;
    }

    #buttonSubmit{
        position: relative;
        width: 100%;
        height: 50px;
        border-radius: 20px;
        border: 0;
        background-color: #242436;
        color: #fff;
        cursor: pointer;
    }
</style>

<body>
<header></header>
<main>
    <div class="formDiv">
        <p class="formTitle"> Cadastrar </p>
        <br><br>
        <form action="/serveletTest_war/cadastrar" method="POST">
            <label for="nome"> Escreva o nome do produto: </label>
            <input type="text" id="nome" name="nome" minlength="3"/>

            <br>

            <label for="descricao"> Digite a descricao do produto: </label>
            <input type="text" id="descricao" name="descricao" minlength="10"/>

            <br>

            <label for="preco"> Digite o preco do produto: </label>
            <input type="number" id="preco" name="preco" min="1"/>

            <br>

            <label for="quantidade"> Digite a quantidade em estoque: </label>
            <input type="number" id="quantidade" name="quantidade" min="1"/>

            <br>

            <label for="min_quantidade"> Digite a quantidade minima de estoque: </label>
            <input type="number" id="min_quantidade" name="min_quantidade" min="1"/>

            <br>

            <input type="submit" id="buttonSubmit" value="Enviar" />
        </form>
    </div>
</main>
<footer></footer>
</body>
</html>
