package com.pedro.infrastructure;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.sql.ResultSet;

public class ProductDAO {
    private String url;
    private String usuario;
    private String senha;
    private String database;

    private Connection con;

    public void create(String nome, String descricao, String ean13, float preco, int quantidade, int min_quantidade){

        url="jdbc:postgresql://localhost:5432/newgo";
        usuario="postgres";
        senha="root";

        try{
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection(url, usuario, senha);
            System.out.println("Conexao realizada!!!");

            String sql = "INSERT INTO produtos (nome, descricao, ean13, preco, quantidade, estoque_min, dtcreate, l_ativo) values (?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement statement = con.prepareStatement(sql);

            statement.setString(1, nome);
            statement.setString(2, descricao);
            statement.setString(3, ean13);
            statement.setFloat(4, preco);
            statement.setInt(5, quantidade);
            statement.setInt(6, min_quantidade);
            statement.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
            statement.setBoolean(8, false);

            int rows = statement.executeUpdate();
            System.out.println("Novo produto adicionado.");

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean validate(String nome, String ean13){
        url="jdbc:postgresql://localhost:5432/newgo";
        usuario="postgres";
        senha="root";

        try {
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection(url, usuario, senha);
            System.out.println("Conexao realizada!!!");

            String nomeVerify = "select * from produtos where nome =?";
            PreparedStatement preparedStatementNome = con.prepareStatement(nomeVerify);
            preparedStatementNome.setString(1, nome);
            ResultSet rsNome = preparedStatementNome.executeQuery();

            String ean13Verify = "select * from produtos where ean13 =?";
            PreparedStatement preparedStatementEan = con.prepareStatement(ean13Verify);
            preparedStatementEan.setString(1, ean13);
            ResultSet rsEan = preparedStatementEan.executeQuery();

            if(rsNome.next() && rsEan.next()){
                return true;
            }

        } catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
