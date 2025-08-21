package com.loja.amor_de_mamae.dao;

import com.loja.amor_de_mamae.model.Produto;

import javafx.util.Callback;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {
    public void salvar(Produto p) throws Exception {
        String sql = "INSERT INTO Produtos (codigo, nome, preco, tamanho, quantidade) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, p.getCodigo());
            stmt.setString(2, p.getNome());
            stmt.setDouble(3, p.getPreco());
            stmt.setString(4, p.getTamanho());
            stmt.setInt(5, p.getQuantidade());
            stmt.executeUpdate();
        }
    }

    public List<Produto> listar() throws Exception {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM Produtos";
        try (Connection conn = ConnectionFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Produto p = new Produto();
                p.setIdProduto(rs.getInt("id_produto"));
                p.setCodigo(rs.getString("codigo"));
                p.setNome(rs.getString("nome"));
                p.setPreco(rs.getDouble("preco"));
                p.setTamanho(rs.getString("tamanho"));
                p.setQuantidade(rs.getInt("quantidade"));
                produtos.add(p);
            }
        }
        return produtos;
    }
}
