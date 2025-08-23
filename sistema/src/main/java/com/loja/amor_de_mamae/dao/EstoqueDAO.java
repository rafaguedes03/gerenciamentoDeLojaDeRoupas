package com.loja.amor_de_mamae.dao;

import com.loja.amor_de_mamae.model.Estoque;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class EstoqueDAO {

    public void salvar(Estoque estoque) throws Exception {
        String sql = "INSERT INTO Estoque (id_produto, tamanho, quantidade) VALUES (?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, estoque.getIdProduto());
            stmt.setString(2, estoque.getTamanho());
            stmt.setInt(3, estoque.getQuantidade());
            stmt.executeUpdate();
        }
    }

    public void atualizar(Estoque estoque) throws Exception {
        String sql = "UPDATE Estoque SET tamanho = ?, quantidade = ? WHERE id_produto = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, estoque.getTamanho());
            stmt.setInt(2, estoque.getQuantidade());
            stmt.setInt(3, estoque.getIdProduto());
            stmt.executeUpdate();
        }
    }

    public void excluirPorProduto(int idProduto) throws Exception {
        String sql = "DELETE FROM Estoque WHERE id_produto = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idProduto);
            stmt.executeUpdate();
        }
    }


}