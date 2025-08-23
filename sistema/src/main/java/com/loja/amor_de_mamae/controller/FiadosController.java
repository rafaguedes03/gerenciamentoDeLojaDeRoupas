package com.loja.amor_de_mamae.controller;

import com.loja.amor_de_mamae.dao.FiadoDAO;
import com.loja.amor_de_mamae.model.Fiado;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.text.Text;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class FiadosController {

    @FXML private TextField searchCliente;
    @FXML private TableView<Fiado> tableViewFiados;
    @FXML private TableColumn<Fiado, String> colunaCliente;
    @FXML private TableColumn<Fiado, String> colunaData;
    @FXML private TableColumn<Fiado, Double> colunaTotal;
    @FXML private TableColumn<Fiado, String> colunaParcelas;
    @FXML private TableColumn<Fiado, String> colunaStatus;
    @FXML private TableColumn<Fiado, Void> colunaAcao;
    
    @FXML private Text textTotalFiados;
    @FXML private Text textFiadosAtivos;
    @FXML private Text textParcelasPendentes;

    private FiadoDAO fiadoDAO = new FiadoDAO();
    private ObservableList<Fiado> listaFiados = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        configurarTabela();
        carregarFiados();
        atualizarEstatisticas();
    }

    private void configurarTabela() {
        colunaCliente.setCellValueFactory(new PropertyValueFactory<>("nomeCliente"));
        colunaData.setCellValueFactory(new PropertyValueFactory<>("dataVenda"));
        colunaTotal.setCellValueFactory(new PropertyValueFactory<>("totalVenda"));
        colunaParcelas.setCellValueFactory(cellData -> {
            Fiado fiado = cellData.getValue();
            return new javafx.beans.property.SimpleStringProperty(
                fiado.getParcelasPagas() + "/" + fiado.getNumParcelas()
            );
        });
        colunaStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        configurarColunaAcao();
        tableViewFiados.setItems(listaFiados);
    }

    private void configurarColunaAcao() {
        Callback<TableColumn<Fiado, Void>, TableCell<Fiado, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Fiado, Void> call(final TableColumn<Fiado, Void> param) {
                return new TableCell<>() {
                    private final Button btnVisualizar = new Button("Visualizar");

                    {
                        btnVisualizar.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 10px;");
                        btnVisualizar.setOnAction(event -> {
                            Fiado fiado = getTableView().getItems().get(getIndex());
                            visualizarDetalhesFiado(fiado);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btnVisualizar);
                        }
                    }
                };
            }
        };

        colunaAcao.setCellFactory(cellFactory);
    }

    @FXML
    private void buscarFiados() {
        try {
            String termo = searchCliente.getText().trim();
            List<Fiado> fiados;
            
            if (termo.isEmpty()) {
                fiados = fiadoDAO.listarTodosFiados();
            } else {
                fiados = fiadoDAO.buscarFiadosPorCliente(termo);
            }
            
            listaFiados.setAll(fiados);
            atualizarEstatisticas();
            
        } catch (Exception e) {
            mostrarAlerta("Erro", "Erro ao buscar fiados: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void limparBusca() {
        searchCliente.clear();
        carregarFiados();
    }

    private void carregarFiados() {
        try {
            List<Fiado> fiados = fiadoDAO.listarTodosFiados();
            listaFiados.setAll(fiados);
            atualizarEstatisticas();
            
        } catch (Exception e) {
            mostrarAlerta("Erro", "Erro ao carregar fiados: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void atualizarEstatisticas() {
        double total = 0;
        int ativos = 0;
        int pendentes = 0;
        
        for (Fiado fiado : listaFiados) {
            if ("ativo".equals(fiado.getStatus())) {
                total += fiado.getTotalVenda();
                ativos++;
                pendentes += (fiado.getNumParcelas() - fiado.getParcelasPagas());
            }
        }
        
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        textTotalFiados.setText(format.format(total));
        textFiadosAtivos.setText(String.valueOf(ativos));
        textParcelasPendentes.setText(String.valueOf(pendentes));
    }

    private void visualizarDetalhesFiado(Fiado fiado) {
        try {
            // Abrir tela de detalhes do fiado
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/loja/amor_de_mamae/view/DetalhesFiado.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Detalhes do Fiado - " + fiado.getNomeCliente());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);

            DetalhesFiadoController controller = loader.getController();
            controller.setFiado(fiado);
            controller.carregarDados();

            stage.showAndWait();
            
            // Recarregar após fechar a janela de detalhes
            carregarFiados();
            
        } catch (Exception e) {
            mostrarAlerta("Erro", "Erro ao abrir detalhes do fiado: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void mostrarAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}