package com.pedro.infrastructure;

import java.sql.Connection;
import java.sql.DriverManager;

public class Conexao {
    private String url;
    private String usuario;
    private String senha;
    private String database;

    private Connection con;

    public Conexao(){
        url="jdbc:postgresql://localhost:5432/newgo";
        usuario="postgres";
        senha="root";
        database="newgo";

        try{
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection(url, usuario, senha);
            System.out.println("Conexao realizada!!!");
        } catch (Exception e){
            e.printStackTrace();
        }
    }


}
