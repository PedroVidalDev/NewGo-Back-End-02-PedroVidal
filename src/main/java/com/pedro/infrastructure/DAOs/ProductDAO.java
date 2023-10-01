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

    public Product create(Product product){

        url="jdbc:postgresql://localhost:5432/newgo";
        usuario="postgres";
        senha="root";

        try{
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection(url, usuario, senha);
            System.out.println("Conexao realizada!!!");

            String sql = "INSERT INTO produtos (hash, nome, descricao, ean13, preco, quantidade, estoque_min, dtcreate, l_ativo) values (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement statement = con.prepareStatement(sql);

            statement.setObject(1, product.getHash());
            statement.setString(2, product.getNome());
            statement.setString(3, product.getDescricao());
            statement.setString(4, product.getEan13());
            statement.setFloat(5, product.getPreco());
            statement.setInt(6, product.getQuantidade());
            statement.setInt(7, product.getEstoquemin());
            statement.setTimestamp(8, new Timestamp(System.currentTimeMillis()));
            statement.setBoolean(9, false);

            statement.executeUpdate();
            Product newProduct = consultar(product.getHash());

            return newProduct;

        } catch (Exception e){
            e.printStackTrace();
        }

        return null;
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

    public Product consultar(UUID hash){
        url="jdbc:postgresql://localhost:5432/newgo";
        usuario="postgres";
        senha="root";

        try{
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection(url, usuario, senha);
            System.out.println("Conexao realizada!!!");

            String productSelect = "select * from produtos where hash =?";
            PreparedStatement preparedStatementNome = con.prepareStatement(productSelect);
            preparedStatementNome.setObject(1, hash);
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

                Product product = new Product(id, hash, nome, descricao, ean13, preco, quantidade, estoque_min, dtcreate, dtupdate, lativo);

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

    public void alterar(UUID hash, Product product){
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
            preparedStatement.setObject(6, hash);

            preparedStatement.executeUpdate();

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean consultarLativoAntigo(UUID hash){
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
            preparedStatementNome.setObject(1, hash);
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

    public void editPriceBatch(UUID hash, float value){
        url="jdbc:postgresql://localhost:5432/newgo";
        usuario="postgres";
        senha="root";

        try{
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection(url, usuario, senha);
            System.out.println("Conexao realizada!!!");

            String productSelect = "update produtos set preco=?, dtupdate=? where hash =?";
            PreparedStatement preparedStatement = con.prepareStatement(productSelect);
            preparedStatement.setFloat(1, value);
            preparedStatement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            preparedStatement.setObject(3, hash);

            preparedStatement.executeUpdate();

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void editQntBatch(UUID hash, float value){
        url="jdbc:postgresql://localhost:5432/newgo";
        usuario="postgres";
        senha="root";

        try{
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection(url, usuario, senha);
            System.out.println("Conexao realizada!!!");

            String productSelect = "update produtos set quantidade=?, dtupdate=? where hash =?";
            PreparedStatement preparedStatement = con.prepareStatement(productSelect);
            preparedStatement.setFloat(1, value);
            preparedStatement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            preparedStatement.setObject(3, hash);

            preparedStatement.executeUpdate();

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
