package com.pedro.infrastructure.DAOs;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.pedro.application.DTOs.ProductInput;
import com.pedro.infrastructure.entities.Product;
import org.postgresql.jdbc2.ArrayAssistant;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;

public class ProductDAO {
    private String url;
    private String usuario;
    private String senha;
    private String database;

    private Connection con;

    public void create(ProductInput product){

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

    public Product consultar(String hash){
        url="jdbc:postgresql://localhost:5432/newgo";
        usuario="postgres";
        senha="root";

        try{
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection(url, usuario, senha);
            System.out.println("Conexao realizada!!!");

            String productSelect = "select * from produtos where hash =?";
            PreparedStatement preparedStatementNome = con.prepareStatement(productSelect);
            preparedStatementNome.setObject(1, UUID.fromString(hash));
            ResultSet rs = preparedStatementNome.executeQuery();

            if(rs.next()){
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String descricao = rs.getString("descricao");
                String ean13 = rs.getString("ean13");
                float preco = rs.getFloat("preco");
                int quantidade = rs.getInt("quantidade");
                int estoque_min = rs.getInt("estoque_min");
                Timestamp dtcreate = rs.getTimestamp("dtcreate");
                Timestamp dtupdate = rs.getTimestamp("dtupdate");
                boolean lativo = rs.getBoolean("l_ativo");

                Product product = new Product(id, UUID.fromString(hash), nome, descricao, ean13, preco, quantidade, estoque_min, dtcreate, dtupdate, lativo);

                return product;
            }

        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public void deletar(String hash){
        url="jdbc:postgresql://localhost:5432/newgo";
        usuario="postgres";
        senha="root";

        try{
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection(url, usuario, senha);
            System.out.println("Conexao realizada!!!");

            int affectedRows = 0;

            String productSelect = "delete from produtos where hash =?";
            PreparedStatement preparedStatement = con.prepareStatement(productSelect);
            preparedStatement.setObject(1, UUID.fromString(hash));
            preparedStatement.executeUpdate();

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void LativoAlterar(String hash, boolean lativo){
        url="jdbc:postgresql://localhost:5432/newgo";
        usuario="postgres";
        senha="root";

        try{
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection(url, usuario, senha);
            System.out.println("Conexao realizada!!!");

            String productSelect = "update produtos set l_ativo =?, dtupdate=? where hash =?";
            PreparedStatement preparedStatement = con.prepareStatement(productSelect);
            preparedStatement.setBoolean(1, lativo);
            preparedStatement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            preparedStatement.setObject(3, UUID.fromString(hash));

            preparedStatement.executeUpdate();

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void alterar(String hash, Product product){
        url="jdbc:postgresql://localhost:5432/newgo";
        usuario="postgres";
        senha="root";

        try{
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection(url, usuario, senha);
            System.out.println("Conexao realizada!!!");

            String productSelect = "update produtos set descricao=?, preco=?, quantidade=?, estoque_min=?, dtupdate=?, l_ativo=true where hash =?";
            PreparedStatement preparedStatement = con.prepareStatement(productSelect);
            preparedStatement.setString(1, product.getDescricao());
            preparedStatement.setFloat(2, product.getPreco());
            preparedStatement.setInt(3, product.getQuantidade());
            preparedStatement.setInt(4, product.getEstoquemin());
            preparedStatement.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
            preparedStatement.setObject(6, UUID.fromString(hash));

            preparedStatement.executeUpdate();

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean consultarLativoAntigo(String hash){
        url="jdbc:postgresql://localhost:5432/newgo";
        usuario="postgres";
        senha="root";

        boolean lativo = false;

        try{
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection(url, usuario, senha);
            System.out.println("Conexao realizada!!!");

            String productSelect = "select * from produtos where hash =?";
            PreparedStatement preparedStatementNome = con.prepareStatement(productSelect);
            preparedStatementNome.setObject(1, UUID.fromString(hash));
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
                int id = rs.getInt("id");
                UUID hash = UUID.fromString(rs.getString("hash"));
                String nome = rs.getString("nome");
                String descricao = rs.getString("descricao");
                String ean13 = rs.getString("ean13");
                float preco = rs.getFloat("preco");
                int quantidade = rs.getInt("quantidade");
                int estoque_min = rs.getInt("estoque_min");
                Timestamp dtcreate = rs.getTimestamp("dtcreate");
                Timestamp dtupdate = rs.getTimestamp("dtupdate");

                Product product = new Product(id, hash, nome, descricao, ean13, preco, quantidade, estoque_min, dtcreate, dtupdate, lativo);

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

            String productSelect = "select * from produtos where quantidade<estoque_min AND l_ativo=true";
            PreparedStatement preparedStatementNome = con.prepareStatement(productSelect);
            ResultSet rs = preparedStatementNome.executeQuery();

            while(rs.next()){
                int id = rs.getInt("id");
                UUID hash = UUID.fromString(rs.getString("hash"));
                String nome = rs.getString("nome");
                String descricao = rs.getString("descricao");
                String ean13 = rs.getString("ean13");
                float preco = rs.getFloat("preco");
                int quantidade = rs.getInt("quantidade");
                int estoque_min = rs.getInt("estoque_min");
                Timestamp dtcreate = rs.getTimestamp("dtcreate");
                Timestamp dtupdate = rs.getTimestamp("dtcreate");

                Product product = new Product(id, hash, nome, descricao, ean13, preco, quantidade, estoque_min, dtcreate, dtupdate, true);

                listaProdutos.add(product);
            }

        } catch (Exception e){
            e.printStackTrace();
        }
        return listaProdutos;
    }
}

