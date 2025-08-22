package com.loja.amor_de_mamae.dao;

import com.loja.amor_de_mamae.model.Caixa;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

public class CaixaDAO {

    // Metodo para verificar se o caixa está aberto
    public boolean isCaixaAberto() throws Exception {
        String sql = "SELECT aberto FROM caixa WHERE aberto = true ORDER BY id_caixa DESC LIMIT 1";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getBoolean("aberto");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Método para abrir o caixa
    public void abrirCaixa(Caixa caixa) throws Exception {
        String sql = "INSERT INTO caixa (data_abertura, saldo_inicial, aberto) VALUES (?, ?, ?)";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
             
            stmt.setTimestamp(1, Timestamp.valueOf(caixa.getData_abertura()));
            stmt.setDouble(2, caixa.getSaldo_inicial());
            stmt.setBoolean(3, caixa.getAberto());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        caixa.setId_caixa(generatedKeys.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para fechar o caixa
    public void fecharCaixa(int idCaixa, Caixa caixa) throws Exception {
        String sql = "UPDATE caixa SET data_fechamento = ?, saldo_final = ?, aberto = ? WHERE id_caixa = ?";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setTimestamp(1, Timestamp.valueOf(caixa.getData_fechamento()));
            stmt.setDouble(2, caixa.getSaldo_final());
            stmt.setBoolean(3, false);
            stmt.setInt(4, caixa.getId_caixa());
                
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
