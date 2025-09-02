package com.loja.amor_de_mamae.dao;

import com.loja.amor_de_mamae.model.Caixa;
import com.loja.amor_de_mamae.util.ConexaoMySQL;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

public class CaixaDAO {
    private Connection conn;
    public CaixaDAO(Connection conn) {
        this.conn = conn;
    }

    public CaixaDAO() {
        try {
            this.conn = ConexaoMySQL.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Metodo para verificar se o caixa está aberto
    public boolean isCaixaAberto() throws Exception {
        String sql = "SELECT aberto FROM caixa WHERE aberto = true ORDER BY id_caixa DESC LIMIT 1";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
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
    public void abrirCaixa(Caixa caixa) {
        String sql = "INSERT INTO caixa (data_abertura, saldo_inicial, aberto) VALUES (?, ?, ?)";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, caixa.getData_abertura().toString());
            pstmt.setDouble(2, caixa.getSaldo_inicial());
            pstmt.setBoolean(3, true);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
    public Caixa obterCaixaAberto() throws Exception {
        String sql = "SELECT * FROM caixa WHERE aberto = true ORDER BY data_abertura DESC LIMIT 1";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                Caixa caixa = new Caixa();
                caixa.setId_caixa(rs.getInt("id_caixa"));
                caixa.setData_abertura(rs.getTimestamp("data_abertura").toLocalDateTime());
                caixa.setSaldo_inicial(rs.getDouble("saldo_inicial"));
                caixa.setAberto(rs.getBoolean("aberto"));
                
                // Campos que podem ser null
                if (rs.getTimestamp("data_fechamento") != null) {
                    caixa.setData_fechamento(rs.getTimestamp("data_fechamento").toLocalDateTime());
                }
                if (rs.getObject("saldo_final") != null) {
                    caixa.setSaldo_final(rs.getDouble("saldo_final"));
                }
                if (rs.getObject("valor_vendas") != null) {
                    caixa.setValor_vendas(rs.getDouble("valor_vendas"));
                }
                
                return caixa;
            }
        }
        return null;
    }

    public void fecharCaixa(Caixa caixa) throws Exception {
        String sql = "UPDATE caixa SET data_fechamento = ?, saldo_final = ?, valor_vendas = ?, aberto = ? WHERE id_caixa = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setTimestamp(1, java.sql.Timestamp.valueOf(caixa.getData_fechamento()));
            stmt.setDouble(2, caixa.getSaldo_final());
            stmt.setDouble(3, caixa.getValor_vendas());
            stmt.setBoolean(4, false);
            stmt.setInt(5, caixa.getId_caixa());
            
            stmt.executeUpdate();
        }
    }
}
