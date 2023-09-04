<html>
<head>
    <meta charset="x-UTF-16LE-BOM">
    <title> NewGO - Servlet </title>
</head>
<body>
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

    .container{
        position: relative;
        display: flex;
        flex-direction: column;
        width: 30%;
        height: 50%;
        background-color: #0a081e;
        border-radius: 20px;
        align-items: center;
        justify-content: center;
    }

    .resposta{
        color: #fff;
        font-weight: bold;
        font-size: 2rem;
    }

    .voltarButton{
        position: relative;
        width: 150px;
        height: 50px;
        border-radius: 20px;
        border: 0;
        background-color: #242436;
        color: #fff;
        cursor: pointer;
    }
</style>
<header></header>
<main>
    <%
        String resposta = (String) request.getAttribute("resposta");
    %>
    <div class="container">
        <p class="resposta"> <%=resposta%></p>
        <br>
        <a href="/serveletTest_war/"> <button class="voltarButton"> Voltar </button> </a>
    </div>
</main>
<footer></footer>
</body>
</html>

