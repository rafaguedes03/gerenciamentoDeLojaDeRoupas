package com.loja.amor_de_mamae.dao;

import com.loja.amor_de_mamae.model.Produto;
import com.loja.amor_de_mamae.dao.ProdutoEstoqueDAO; // Importar a nova classe DAO
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {
    
    // Método para salvar um Produto e retornar o ID gerado
    public void salvar(Produto p) throws Exception {
        String sql = "INSERT INTO Produtos (codigo, nome, preco) VALUES (?, ?, ?)";
        
        // A flag RETURN_GENERATED_KEYS é crucial para obter o ID gerado
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, p.getCodigo());
            stmt.setString(2, p.getNome());
            stmt.setDouble(3, p.getPreco());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    p.setIdProduto(rs.getInt(1));
                }
            }
        }
    }

    // Método para listar produtos com os dados de estoque
    public List<ProdutoEstoqueDAO> listarComEstoque() throws Exception {
        List<ProdutoEstoqueDAO> produtos = new ArrayList<>();
        String sql = "SELECT p.id_produto, p.codigo, p.nome, p.preco, e.tamanho, e.quantidade " +
                     "FROM Produtos p JOIN Estoque e ON p.id_produto = e.id_produto";
        
        try (Connection conn = ConnectionFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                ProdutoEstoqueDAO p = new ProdutoEstoqueDAO();
                p.setId_produto(rs.getInt("id_produto"));
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

    // Método para buscar um produto por código
    public Produto buscarPorCodigo(String codigo) throws Exception {
        String sql = "SELECT * FROM Produtos WHERE codigo = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, codigo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Produto p = new Produto(rs.getString("nome"), rs.getString("codigo"), rs.getDouble("preco"));
                    p.setIdProduto(rs.getInt("id_produto"));
                    return p;
                }
            }
        }
        return null;
    }
}