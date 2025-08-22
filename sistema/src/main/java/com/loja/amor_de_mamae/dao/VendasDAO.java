package com.loja.amor_de_mamae.dao;

import com.loja.amor_de_mamae.model.*;
import com.loja.amor_de_mamae.util.ConexaoMySQL;
import java.sql.Connection;
import java.sql.PreparedStatement;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

public class VendasDAO {

    // Registrar uma nova venda
    public int registrarVenda(Vendas venda) {
        String sql = "INSERT INTO Venda (id_cliente, id_caixa, data_venda, total_venda, forma_pagamento, status_parcelado, num_parcelas) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexaoMySQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            if (venda.getId_cliente() != null) {
                stmt.setInt(1, venda.getId_cliente());
            } else {
                stmt.setNull(1, Types.INTEGER);
            }

            stmt.setInt(2, venda.getId_caixa());
            stmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setDouble(4, venda.getTotal_venda().doubleValue());
            stmt.setString(5, venda.getForma_pagamento());
            stmt.setString(6, venda.getStatus_parcelado());
            stmt.setInt(7, venda.getNum_parcelas());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int idVenda = rs.getInt(1);
                venda.setId_venda(idVenda);
                return idVenda;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1; // erro
    }

    // Registrar os itens da venda
    public void registrarItensVenda(int idVenda, List<ItemVenda> itens) {
        String sql = "INSERT INTO ItemVenda (id_venda, id_estoque, quantidade, preco_unitario, subtotal) "
                   + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConexaoMySQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (ItemVenda item : itens) {
                stmt.setInt(1, idVenda);
                stmt.setInt(2, item.getId_estoque());
                stmt.setInt(3, item.getQuantidade());
                stmt.setDouble(4, item.getPreco_unitario());
                stmt.setDouble(5, item.getSubtotal());
                stmt.addBatch();

                // Atualizar estoque
                atualizarEstoque(item.getId_estoque(), item.getQuantidade());
            }

            stmt.executeBatch();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Atualizar o estoque após uma venda
    private void atualizarEstoque(int idEstoque, int quantidadeVendida) {
        String sql = "UPDATE Estoque SET quantidade = quantidade - ? WHERE id_estoque = ?";

        try (Connection conn = ConexaoMySQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, quantidadeVendida);
            stmt.setInt(2, idEstoque);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Caso seja fiado, registrar na tabela Fiado
    public int registrarFiado(int idVenda) {
        String sql = "INSERT INTO Fiado (id_venda, status) VALUES (?, 'ativo')";

        try (Connection conn = ConexaoMySQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, idVenda);
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    // Registrar parcelas do fiado
    public void registrarParcelas(int idFiado, List<ParcelaFiado> parcelas) {
        String sql = "INSERT INTO ParcelaFiado (id_fiado, numero_parcela, valor_parcela, data_vencimento, status) "
                   + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConexaoMySQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (ParcelaFiado parcela : parcelas) {
                stmt.setInt(1, idFiado);
                stmt.setInt(2, parcela.getNumero_parcela());
                stmt.setDouble(3, parcela.getValor_parcela());
                stmt.setDate(idFiado, null);
                stmt.setString(5, parcela.getStatus());
                stmt.addBatch();
            }

            stmt.executeBatch();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
