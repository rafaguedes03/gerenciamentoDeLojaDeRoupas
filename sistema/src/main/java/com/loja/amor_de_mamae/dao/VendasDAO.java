package com.loja.amor_de_mamae.dao;

import com.loja.amor_de_mamae.model.Vendas;
import com.loja.amor_de_mamae.model.ItemVenda;

import java.sql.*;
import java.util.List;

public class VendasDAO {
    
    public int salvarVenda(Vendas venda, List<ItemVenda> itens) throws Exception {
        Connection conn = null;
        PreparedStatement stmtVenda = null;
        PreparedStatement stmtItem = null;
        ResultSet rs = null;
        
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false); // Iniciar transação
            
            // Inserir venda
            String sqlVenda = "INSERT INTO venda (id_cliente, id_caixa, data_venda, total_venda, forma_pagamento, status_parcelado, num_parcelas) VALUES (?, ?, ?, ?, ?, ?, ?)";
            stmtVenda = conn.prepareStatement(sqlVenda, Statement.RETURN_GENERATED_KEYS);
            
            stmtVenda.setObject(1, venda.getId_cliente() > 0 ? venda.getId_cliente() : null);
            stmtVenda.setInt(2, venda.getId_caixa());
            stmtVenda.setTimestamp(3, Timestamp.valueOf(venda.getData_venda()));
            stmtVenda.setDouble(4, venda.getTotal_venda());
            stmtVenda.setString(5, venda.getForma_pagamento());
            stmtVenda.setString(6, venda.getStatus_parcelado());
            stmtVenda.setInt(7, venda.getNum_parcelas());
            
            stmtVenda.executeUpdate();
            
            // Obter ID da venda
            rs = stmtVenda.getGeneratedKeys();
            int idVenda = 0;
            if (rs.next()) {
                idVenda = rs.getInt(1);
            }
            
            // Inserir itens da venda
            String sqlItem = "INSERT INTO itemvenda (id_venda, id_estoque, quantidade, preco_unitario, subtotal) VALUES (?, ?, ?, ?, ?)";
            stmtItem = conn.prepareStatement(sqlItem);
            
            for (ItemVenda item : itens) {
                stmtItem.setInt(1, idVenda);
                stmtItem.setInt(2, item.getId_estoque());
                stmtItem.setInt(3, item.getQuantidade());
                stmtItem.setDouble(4, item.getPreco_unitario());
                stmtItem.setDouble(5, item.getSubtotal());
                stmtItem.addBatch();
                
                // Atualizar estoque
                atualizarEstoque(conn, item.getId_estoque(), item.getQuantidade());
            }
            
            stmtItem.executeBatch();
            
            conn.commit(); // Commit da transação
            return idVenda;
            
        } catch (Exception e) {
            if (conn != null) {
                conn.rollback(); // Rollback em caso de erro
            }
            throw e;
        } finally {
            if (rs != null) rs.close();
            if (stmtItem != null) stmtItem.close();
            if (stmtVenda != null) stmtVenda.close();
            if (conn != null) conn.close();
        }
    }
    
    private void atualizarEstoque(Connection conn, int idEstoque, int quantidade) throws SQLException {
        String sql = "UPDATE estoque SET quantidade = quantidade - ? WHERE id_estoque = ? AND quantidade >= ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, quantidade);
            stmt.setInt(2, idEstoque);
            stmt.setInt(3, quantidade);
            stmt.executeUpdate();
        }
    }
}