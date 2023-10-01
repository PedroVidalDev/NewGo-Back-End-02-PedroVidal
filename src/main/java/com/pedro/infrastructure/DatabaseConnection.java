package com.pedro.infrastructure;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String url = "jdbc:postgresql://localhost:5432/newgo";
    private static final String user = "postgres";
    private static final String password = "root";

    public static Connection getConnection(){
        try{

            Class.forName("org.postgresql.Driver");
            Connection con = DriverManager.getConnection(url, user, password);
            System.out.println("Conexao realizada!!!");
            return con;

        } catch(ClassNotFoundException | SQLException e){
            throw new RuntimeException(e);
        }

    }

}
