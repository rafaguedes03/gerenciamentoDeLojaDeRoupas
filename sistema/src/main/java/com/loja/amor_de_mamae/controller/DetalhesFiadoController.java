package com.loja.amor_de_mamae.controller;

import com.loja.amor_de_mamae.dao.FiadoDAO;
import com.loja.amor_de_mamae.model.Fiado;
import com.loja.amor_de_mamae.model.ParcelaFiado;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.scene.text.Text;

import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class DetalhesFiadoController {

    @FXML private Text textTitulo;
    @FXML private Label labelCliente;
    @FXML private Label labelDataVenda;
    @FXML private Label labelTotal;
    @FXML private Label labelStatus;
    
    @FXML private TableView<ParcelaFiado> tableViewParcelas;
    @FXML private TableColumn<ParcelaFiado, Integer> colunaNumero;
    @FXML private TableColumn<ParcelaFiado, Double> colunaValor;
    @FXML private TableColumn<ParcelaFiado, String> colunaVencimento;
    @FXML private TableColumn<ParcelaFiado, String> colunaStatusParcela;
    @FXML private TableColumn<ParcelaFiado, Void> colunaAcaoParcela;

    private Fiado fiado;
    private FiadoDAO fiadoDAO = new FiadoDAO();
    private ObservableList<ParcelaFiado> listaParcelas = FXCollections.observableArrayList();

    public void setFiado(Fiado fiado) {
        this.fiado = fiado;
    }

    @FXML
    public void initialize() {
        configurarTabelaParcelas();
    }

    public void carregarDados() {
        if (fiado != null) {
            textTitulo.setText("Detalhes do Fiado - " + fiado.getNomeCliente());
            labelCliente.setText(fiado.getNomeCliente());
            labelDataVenda.setText(fiado.getDataVenda().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            
            NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
            labelTotal.setText(format.format(fiado.getTotalVenda()));
            
            labelStatus.setText(fiado.getStatus().toUpperCase());
            
            carregarParcelas();
        }
    }

    private void configurarTabelaParcelas() {
        colunaNumero.setCellValueFactory(new PropertyValueFactory<>("numero_parcela"));
        colunaValor.setCellValueFactory(new PropertyValueFactory<>("valor_parcela"));
        
        colunaValor.setCellFactory(column -> new TableCell<ParcelaFiado, Double>() {
            @Override
            protected void updateItem(Double valor, boolean empty) {
                super.updateItem(valor, empty);
                if (empty || valor == null) {
                    setText(null);
                } else {
                    setText(NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(valor));
                }
            }
        });
        
        colunaVencimento.setCellValueFactory(cellData -> {
            ParcelaFiado parcela = cellData.getValue();
            return new javafx.beans.property.SimpleStringProperty(
                parcela.getData_vencimento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            );
        });
        
        colunaStatusParcela.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        configurarColunaAcaoParcelas();
        tableViewParcelas.setItems(listaParcelas);
    }

    private void configurarColunaAcaoParcelas() {
        Callback<TableColumn<ParcelaFiado, Void>, TableCell<ParcelaFiado, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<ParcelaFiado, Void> call(final TableColumn<ParcelaFiado, Void> param) {
                return new TableCell<>() {
                    private final Button btnPagar = new Button("Pagar");

                    {
                        btnPagar.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-size: 10px;");
                        btnPagar.setOnAction(event -> {
                            ParcelaFiado parcela = getTableView().getItems().get(getIndex());
                            if (!"paga".equals(parcela.getStatus())) {
                                marcarParcelaComoPaga(parcela);
                            }
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            ParcelaFiado parcela = getTableView().getItems().get(getIndex());
                            if ("paga".equals(parcela.getStatus())) {
                                btnPagar.setDisable(true);
                                btnPagar.setText("Paga");
                                btnPagar.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white; -fx-font-size: 10px;");
                            } else {
                                btnPagar.setDisable(false);
                                btnPagar.setText("Pagar");
                                btnPagar.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-size: 10px;");
                            }
                            setGraphic(btnPagar);
                        }
                    }
                };
            }
        };

        colunaAcaoParcela.setCellFactory(cellFactory);
    }

    private void carregarParcelas() {
        try {
            if (fiado != null) {
                List<ParcelaFiado> parcelas = fiadoDAO.listarParcelasPorFiado(fiado.getId_fiado());
                listaParcelas.setAll(parcelas);
            }
        } catch (Exception e) {
            mostrarAlerta("Erro", "Erro ao carregar parcelas: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void marcarParcelaComoPaga(ParcelaFiado parcela) {
        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar Pagamento");
        confirmacao.setHeaderText("Deseja marcar esta parcela como paga?");
        confirmacao.setContentText("Parcela: " + parcela.getNumero_parcela() + 
                                 "\nValor: " + NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(parcela.getValor_parcela()) +
                                 "\nVencimento: " + parcela.getData_vencimento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        Optional<ButtonType> result = confirmacao.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                fiadoDAO.marcarParcelaComoPaga(parcela.getId_parcela());
                fiadoDAO.atualizarStatusFiado(fiado.getId_fiado());
                
                mostrarAlerta("Sucesso", "Parcela marcada como paga com sucesso!", Alert.AlertType.INFORMATION);
                carregarParcelas();
                atualizarStatusFiado();
                
            } catch (Exception e) {
                mostrarAlerta("Erro", "Erro ao marcar parcela como paga: " + e.getMessage(), Alert.AlertType.ERROR);
                e.printStackTrace();
            }
        }
    }

    private void atualizarStatusFiado() {
        try {
            // Recarregar o status do fiado
            List<Fiado> fiados = fiadoDAO.listarTodosFiados();
            for (Fiado f : fiados) {
                if (f.getId_fiado() == fiado.getId_fiado()) {
                    fiado.setStatus(f.getStatus());
                    labelStatus.setText(fiado.getStatus().toUpperCase());
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void fechar() {
        Stage stage = (Stage) textTitulo.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}