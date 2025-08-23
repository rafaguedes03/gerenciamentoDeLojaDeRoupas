package com.loja.amor_de_mamae.dao;

import com.loja.amor_de_mamae.model.Fiado;
import com.loja.amor_de_mamae.model.ParcelaFiado;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FiadoDAO {
    
    public List<Fiado> listarTodosFiados() throws Exception {
        List<Fiado> fiados = new ArrayList<>();
        String sql = "SELECT f.id_fiado, f.id_venda, f.status, v.data_venda, c.nome as nome_cliente, " +
                     "v.total_venda, v.num_parcelas, " +
                     "(SELECT COUNT(*) FROM ParcelaFiado pf WHERE pf.id_fiado = f.id_fiado AND pf.status = 'paga') as parcelas_pagas " +
                     "FROM Fiado f " +
                     "JOIN Venda v ON f.id_venda = v.id_venda " +
                     "JOIN Cliente c ON v.id_cliente = c.id_cliente " +
                     "ORDER BY v.data_venda DESC";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Fiado fiado = new Fiado();
                fiado.setId_fiado(rs.getInt("id_fiado"));
                fiado.setId_venda(rs.getInt("id_venda"));
                fiado.setStatus(rs.getString("status"));
                fiado.setDataVenda(rs.getDate("data_venda").toLocalDate());
                fiado.setNomeCliente(rs.getString("nome_cliente"));
                fiado.setTotalVenda(rs.getDouble("total_venda"));
                fiado.setNumParcelas(rs.getInt("num_parcelas"));
                fiado.setParcelasPagas(rs.getInt("parcelas_pagas"));
                fiados.add(fiado);
            }
        }
        return fiados;
    }
    
    public List<Fiado> buscarFiadosPorCliente(String nomeCliente) throws Exception {
        List<Fiado> fiados = new ArrayList<>();
        String sql = "SELECT f.id_fiado, f.id_venda, f.status, v.data_venda, c.nome as nome_cliente, " +
                     "v.total_venda, v.num_parcelas, " +
                     "(SELECT COUNT(*) FROM ParcelaFiado pf WHERE pf.id_fiado = f.id_fiado AND pf.status = 'paga') as parcelas_pagas " +
                     "FROM Fiado f " +
                     "JOIN Venda v ON f.id_venda = v.id_venda " +
                     "JOIN Cliente c ON v.id_cliente = c.id_cliente " +
                     "WHERE c.nome LIKE ? " +
                     "ORDER BY v.data_venda DESC";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + nomeCliente + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Fiado fiado = new Fiado();
                    fiado.setId_fiado(rs.getInt("id_fiado"));
                    fiado.setId_venda(rs.getInt("id_venda"));
                    fiado.setStatus(rs.getString("status"));
                    fiado.setDataVenda(rs.getDate("data_venda").toLocalDate());
                    fiado.setNomeCliente(rs.getString("nome_cliente"));
                    fiado.setTotalVenda(rs.getDouble("total_venda"));
                    fiado.setNumParcelas(rs.getInt("num_parcelas"));
                    fiado.setParcelasPagas(rs.getInt("parcelas_pagas"));
                    fiados.add(fiado);
                }
            }
        }
        return fiados;
    }
    
    public List<ParcelaFiado> listarParcelasPorFiado(int idFiado) throws Exception {
        List<ParcelaFiado> parcelas = new ArrayList<>();
        String sql = "SELECT * FROM ParcelaFiado WHERE id_fiado = ? ORDER BY numero_parcela";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idFiado);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ParcelaFiado parcela = new ParcelaFiado();
                    parcela.setId_parcela(rs.getInt("id_parcela"));
                    parcela.setId_fiado(rs.getInt("id_fiado"));
                    parcela.setNumero_parcela(rs.getInt("numero_parcela"));
                    parcela.setValor_parcela(rs.getDouble("valor_parcela"));
                    parcela.setData_vencimento(rs.getDate("data_vencimento").toLocalDate());
                    parcela.setStatus(rs.getString("status"));
                    parcelas.add(parcela);
                }
            }
        }
        return parcelas;
    }
    
    public void marcarParcelaComoPaga(int idParcela) throws Exception {
        String sql = "UPDATE ParcelaFiado SET status = 'paga' WHERE id_parcela = ?";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idParcela);
            stmt.executeUpdate();
        }
    }
    
    public void atualizarStatusFiado(int idFiado) throws Exception {
        // Verificar se todas as parcelas estão pagas
        String sqlCheck = "SELECT COUNT(*) as pendentes FROM ParcelaFiado WHERE id_fiado = ? AND status != 'paga'";
        String sqlUpdate = "UPDATE Fiado SET status = ? WHERE id_fiado = ?";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmtCheck = conn.prepareStatement(sqlCheck);
             PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdate)) {
            
            stmtCheck.setInt(1, idFiado);
            try (ResultSet rs = stmtCheck.executeQuery()) {
                if (rs.next()) {
                    int pendentes = rs.getInt("pendentes");
                    String status = pendentes == 0 ? "pago" : "ativo";
                    
                    stmtUpdate.setString(1, status);
                    stmtUpdate.setInt(2, idFiado);
                    stmtUpdate.executeUpdate();
                }
            }
        }
    }
    
    public void criarFiado(int idVenda, int numParcelas, double totalVenda) throws Exception {
        Connection conn = null;
        PreparedStatement stmtFiado = null;
        PreparedStatement stmtParcela = null;
        
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            
            // Verificar se já existe fiado para esta venda
            String sqlCheck = "SELECT COUNT(*) FROM Fiado WHERE id_venda = ?";
            try (PreparedStatement stmtCheck = conn.prepareStatement(sqlCheck)) {
                stmtCheck.setInt(1, idVenda);
                try (ResultSet rs = stmtCheck.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        throw new Exception("Já existe um fiado para esta venda");
                    }
                }
            }
            
            // Inserir fiado
            String sqlFiado = "INSERT INTO Fiado (id_venda, status) VALUES (?, 'ativo')";
            stmtFiado = conn.prepareStatement(sqlFiado, Statement.RETURN_GENERATED_KEYS);
            stmtFiado.setInt(1, idVenda);
            stmtFiado.executeUpdate();
            
            // Obter ID do fiado
            int idFiado = 0;
            try (ResultSet rs = stmtFiado.getGeneratedKeys()) {
                if (rs.next()) {
                    idFiado = rs.getInt(1);
                }
            }
            
            // Calcular valor da parcela
            double valorParcela = totalVenda / numParcelas;
            
            // Inserir parcelas
            String sqlParcela = "INSERT INTO ParcelaFiado (id_fiado, numero_parcela, valor_parcela, data_vencimento, status) VALUES (?, ?, ?, ?, 'a vencer')";
            stmtParcela = conn.prepareStatement(sqlParcela);
            
            LocalDate dataAtual = LocalDate.now();
            for (int i = 1; i <= numParcelas; i++) {
                LocalDate dataVencimento = dataAtual.plusMonths(i);
                stmtParcela.setInt(1, idFiado);
                stmtParcela.setInt(2, i);
                stmtParcela.setDouble(3, valorParcela);
                stmtParcela.setDate(4, Date.valueOf(dataVencimento));
                stmtParcela.addBatch();
            }
            
            stmtParcela.executeBatch();
            conn.commit();
            
        } catch (Exception e) {
            if (conn != null) {
                conn.rollback();
            }
            throw e;
        } finally {
            if (stmtParcela != null) stmtParcela.close();
            if (stmtFiado != null) stmtFiado.close();
            if (conn != null) conn.close();
        }
    }
}