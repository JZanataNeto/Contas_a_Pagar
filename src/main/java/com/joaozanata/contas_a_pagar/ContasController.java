package com.joaozanata.contas_a_pagar;

import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

public class ContasController implements Initializable {

    @FXML private TableView<Conta> tblContas;
    @FXML private TableColumn<Conta, Date> clData;
    @FXML private TableColumn<Conta, String> clDescricao;
    @FXML private TableColumn<Conta, String> clForma;
    @FXML private TableColumn<Conta, String> clParcela;
    @FXML private TableColumn<Conta, String> clStatus;
    @FXML private TableColumn<Conta, String> clValor;
    @FXML private DatePicker dpVencimento;
    @FXML private TextField txtDescricao;
    @FXML private TextField txtForma;
    @FXML private TextField txtParcela;
    @FXML private TextField txtStatus;
    @FXML private TextField txtValor;
    @FXML private Button btnSalvar;
    @FXML private Button btnAtualizar;
    @FXML private Button btnApagar;
    @FXML private Button btnLimpar;
    @FXML private TextField txtAPagar;
    @FXML private TextField txtPaga;
    @FXML private TextField txtTotalGeral;
    @FXML private TextField txtVencidas;

    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private ContasService service;

    public void initialize(URL location, ResourceBundle resources) {
        service = ContasService.getInstance();
        configuraColunas();
        configuraBindings();
        tblContas.getItems().addListener((javafx.collections.ListChangeListener<Conta>) c -> atualizarTotais());
        atualizarDadosTabela();
        atualizarTotais();
        txtPaga.setEditable(false);
        txtVencidas.setEditable(false);
        txtAPagar.setEditable(false);
        txtTotalGeral.setEditable(false);
    }

    public void atualizarTotais() {
        double paga = 0.0;
        double pendente = 0.0;
        double vencidas = 0.0;
        double totalGeral = 0.0;

        for (Conta conta : tblContas.getItems()) {
            double valor = Double.parseDouble(conta.getValor());
            String status = conta.getStatus() != null ? conta.getStatus().toLowerCase() : "";
            if (status.contains("paga") || status.contains("pago")) {
                paga += valor;
            }
            else if (status.contains("vencida") || status.contains("vencido")) {
                    vencidas += valor;
            }
            else if (status.contains("pendente")){
                    pendente += valor;
            }
            totalGeral = paga + pendente + vencidas;
        }

        Locale ptBr = new Locale("pt", "BR");
        NumberFormat formatadorMoeda = NumberFormat.getCurrencyInstance(ptBr);
        txtPaga.setText(formatadorMoeda.format(paga));
        txtAPagar.setText(formatadorMoeda.format(pendente));
        txtVencidas.setText(formatadorMoeda.format(vencidas));
        txtTotalGeral.setText(formatadorMoeda.format(totalGeral));
    }

    public void salvar() {
        Conta c = new Conta();
        pegaValores(c);
        service.salvar(c);
        atualizarDadosTabela();
        atualizarTotais();
    }
    public void atualizar() {
        Conta c = tblContas.getSelectionModel().getSelectedItem();
        pegaValores(c);
        service.atualizar(c);
        atualizarDadosTabela();
        atualizarTotais();
    }
    public void apagar() {
        Conta c = tblContas.getSelectionModel().getSelectedItem();
        service.apagar(c.getId());
        atualizarDadosTabela();
        atualizarTotais();
    }
    public void limpar() {
        tblContas.getSelectionModel().select(null);
        dpVencimento.setValue(null);
        txtDescricao.setText("");
        txtForma.setText("");
        txtParcela.setText("");
        txtStatus.setText("");
        txtValor.setText("");
    }
    private void pegaValores(Conta c) {
        c.setDataVencimento(dataSelecionada());
        c.setDescricao(txtDescricao.getText());
        c.setForma(txtForma.getText());
        c.setParcela(txtParcela.getText());
        c.setStatus(txtStatus.getText());
        c.setValor(txtValor.getText());
    }
    private Date dataSelecionada() {
        LocalDateTime time = dpVencimento.getValue().atStartOfDay();
        return Date.from(time.atZone(ZoneId.systemDefault()).toInstant());
    }

    private void atualizarDadosTabela() {
        tblContas.getItems().setAll(service.buscarContas());
        limpar();
    }
    private void configuraColunas() {
        clData.setCellValueFactory(new PropertyValueFactory<>("dataVencimento"));
        clData.setCellFactory(coluna -> new TableCell<>() {
            @Override
            protected void updateItem(Date data, boolean empty) {
                super.updateItem(data, empty);
                setText(empty || data == null ? null : data.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().format(FORMATO_DATA));
            }
        });
        clDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        clForma.setCellValueFactory(new PropertyValueFactory<>("forma"));
        clParcela.setCellValueFactory(new PropertyValueFactory<>("parcela"));
        clStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        clValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
    }
    private void configuraBindings() {
        BooleanBinding camposPreenchidos = dpVencimento.valueProperty().isNull()
                .or(txtDescricao.textProperty().isEmpty())
                .or(txtForma.textProperty().isEmpty())
                .or(txtParcela.textProperty().isEmpty())
                .or(txtStatus.textProperty().isEmpty())
                .or(txtValor.textProperty().isEmpty());
        BooleanBinding algoSelecionado = tblContas.getSelectionModel().selectedItemProperty().isNull();
        btnAtualizar.disableProperty().bind(algoSelecionado);
        btnLimpar.disableProperty().bind(algoSelecionado);
        btnApagar.disableProperty().bind(algoSelecionado);
        btnSalvar.disableProperty().bind(algoSelecionado.not().or(camposPreenchidos));
        tblContas.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
           if (newValue != null) {
               LocalDate data;
               data = newValue.getDataVencimento().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
               dpVencimento.setValue(data);
               txtDescricao.setText(newValue.getDescricao());
               txtForma.setText(newValue.getForma());
               txtParcela.setText(newValue.getParcela());
               txtStatus.setText(newValue.getStatus());
               txtValor.setText(newValue.getValor());
           }
        });
    }
}


