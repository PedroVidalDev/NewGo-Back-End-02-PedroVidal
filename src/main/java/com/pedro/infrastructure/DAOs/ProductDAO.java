package com.pedro.infrastructure.DAOs;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.pedro.infrastructure.entities.Product;
import org.postgresql.jdbc2.ArrayAssistant;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class ProductDAO {
    private String url;
    private String usuario;
    private String senha;
    private String database;

    private Connection con;

    public void create(Product product){

        url="jdbc:postgresql://localhost:5432/newgo";
        usuario="postgres";
        senha="root";

        try{
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection(url, usuario, senha);
            System.out.println("Conexao realizada!!!");

            String sql = "INSERT INTO produtos (nome, descricao, ean13, preco, quantidade, estoque_min, dtcreate, l_ativo) values (?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement statement = con.prepareStatement(sql);

            statement.setString(1, product.getNome());
            statement.setString(2, product.getDescricao());
            statement.setString(3, product.getEan13());
            statement.setFloat(4, product.getPreco());
            statement.setInt(5, product.getQuantidade());
            statement.setInt(6, product.getEstoquemin());
            statement.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
            statement.setBoolean(8, false);

            statement.executeUpdate();

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

            if(rsNome.next() || rsEan.next()){
                return true;
            }

        } catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public Product consultar(int id){
        url="jdbc:postgresql://localhost:5432/newgo";
        usuario="postgres";
        senha="root";

        try{
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection(url, usuario, senha);
            System.out.println("Conexao realizada!!!");

            String productSelect = "select * from produtos where id =?";
            PreparedStatement preparedStatementNome = con.prepareStatement(productSelect);
            preparedStatementNome.setInt(1, id);
            ResultSet rs = preparedStatementNome.executeQuery();

            if(rs.next()){
                String nome = rs.getString("nome");
                String descricao = rs.getString("descricao");
                String ean13 = rs.getString("ean13");
                float preco = rs.getFloat("preco");
                int quantidade = rs.getInt("quantidade");
                int estoque_min = rs.getInt("estoque_min");

                Product product = new Product(nome, descricao, ean13, preco, estoque_min, quantidade);

                return product;
            }

        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public void deletar(int id){
        url="jdbc:postgresql://localhost:5432/newgo";
        usuario="postgres";
        senha="root";

        try{
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection(url, usuario, senha);
            System.out.println("Conexao realizada!!!");

            int affectedRows = 0;

            String productSelect = "delete from produtos where id =?";
            PreparedStatement preparedStatement = con.prepareStatement(productSelect);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();


        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void LativoAlterar(int id, boolean lativo){
        url="jdbc:postgresql://localhost:5432/newgo";
        usuario="postgres";
        senha="root";

        try{
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection(url, usuario, senha);
            System.out.println("Conexao realizada!!!");

            String productSelect = "update produtos set l_ativo =?, dtupdate=? where id =?";
            PreparedStatement preparedStatement = con.prepareStatement(productSelect);
            preparedStatement.setBoolean(1, lativo);
            preparedStatement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            preparedStatement.setInt(3, id);

            preparedStatement.executeUpdate();

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void alterar(int id, Product product){
        url="jdbc:postgresql://localhost:5432/newgo";
        usuario="postgres";
        senha="root";

        try{
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection(url, usuario, senha);
            System.out.println("Conexao realizada!!!");

            String productSelect = "update produtos set descricao=?, preco=?, quantidade=?, estoque_min=?, dtupdate=?, l_ativo=true where id =?";
            PreparedStatement preparedStatement = con.prepareStatement(productSelect);
            preparedStatement.setString(1, product.getDescricao());
            preparedStatement.setFloat(2, product.getPreco());
            preparedStatement.setInt(3, product.getQuantidade());
            preparedStatement.setInt(4, product.getEstoquemin());
            preparedStatement.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
            preparedStatement.setInt(6, id);

            preparedStatement.executeUpdate();

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean consultarLativoAntigo(int id){
        url="jdbc:postgresql://localhost:5432/newgo";
        usuario="postgres";
        senha="root";

        boolean lativo = false;

        try{
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection(url, usuario, senha);
            System.out.println("Conexao realizada!!!");

            String productSelect = "select * from produtos where id =?";
            PreparedStatement preparedStatementNome = con.prepareStatement(productSelect);
            preparedStatementNome.setInt(1, id);
            ResultSet rs = preparedStatementNome.executeQuery();

            if(rs.next()) {
                lativo = rs.getBoolean("l_ativo");
            }

        } catch (Exception e){
            e.printStackTrace();
        }

        return lativo;
    }

    public ArrayList filtrarProdutosLativo(boolean lativo){
        url="jdbc:postgresql://localhost:5432/newgo";
        usuario="postgres";
        senha="root";

        ArrayList<Product> listaProdutos = new ArrayList();

        try{
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection(url, usuario, senha);
            System.out.println("Conexao realizada!!!");

            String productSelect = "select * from produtos where l_ativo =?";
            PreparedStatement preparedStatementNome = con.prepareStatement(productSelect);
            preparedStatementNome.setBoolean(1, lativo);
            ResultSet rs = preparedStatementNome.executeQuery();

            while(rs.next()){
                String nome = rs.getString("nome");
                String descricao = rs.getString("descricao");
                String ean13 = rs.getString("ean13");
                float preco = rs.getFloat("preco");
                int quantidade = rs.getInt("quantidade");
                int estoque_min = rs.getInt("estoque_min");

                Product product = new Product(nome, descricao, ean13, preco, estoque_min, quantidade);

                listaProdutos.add(product);
            }

        } catch (Exception e){
            e.printStackTrace();
        }
        return listaProdutos;
    }

    public ArrayList filtrarProdutosQntMenorMin(){
        url="jdbc:postgresql://localhost:5432/newgo";
        usuario="postgres";
        senha="root";

        ArrayList<Product> listaProdutos = new ArrayList();

        try{
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection(url, usuario, senha);
            System.out.println("Conexao realizada!!!");

            String productSelect = "select * from produtos where quantidade<estoque_min";
            PreparedStatement preparedStatementNome = con.prepareStatement(productSelect);
            ResultSet rs = preparedStatementNome.executeQuery();

            while(rs.next()){
                String nome = rs.getString("nome");
                String descricao = rs.getString("descricao");
                String ean13 = rs.getString("ean13");
                float preco = rs.getFloat("preco");
                int quantidade = rs.getInt("quantidade");
                int estoque_min = rs.getInt("estoque_min");

                Product product = new Product(nome, descricao, ean13, preco, estoque_min, quantidade);

                listaProdutos.add(product);
            }

        } catch (Exception e){
            e.printStackTrace();
        }
        return listaProdutos;
    }
}

