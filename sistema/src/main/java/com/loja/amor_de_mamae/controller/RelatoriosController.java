package com.loja.amor_de_mamae.controller;

import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.loja.amor_de_mamae.dao.RelatorioDAO;
import com.loja.amor_de_mamae.model.RelatorioVenda;
import com.loja.amor_de_mamae.model.RelatorioEstoque;
import com.loja.amor_de_mamae.model.RelatorioFiado;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.scene.text.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import com.itextpdf.text.Document;
import com.itextpdf.text.Font;


public class RelatoriosController {

    @FXML private ChoiceBox<String> choiceTipoRelatorio;
    @FXML private ChoiceBox<String> choicePeriodo;
    @FXML private DatePicker dateInicial;
    @FXML private DatePicker dateFinal;
    
    @FXML private TableView<Object> tableViewResultados;
    @FXML private TableColumn<Object, String> colunaDescricao;
    @FXML private TableColumn<Object, String> colunaValor;
    @FXML private TableColumn<Object, String> colunaQuantidade;
    @FXML private TableColumn<Object, String> colunaData;
    
    @FXML private Text textTotalGeral;
    @FXML private Text textTotalRegistros;
    @FXML private Text textMedia;
    @FXML private TextArea textAreaResumo;

    private RelatorioDAO relatorioDAO = new RelatorioDAO();
    private ObservableList<Object> listaResultados = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        configurarFiltros();
        configurarTabela();
        configurarPeriodoPadrao();
    }

    private void configurarFiltros() {
        // Tipos de relatório
        choiceTipoRelatorio.setItems(FXCollections.observableArrayList(
            "Vendas - Detalhado",
            "Vendas - Resumo por Dia",
            "Estoque - Completo",
            "Estoque - Baixo",
            "Fiados - Ativos",
            "Fiados - Atrasados"
        ));
        choiceTipoRelatorio.setValue("Vendas - Detalhado");

        // Períodos pré-definidos
        choicePeriodo.setItems(FXCollections.observableArrayList(
            "Hoje",
            "Esta Semana",
            "Este Mês",
            "Últimos 7 dias",
            "Últimos 30 dias",
            "Personalizado"
        ));
        choicePeriodo.setValue("Este Mês");

        // Listeners
        choicePeriodo.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            configurarPeriodoAutomatico(newValue);
        });
    }

    private void configurarTabela() {
        colunaDescricao.setCellValueFactory(cellData -> {
            Object item = cellData.getValue();
            if (item instanceof RelatorioVenda) {
                return new javafx.beans.property.SimpleStringProperty(((RelatorioVenda) item).getDescricao());
            } else if (item instanceof RelatorioEstoque) {
                return new javafx.beans.property.SimpleStringProperty(((RelatorioEstoque) item).getProduto());
            } else if (item instanceof RelatorioFiado) {
                return new javafx.beans.property.SimpleStringProperty(((RelatorioFiado) item).getCliente());
            }
            return new javafx.beans.property.SimpleStringProperty("");
        });

        colunaValor.setCellValueFactory(cellData -> {
            Object item = cellData.getValue();
            NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
            if (item instanceof RelatorioVenda) {
                return new javafx.beans.property.SimpleStringProperty(format.format(((RelatorioVenda) item).getValor()));
            } else if (item instanceof RelatorioEstoque) {
                return new javafx.beans.property.SimpleStringProperty(format.format(((RelatorioEstoque) item).getValorTotal()));
            } else if (item instanceof RelatorioFiado) {
                return new javafx.beans.property.SimpleStringProperty(format.format(((RelatorioFiado) item).getValorPendente()));
            }
            return new javafx.beans.property.SimpleStringProperty("");
        });

        colunaQuantidade.setCellValueFactory(cellData -> {
            Object item = cellData.getValue();
            if (item instanceof RelatorioVenda) {
                return new javafx.beans.property.SimpleStringProperty(String.valueOf(((RelatorioVenda) item).getQuantidade()));
            } else if (item instanceof RelatorioEstoque) {
                return new javafx.beans.property.SimpleStringProperty(String.valueOf(((RelatorioEstoque) item).getQuantidade()));
            } else if (item instanceof RelatorioFiado) {
                RelatorioFiado fiado = (RelatorioFiado) item;
                return new javafx.beans.property.SimpleStringProperty(fiado.getParcelasPagas() + "/" + fiado.getParcelasTotal());
            }
            return new javafx.beans.property.SimpleStringProperty("");
        });

        colunaData.setCellValueFactory(cellData -> {
            Object item = cellData.getValue();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            if (item instanceof RelatorioVenda) {
                return new javafx.beans.property.SimpleStringProperty(((RelatorioVenda) item).getData().format(formatter));
            } else if (item instanceof RelatorioFiado) {
                return new javafx.beans.property.SimpleStringProperty(((RelatorioFiado) item).getDataVenda().format(formatter));
            }
            return new javafx.beans.property.SimpleStringProperty("");
        });

        tableViewResultados.setItems(listaResultados);
    }

    private void configurarPeriodoPadrao() {
        configurarPeriodoAutomatico("Este Mês");
    }

    private void configurarPeriodoAutomatico(String periodo) {
        LocalDate hoje = LocalDate.now();
        
        switch (periodo) {
            case "Hoje":
                dateInicial.setValue(hoje);
                dateFinal.setValue(hoje);
                break;
            case "Esta Semana":
                dateInicial.setValue(hoje.minusDays(hoje.getDayOfWeek().getValue() - 1));
                dateFinal.setValue(hoje);
                break;
            case "Este Mês":
                dateInicial.setValue(hoje.withDayOfMonth(1));
                dateFinal.setValue(hoje);
                break;
            case "Últimos 7 dias":
                dateInicial.setValue(hoje.minusDays(6));
                dateFinal.setValue(hoje);
                break;
            case "Últimos 30 dias":
                dateInicial.setValue(hoje.minusDays(29));
                dateFinal.setValue(hoje);
                break;
            case "Personalizado":
                // Mantém as datas atuais
                break;
        }
    }

    @FXML
    private void gerarRelatorio() {
        try {
            if (dateInicial.getValue() == null || dateFinal.getValue() == null) {
                mostrarAlerta("Erro", "Selecione as datas inicial e final!", Alert.AlertType.ERROR);
                return;
            }

            if (dateInicial.getValue().isAfter(dateFinal.getValue())) {
                mostrarAlerta("Erro", "Data inicial não pode ser maior que data final!", Alert.AlertType.ERROR);
                return;
            }

            String tipoRelatorio = choiceTipoRelatorio.getValue();
            LocalDate dataInicio = dateInicial.getValue();
            LocalDate dataFim = dateFinal.getValue();

            listaResultados.clear();
            double totalGeral = 0;
            int totalRegistros = 0;

            switch (tipoRelatorio) {
                case "Vendas - Detalhado":
                    List<RelatorioVenda> vendas = relatorioDAO.gerarRelatorioVendas(dataInicio, dataFim);
                    listaResultados.addAll(vendas);
                    totalGeral = vendas.stream().mapToDouble(RelatorioVenda::getValor).sum();
                    totalRegistros = vendas.size();
                    break;

                case "Vendas - Resumo por Dia":
                    List<RelatorioVenda> resumoVendas = relatorioDAO.gerarResumoVendasPorDia(dataInicio, dataFim);
                    listaResultados.addAll(resumoVendas);
                    totalGeral = resumoVendas.stream().mapToDouble(RelatorioVenda::getValor).sum();
                    totalRegistros = resumoVendas.size();
                    break;

                case "Estoque - Completo":
                    List<RelatorioEstoque> estoque = relatorioDAO.gerarRelatorioEstoque();
                    listaResultados.addAll(estoque);
                    totalGeral = estoque.stream().mapToDouble(RelatorioEstoque::getValorTotal).sum();
                    totalRegistros = estoque.size();
                    break;

                case "Estoque - Baixo":
                    List<RelatorioEstoque> estoqueBaixo = relatorioDAO.gerarRelatorioEstoqueBaixo(10); // Limite de 10 unidades
                    listaResultados.addAll(estoqueBaixo);
                    totalGeral = estoqueBaixo.stream().mapToDouble(RelatorioEstoque::getValorTotal).sum();
                    totalRegistros = estoqueBaixo.size();
                    break;

                case "Fiados - Ativos":
                    List<RelatorioFiado> fiados = relatorioDAO.gerarRelatorioFiados();
                    listaResultados.addAll(fiados);
                    totalGeral = fiados.stream().mapToDouble(RelatorioFiado::getValorPendente).sum();
                    totalRegistros = fiados.size();
                    break;

                case "Fiados - Atrasados":
                    List<RelatorioFiado> fiadosAtrasados = relatorioDAO.gerarRelatorioFiadosAtrasados();
                    listaResultados.addAll(fiadosAtrasados);
                    totalGeral = fiadosAtrasados.stream().mapToDouble(RelatorioFiado::getValorPendente).sum();
                    totalRegistros = fiadosAtrasados.size();
                    break;
            }

            // Atualizar estatísticas
            atualizarEstatisticas(totalGeral, totalRegistros);
            
            // Gerar resumo textual
            gerarResumoTextual(tipoRelatorio, dataInicio, dataFim, totalGeral, totalRegistros);

        } catch (Exception e) {
            mostrarAlerta("Erro", "Erro ao gerar relatório: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void atualizarEstatisticas(double totalGeral, int totalRegistros) {
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        textTotalGeral.setText(format.format(totalGeral));
        textTotalRegistros.setText(String.valueOf(totalRegistros));
        
        double media = totalRegistros > 0 ? totalGeral / totalRegistros : 0;
        textMedia.setText(format.format(media));
    }

    private void gerarResumoTextual(String tipoRelatorio, LocalDate dataInicio, LocalDate dataFim, 
                                  double totalGeral, int totalRegistros) {
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        StringBuilder resumo = new StringBuilder();
        resumo.append("=== RELATÓRIO - ").append(tipoRelatorio.toUpperCase()).append(" ===\n");
        resumo.append("Período: ").append(dataInicio.format(dateFormatter))
              .append(" a ").append(dataFim.format(dateFormatter)).append("\n");
        resumo.append("Data de geração: ").append(LocalDate.now().format(dateFormatter)).append("\n");
        resumo.append("Total de registros: ").append(totalRegistros).append("\n");
        resumo.append("Valor total: ").append(format.format(totalGeral)).append("\n");
        
        if (totalRegistros > 0) {
            double media = totalGeral / totalRegistros;
            resumo.append("Média por registro: ").append(format.format(media)).append("\n");
        }
        
        resumo.append("\n=== DETALHES ===\n");
        
        // Adicionar detalhes específicos por tipo de relatório
        switch (tipoRelatorio) {
            case "Vendas - Detalhado":
                resumo.append("Relatório detalhado de todas as vendas do período.\n");
                break;
            case "Vendas - Resumo por Dia":
                resumo.append("Resumo consolidado de vendas por dia.\n");
                break;
            case "Estoque - Completo":
                resumo.append("Inventário completo do estoque atual.\n");
                break;
            case "Estoque - Baixo":
                resumo.append("Produtos com estoque baixo (≤ 10 unidades).\n");
                break;
            case "Fiados - Ativos":
                resumo.append("Clientes com fiados em aberto.\n");
                break;
            case "Fiados - Atrasados":
                resumo.append("Clientes com parcelas em atraso.\n");
                break;
        }
        
        textAreaResumo.setText(resumo.toString());
    }

    @FXML
    private void exportarRelatorio() {
        try {
            if (listaResultados.isEmpty()) {
                mostrarAlerta("Aviso", "Gere um relatório antes de exportar!", Alert.AlertType.WARNING);
                return;
            }

            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Selecionar pasta para salvar relatório");
            File selectedDirectory = directoryChooser.showDialog(null);

            if (selectedDirectory != null) {
                String tipoRelatorio = choiceTipoRelatorio.getValue();
                LocalDate dataInicio = dateInicial.getValue();
                LocalDate dataFim = dateFinal.getValue();
                
                String fileName = "Relatorio_" + tipoRelatorio.replace(" ", "_") + "_" +
                                LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + ".txt";
                
                File file = new File(selectedDirectory, fileName);
                
                try (FileWriter writer = new FileWriter(file)) {
                    writer.write(textAreaResumo.getText());
                    writer.write("\n\n=== DADOS DETALHADOS ===\n");
                    
                    NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
                    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    
                    for (Object item : listaResultados) {
                        if (item instanceof RelatorioVenda) {
                            RelatorioVenda venda = (RelatorioVenda) item;
                            writer.write(String.format("%s | %s | %d un | %s | %s\n",
                                venda.getData().format(dateFormatter),
                                venda.getDescricao(),
                                venda.getQuantidade(),
                                format.format(venda.getValor()),
                                venda.getFormaPagamento()));
                        } else if (item instanceof RelatorioEstoque) {
                            RelatorioEstoque estoque = (RelatorioEstoque) item;
                            writer.write(String.format("%s | %s | %s | %d un | %s\n",
                                estoque.getProduto(),
                                estoque.getCodigo(),
                                estoque.getTamanho(),
                                estoque.getQuantidade(),
                                format.format(estoque.getValorTotal())));
                        } else if (item instanceof RelatorioFiado) {
                            RelatorioFiado fiado = (RelatorioFiado) item;
                            writer.write(String.format("%s | %s | %s | %s | %d/%d parcelas\n",
                                fiado.getCliente(),
                                fiado.getCpf(),
                                fiado.getDataVenda().format(dateFormatter),
                                format.format(fiado.getValorPendente()),
                                fiado.getParcelasPagas(),
                                fiado.getParcelasTotal()));
                        }
                    }
                }
                
                mostrarAlerta("Sucesso", "Relatório exportado com sucesso!\nArquivo: " + file.getAbsolutePath(), Alert.AlertType.INFORMATION);
            }
        } catch (IOException e) {
            mostrarAlerta("Erro", "Erro ao exportar relatório: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
private void exportarParaPDF() {
    try {
        if (listaResultados.isEmpty()) {
            mostrarAlerta("Aviso", "Gere um relatório antes de exportar para PDF!", Alert.AlertType.WARNING);
            return;
        }

        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Salvar Relatório PDF");
        File selectedDirectory = directoryChooser.showDialog(null);

        if (selectedDirectory != null) {
            String tipoRelatorio = choiceTipoRelatorio.getValue();
            String fileName = "Relatorio_" + tipoRelatorio.replace(" ", "_") + "_" +
                            LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + ".pdf";
            
            File file = new File(selectedDirectory, fileName);
            
            // Usar iText corretamente com módulos
            com.itextpdf.text.Document document = new com.itextpdf.text.Document();
            com.itextpdf.text.pdf.PdfWriter.getInstance(document, new java.io.FileOutputStream(file));
            document.open();
            
            // Configuração de fontes
            com.itextpdf.text.Font titleFont = new com.itextpdf.text.Font(
                com.itextpdf.text.Font.FontFamily.HELVETICA, 18, com.itextpdf.text.Font.BOLD);
            com.itextpdf.text.Font headerFont = new com.itextpdf.text.Font(
                com.itextpdf.text.Font.FontFamily.HELVETICA, 12, com.itextpdf.text.Font.BOLD);
            com.itextpdf.text.Font normalFont = new com.itextpdf.text.Font(
                com.itextpdf.text.Font.FontFamily.HELVETICA, 12);
            com.itextpdf.text.Font smallFont = new com.itextpdf.text.Font(
                com.itextpdf.text.Font.FontFamily.HELVETICA, 10);
            
            // Título
            com.itextpdf.text.Paragraph title = new com.itextpdf.text.Paragraph(
                "RELATÓRIO - " + tipoRelatorio.toUpperCase(), titleFont);
            title.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);
            
            // Informações do relatório
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            
            document.add(new com.itextpdf.text.Paragraph(
                "Período: " + dateInicial.getValue().format(dateFormatter) + " a " + 
                dateFinal.getValue().format(dateFormatter), normalFont));
            
            document.add(new com.itextpdf.text.Paragraph(
                "Data de geração: " + LocalDate.now().format(dateFormatter), normalFont));
            
            document.add(new com.itextpdf.text.Paragraph(
                "Total de registros: " + textTotalRegistros.getText(), normalFont));
            
            document.add(new com.itextpdf.text.Paragraph(
                "Valor total: " + textTotalGeral.getText(), normalFont));
            
            document.add(new com.itextpdf.text.Paragraph(
                "Média por registro: " + textMedia.getText(), normalFont));
            
            document.add(new com.itextpdf.text.Paragraph(" "));
            
            // Detalhes do relatório
            com.itextpdf.text.Paragraph detalhes = new com.itextpdf.text.Paragraph(
                "=== DETALHES ===", headerFont);
            detalhes.setSpacingAfter(10);
            document.add(detalhes);
            
            String detalhesTexto = "";
            switch (tipoRelatorio) {
                case "Vendas - Detalhado":
                    detalhesTexto = "Relatório detalhado de todas as vendas do período.";
                    break;
                case "Vendas - Resumo por Dia":
                    detalhesTexto = "Resumo consolidado de vendas por dia.";
                    break;
                case "Estoque - Completo":
                    detalhesTexto = "Inventário completo do estoque atual.";
                    break;
                case "Estoque - Baixo":
                    detalhesTexto = "Produtos com estoque baixo (≤ 10 unidades).";
                    break;
                case "Fiados - Ativos":
                    detalhesTexto = "Clientes com fiados em aberto.";
                    break;
                case "Fiados - Atrasados":
                    detalhesTexto = "Clientes com parcelas em atraso.";
                    break;
            }
            
            document.add(new com.itextpdf.text.Paragraph(detalhesTexto, normalFont));
            document.add(new com.itextpdf.text.Paragraph(" "));
            
            // Dados detalhados
            com.itextpdf.text.Paragraph dadosTitulo = new com.itextpdf.text.Paragraph(
                "=== DADOS DETALHADOS ===", headerFont);
            dadosTitulo.setSpacingAfter(10);
            document.add(dadosTitulo);
            
            // Criar tabela
            com.itextpdf.text.pdf.PdfPTable table = new com.itextpdf.text.pdf.PdfPTable(5);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10);
            table.setSpacingAfter(20);
            
            // Cabeçalhos da tabela
            table.addCell(new com.itextpdf.text.Phrase("Data", headerFont));
            table.addCell(new com.itextpdf.text.Phrase("Nome", headerFont));
            table.addCell(new com.itextpdf.text.Phrase("Quantidade", headerFont));
            table.addCell(new com.itextpdf.text.Phrase("Valor", headerFont));
            table.addCell(new com.itextpdf.text.Phrase("Pagamento", headerFont));
            
            // Preencher tabela com dados
            NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
            
            for (Object item : listaResultados) {
                if (item instanceof RelatorioVenda) {
                    RelatorioVenda venda = (RelatorioVenda) item;
                    table.addCell(new com.itextpdf.text.Phrase(venda.getData().format(dateFormatter), normalFont));
                    table.addCell(new com.itextpdf.text.Phrase(venda.getDescricao(), normalFont));
                    table.addCell(new com.itextpdf.text.Phrase(String.valueOf(venda.getQuantidade()), normalFont));
                    table.addCell(new com.itextpdf.text.Phrase(format.format(venda.getValor()), normalFont));
                    table.addCell(new com.itextpdf.text.Phrase(venda.getFormaPagamento(), normalFont));
                } else if (item instanceof RelatorioEstoque) {
                    RelatorioEstoque estoque = (RelatorioEstoque) item;
                    table.addCell(new com.itextpdf.text.Phrase("N/A", normalFont));
                    table.addCell(new com.itextpdf.text.Phrase(estoque.getProduto(), normalFont));
                    table.addCell(new com.itextpdf.text.Phrase(String.valueOf(estoque.getQuantidade()), normalFont));
                    table.addCell(new com.itextpdf.text.Phrase(format.format(estoque.getValorTotal()), normalFont));
                    table.addCell(new com.itextpdf.text.Phrase("Estoque", normalFont));
                } else if (item instanceof RelatorioFiado) {
                    RelatorioFiado fiado = (RelatorioFiado) item;
                    table.addCell(new com.itextpdf.text.Phrase(fiado.getDataVenda().format(dateFormatter), normalFont));
                    table.addCell(new com.itextpdf.text.Phrase(fiado.getCliente(), normalFont));
                    table.addCell(new com.itextpdf.text.Phrase(
                        fiado.getParcelasPagas() + "/" + fiado.getParcelasTotal(), normalFont));
                    table.addCell(new com.itextpdf.text.Phrase(format.format(fiado.getValorPendente()), normalFont));
                    table.addCell(new com.itextpdf.text.Phrase("Fiado", normalFont));
                }
            }
            
            document.add(table);
            
            // Rodapé
            com.itextpdf.text.Paragraph footer = new com.itextpdf.text.Paragraph(
                "Relatório gerado pelo Sistema Amor de Mamãe - " + 
                LocalDate.now().format(dateFormatter), 
                smallFont);
            footer.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            document.add(footer);
            
            document.close();
            
            mostrarAlerta("Sucesso", "PDF exportado com sucesso!\nArquivo: " + file.getAbsolutePath(), Alert.AlertType.INFORMATION);
        }
    } catch (Exception e) {
        mostrarAlerta("Erro", "Erro ao exportar PDF: " + e.getMessage(), Alert.AlertType.ERROR);
        e.printStackTrace();
    }
}

    private String gerarConteudoHTML(String tipoRelatorio) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>");
        html.append("<html>");
        html.append("<head>");
        html.append("<meta charset='UTF-8'>");
        html.append("<title>Relatório - ").append(tipoRelatorio).append("</title>");
        html.append("<style>");
        html.append("body { font-family: Arial, sans-serif; margin: 40px; }");
        html.append("h1 { color: #2c3e50; text-align: center; }");
        html.append("table { width: 100%; border-collapse: collapse; margin: 20px 0; }");
        html.append("th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }");
        html.append("th { background-color: #f2f2f2; font-weight: bold; }");
        html.append(".info { margin: 10px 0; }");
        html.append(".footer { margin-top: 30px; text-align: center; font-style: italic; color: #7f8c8d; }");
        html.append("</style>");
        html.append("</head>");
        html.append("<body>");
        
        // Cabeçalho
        html.append("<h1>RELATÓRIO - ").append(tipoRelatorio.toUpperCase()).append("</h1>");
        
        // Informações
        html.append("<div class='info'><strong>Data de geração:</strong> ")
            .append(LocalDate.now().format(dateFormatter)).append("</div>");
        html.append("<div class='info'><strong>Período:</strong> ")
            .append(dateInicial.getValue().format(dateFormatter)).append(" a ")
            .append(dateFinal.getValue().format(dateFormatter)).append("</div>");
        html.append("<div class='info'><strong>Total de registros:</strong> ")
            .append(textTotalRegistros.getText()).append("</div>");
        html.append("<div class='info'><strong>Valor total:</strong> ")
            .append(textTotalGeral.getText()).append("</div>");
        html.append("<div class='info'><strong>Média:</strong> ")
            .append(textMedia.getText()).append("</div>");
        
        // Tabela
        html.append("<table>");
        html.append("<thead>");
        html.append("<tr>");
        html.append("<th>Descrição</th>");
        html.append("<th>Valor</th>");
        html.append("<th>Quantidade</th>");
        html.append("<th>Data</th>");
        html.append("</tr>");
        html.append("</thead>");
        html.append("<tbody>");
        
        for (Object item : listaResultados) {
            html.append("<tr>");
            if (item instanceof RelatorioVenda) {
                RelatorioVenda venda = (RelatorioVenda) item;
                html.append("<td>").append(venda.getDescricao()).append("</td>");
                html.append("<td>").append(format.format(venda.getValor())).append("</td>");
                html.append("<td>").append(venda.getQuantidade()).append("</td>");
                html.append("<td>").append(venda.getData().format(dateFormatter)).append("</td>");
            } else if (item instanceof RelatorioEstoque) {
                RelatorioEstoque estoque = (RelatorioEstoque) item;
                html.append("<td>").append(estoque.getProduto()).append("</td>");
                html.append("<td>").append(format.format(estoque.getValorTotal())).append("</td>");
                html.append("<td>").append(estoque.getQuantidade()).append("</td>");
                html.append("<td>N/A</td>");
            } else if (item instanceof RelatorioFiado) {
                RelatorioFiado fiado = (RelatorioFiado) item;
                html.append("<td>").append(fiado.getCliente()).append("</td>");
                html.append("<td>").append(format.format(fiado.getValorPendente())).append("</td>");
                html.append("<td>").append(fiado.getParcelasPagas()).append("/")
                    .append(fiado.getParcelasTotal()).append("</td>");
                html.append("<td>").append(fiado.getDataVenda().format(dateFormatter)).append("</td>");
            }
            html.append("</tr>");
        }
        
        html.append("</tbody>");
        html.append("</table>");
        
        // Rodapé
        html.append("<div class='footer'>");
        html.append("Relatório gerado pelo Sistema Amor de Mamãe - ")
            .append(LocalDate.now().format(dateFormatter));
        html.append("</div>");
        
        html.append("</body>");
        html.append("</html>");
        
        return html.toString();
    }

    @FXML
    private void limparFiltros() {
        choiceTipoRelatorio.setValue("Vendas - Detalhado");
        choicePeriodo.setValue("Este Mês");
        configurarPeriodoPadrao();
        listaResultados.clear();
        textTotalGeral.setText("R$ 0,00");
        textTotalRegistros.setText("0");
        textMedia.setText("R$ 0,00");
        textAreaResumo.clear();
    }

    private void mostrarAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}