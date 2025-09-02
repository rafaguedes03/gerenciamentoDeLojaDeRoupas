package com.loja.amor_de_mamae.dao;

import com.loja.amor_de_mamae.model.RelatorioVenda;
import com.loja.amor_de_mamae.model.RelatorioEstoque;
import com.loja.amor_de_mamae.model.RelatorioFiado;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RelatorioDAO {
    
    // Relatório de Vendas
    public List<RelatorioVenda> gerarRelatorioVendas(LocalDate dataInicio, LocalDate dataFim) throws Exception {
        List<RelatorioVenda> vendas = new ArrayList<>();
        String sql = "SELECT v.data_venda, p.nome as produto, iv.preco_unitario, iv.quantidade, " +
                     "v.forma_pagamento, (iv.preco_unitario * iv.quantidade) as total_item " +
                     "FROM venda v " +
                     "JOIN itemvenda iv ON v.id_venda = iv.id_venda " +
                     "JOIN estoque e ON iv.id_estoque = e.id_estoque " +
                     "JOIN produtos p ON e.id_produto = p.id_produto " +
                     "WHERE DATE(v.data_venda) BETWEEN ? AND ? " +
                     "ORDER BY v.data_venda DESC";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDate(1, Date.valueOf(dataInicio));
            stmt.setDate(2, Date.valueOf(dataFim));
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    RelatorioVenda venda = new RelatorioVenda();
                    venda.setData(rs.getDate("data_venda").toLocalDate());
                    venda.setDescricao(rs.getString("produto"));
                    venda.setValor(rs.getDouble("total_item"));
                    venda.setQuantidade(rs.getInt("quantidade"));
                    venda.setFormaPagamento(rs.getString("forma_pagamento"));
                    vendas.add(venda);
                }
            }
        }
        return vendas;
    }
    
    public List<RelatorioVenda> gerarResumoVendasPorDia(LocalDate dataInicio, LocalDate dataFim) throws Exception {
        List<RelatorioVenda> resumo = new ArrayList<>();
        String sql = "SELECT DATE(v.data_venda) as data, " +
                     "COUNT(*) as total_vendas, " +
                     "SUM(v.total_venda) as total_valor, " +
                     "GROUP_CONCAT(DISTINCT v.forma_pagamento) as formas_pagamento " +
                     "FROM venda v " +
                     "WHERE DATE(v.data_venda) BETWEEN ? AND ? " +
                     "GROUP BY DATE(v.data_venda) " +
                     "ORDER BY data DESC";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDate(1, Date.valueOf(dataInicio));
            stmt.setDate(2, Date.valueOf(dataFim));
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    RelatorioVenda resumoDia = new RelatorioVenda();
                    resumoDia.setData(rs.getDate("data").toLocalDate());
                    resumoDia.setDescricao("Vendas do dia - " + rs.getString("formas_pagamento"));
                    resumoDia.setValor(rs.getDouble("total_valor"));
                    resumoDia.setQuantidade(rs.getInt("total_vendas"));
                    resumo.add(resumoDia);
                }
            }
        }
        return resumo;
    }
    
    // Relatório de Estoque
    public List<RelatorioEstoque> gerarRelatorioEstoque() throws Exception {
        List<RelatorioEstoque> estoque = new ArrayList<>();
        String sql = "SELECT p.nome as produto, p.codigo, e.tamanho, e.quantidade, " +
                     "(p.preco * e.quantidade) as valor_total " +
                     "FROM produtos p " +
                     "JOIN estoque e ON p.id_produto = e.id_produto " +
                     "ORDER BY p.nome, e.tamanho";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                RelatorioEstoque item = new RelatorioEstoque();
                item.setProduto(rs.getString("produto"));
                item.setCodigo(rs.getString("codigo"));
                item.setTamanho(rs.getString("tamanho"));
                item.setQuantidade(rs.getInt("quantidade"));
                item.setValorTotal(rs.getDouble("valor_total"));
                estoque.add(item);
            }
        }
        return estoque;
    }
    
    public List<RelatorioEstoque> gerarRelatorioEstoqueBaixo(int limite) throws Exception {
        List<RelatorioEstoque> estoqueBaixo = new ArrayList<>();
        String sql = "SELECT p.nome as produto, p.codigo, e.tamanho, e.quantidade, " +
                     "(p.preco * e.quantidade) as valor_total " +
                     "FROM Produtos p " +
                     "JOIN estoque e ON p.id_produto = e.id_produto " +
                     "WHERE e.quantidade <= ? " +
                     "ORDER BY e.quantidade ASC";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, limite);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    RelatorioEstoque item = new RelatorioEstoque();
                    item.setProduto(rs.getString("produto"));
                    item.setCodigo(rs.getString("codigo"));
                    item.setTamanho(rs.getString("tamanho"));
                    item.setQuantidade(rs.getInt("quantidade"));
                    item.setValorTotal(rs.getDouble("valor_total"));
                    estoqueBaixo.add(item);
                }
            }
        }
        return estoqueBaixo;
    }
    
    // Relatório de Fiados
    public List<RelatorioFiado> gerarRelatorioFiados() throws Exception {
        List<RelatorioFiado> fiados = new ArrayList<>();
        String sql = "SELECT c.nome as cliente, c.cpf, v.data_venda, v.total_venda, " +
                     "f.status, v.num_parcelas, " +
                     "(SELECT COUNT(*) FROM parcelafiado pf WHERE pf.id_fiado = f.id_fiado AND pf.status = 'paga') as parcelas_pagas, " +
                     "(SELECT SUM(pf.valor_parcela) FROM parcelafiado pf WHERE pf.id_fiado = f.id_fiado AND pf.status != 'paga') as valor_pendente " +
                     "FROM fiado f " +
                     "JOIN venda v ON f.id_venda = v.id_venda " +
                     "JOIN cliente c ON v.id_cliente = c.id_cliente " +
                     "WHERE f.status = 'ativo' " +
                     "ORDER BY v.data_venda DESC";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                RelatorioFiado fiado = new RelatorioFiado();
                fiado.setCliente(rs.getString("cliente"));
                fiado.setCpf(rs.getString("cpf"));
                fiado.setDataVenda(rs.getDate("data_venda").toLocalDate());
                fiado.setTotal(rs.getDouble("total_venda"));
                fiado.setParcelasPagas(rs.getInt("parcelas_pagas"));
                fiado.setParcelasTotal(rs.getInt("num_parcelas"));
                fiado.setValorPendente(rs.getDouble("valor_pendente"));
                fiado.setStatus(rs.getString("status"));
                fiados.add(fiado);
            }
        }
        return fiados;
    }
    
    public List<RelatorioFiado> gerarRelatorioFiadosAtrasados() throws Exception {
        List<RelatorioFiado> fiadosAtrasados = new ArrayList<>();
        String sql = "SELECT c.nome as cliente, c.cpf, v.data_venda, v.total_venda, " +
                     "f.status, v.num_parcelas, " +
                     "(SELECT COUNT(*) FROM parcelafiado pf WHERE pf.id_fiado = f.id_fiado AND pf.status = 'paga') as parcelas_pagas, " +
                     "(SELECT SUM(pf.valor_parcela) FROM parcelafiado pf WHERE pf.id_fiado = f.id_fiado AND pf.status = 'vencida') as valor_atrasado " +
                     "FROM fiado f " +
                     "JOIN venda v ON f.id_venda = v.id_venda " +
                     "JOIN cliente c ON v.id_cliente = c.id_cliente " +
                     "WHERE EXISTS (SELECT 1 FROM ParcelaFiado pf WHERE pf.id_fiado = f.id_fiado AND pf.status = 'vencida') " +
                     "ORDER BY valor_atrasado DESC";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                RelatorioFiado fiado = new RelatorioFiado();
                fiado.setCliente(rs.getString("cliente"));
                fiado.setCpf(rs.getString("cpf"));
                fiado.setDataVenda(rs.getDate("data_venda").toLocalDate());
                fiado.setTotal(rs.getDouble("total_venda"));
                fiado.setParcelasPagas(rs.getInt("parcelas_pagas"));
                fiado.setParcelasTotal(rs.getInt("num_parcelas"));
                fiado.setValorPendente(rs.getDouble("valor_atrasado"));
                fiado.setStatus("ATRASADO");
                fiadosAtrasados.add(fiado);
            }
        }
        return fiadosAtrasados;
    }
    
    // Métodos para estatísticas
    public double getTotalVendasPeriodo(LocalDate dataInicio, LocalDate dataFim) throws Exception {
        String sql = "SELECT COALESCE(SUM(total_venda), 0) as total " +
                     "FROM venda " +
                     "WHERE DATE(data_venda) BETWEEN ? AND ?";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDate(1, Date.valueOf(dataInicio));
            stmt.setDate(2, Date.valueOf(dataFim));
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("total");
                }
            }
        }
        return 0;
    }
    
    public int getTotalVendasCountPeriodo(LocalDate dataInicio, LocalDate dataFim) throws Exception {
        String sql = "SELECT COUNT(*) as total " +
                     "FROM venda " +
                     "WHERE DATE(data_venda) BETWEEN ? AND ?";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDate(1, Date.valueOf(dataInicio));
            stmt.setDate(2, Date.valueOf(dataFim));
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        }
        return 0;
    }
    
    public double getTotalFiadosPendentes() throws Exception {
        String sql = "SELECT COALESCE(SUM(pf.valor_parcela), 0) as total " +
                     "FROM parcelafiado pf " +
                     "JOIN fiado f ON pf.id_fiado = f.id_fiado " +
                     "WHERE pf.status != 'paga' AND f.status = 'ativo'";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getDouble("total");
            }
        }
        return 0;
    }
    
    public double getValorTotalEstoque() throws Exception {
        String sql = "SELECT COALESCE(SUM(p.preco * e.quantidade), 0) as total " +
                     "FROM produtos p " +
                     "JOIN estoque e ON p.id_produto = e.id_produto";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getDouble("total");
            }
        }
        return 0;
    }
}