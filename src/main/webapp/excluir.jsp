<html>
<head>
    <meta charset="x-UTF-16LE-BOM">
    <link rel="stylesheet" href="assets/css/style.css" />
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

    .buttonSubmit{
        position: relative;
        width: 100%;
        height: 50px;
        border-radius: 20px;
        border: 0;
        background-color: #242436;
        color: #fff;
        margin-top: 20px;
        cursor: pointer;
    }
</style>

<body>
<header></header>
<main>
    <div class="formDiv">
        <p class="formTitle"> Excluir </p>

        <form action="/serveletTest_war/products/excluir" method="post">
            <label for="id"> Digitar ID do produto: </label>
            <input type="number" name="id" id="id" min="1">
            <input type="submit" class="buttonSubmit" value="Deletar">
        </form>

    </div>
</main>
<footer></footer>
</body>
</html>
