package com.loja.amor_de_mamae.dao;

import com.loja.amor_de_mamae.dao.ProdutoEstoqueDAO;
import com.loja.amor_de_mamae.model.Produto;
import com.loja.amor_de_mamae.util.ConexaoMySQL;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {
    
    public List<ProdutoEstoqueDAO> buscarProdutosComEstoque(String termo) throws Exception {
        List<ProdutoEstoqueDAO> produtos = new ArrayList<>();
        String sql = "SELECT e.id_estoque, p.id_produto, p.nome, p.codigo, p.preco, e.tamanho, e.quantidade " +
                     "FROM produtos p JOIN estoque e ON p.id_produto = e.id_produto " +
                     "WHERE (p.nome LIKE ? OR p.codigo LIKE ?) AND e.quantidade > 0 " +
                     "ORDER BY p.nome";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + termo + "%");
            stmt.setString(2, "%" + termo + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ProdutoEstoqueDAO dto = new ProdutoEstoqueDAO();
                    dto.setId_estoque(rs.getInt("id_estoque"));
                    dto.setId_produto(rs.getInt("id_produto"));
                    dto.setNome(rs.getString("nome"));
                    dto.setCodigo(rs.getString("codigo"));
                    dto.setPreco(rs.getDouble("preco"));
                    dto.setTamanho(rs.getString("tamanho"));
                    dto.setQuantidade(rs.getInt("quantidade"));
                    produtos.add(dto);
                }
            }
        }
        return produtos;
    }
    
    public ProdutoEstoqueDAO buscarProdutoPorIdEstoque(int idEstoque) throws Exception {
        String sql = "SELECT e.id_estoque, p.id_produto, p.nome, p.codigo, p.preco, e.tamanho, e.quantidade " +
                     "FROM produtos p JOIN estoque e ON p.id_produto = e.id_produto " +
                     "WHERE e.id_estoque = ?";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idEstoque);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    ProdutoEstoqueDAO dto = new ProdutoEstoqueDAO();
                    dto.setId_estoque(rs.getInt("id_estoque"));
                    dto.setId_produto(rs.getInt("id_produto"));
                    dto.setNome(rs.getString("nome"));
                    dto.setCodigo(rs.getString("codigo"));
                    dto.setPreco(rs.getDouble("preco"));
                    dto.setTamanho(rs.getString("tamanho"));
                    dto.setQuantidade(rs.getInt("quantidade"));
                    return dto;
                }
            }
        }
        return null;
    }

    // metodo para excluir produto
    public void excluir(int idProduto) throws Exception {
        String sql = "DELETE FROM produtos WHERE id_produto = ?";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idProduto);
            stmt.executeUpdate();
        }
    }

    public void atualizar(Produto produto) throws Exception {
        String sql = "UPDATE produtos SET nome = ?, codigo = ?, preco = ? WHERE id_produto = ?";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getCodigo());
            stmt.setDouble(3, produto.getPreco());
            stmt.setInt(4, produto.getIdProduto());
            stmt.executeUpdate();
        }
    }

    public void salvar(Produto produto) throws Exception {
        String sql = "INSERT INTO produtos (nome, codigo, preco) VALUES (?, ?, ?)";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getCodigo());
            stmt.setDouble(3, produto.getPreco());
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    produto.setIdProduto(rs.getInt(1));
                }
            }
        }
    }

    

    // listar produtos com estoque
    public List<ProdutoEstoqueDAO> listarComEstoque() throws Exception {
        List<ProdutoEstoqueDAO> produtos = new ArrayList<>();
        String sql = "SELECT p.id_produto, p.codigo, p.nome, p.preco, e.tamanho, e.quantidade " +
                     "FROM produtos p JOIN estoque e ON p.id_produto = e.id_produto";
        
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
}