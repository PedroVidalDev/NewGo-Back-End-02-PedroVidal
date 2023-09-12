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

    .buttonSubmit{
        position: relative;
        width: 90%;
        height: 50px;
        border-radius: 20px;
        border: 0;
        background-color: #242436;
        color: #fff;
        cursor: pointer;
        margin-bottom: 20px;
        transition: 0.5s;
    }

    .buttonSubmit:hover{
        background-color: #3e3e5d;
    }

    a{
        color: white;
    }

</style>

<body>
<header></header>
<main>
    <div class="formDiv">
        <p class="formTitle"> Acoes </p>

        <br><br>

        <button class="buttonSubmit"> <a href="create.jsp"> Adicionar produto </a> </button>
        <button class="buttonSubmit"> <a href="consultar.jsp"> Consultar produto </a> </button>
        <button class="buttonSubmit"> <a href="editar.jsp"> Editar produto </a> </button>
        <button class="buttonSubmit"> <a href="excluir.jsp"> Excluir produto </a> </button>

    </div>
</main>
<footer></footer>
</body>
</html>
