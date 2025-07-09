
package app;

import dao.MesDAO;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Gastos;
import model.Mes;

import java.time.LocalDate;
import java.util.List;

public class MainApp extends Application {

    private TextField tfCasa = new TextField();
    private TextField tfSeguros = new TextField();
    private TextField tfCarro = new TextField();
    private TextField tfEducacao = new TextField();
    private TextField tfInternet = new TextField();
    private TextField tfSaude = new TextField();
    private TextField tfCartao = new TextField();
    private TextField tfContaAgua = new TextField();
    private TextField tfContaLuz = new TextField();
    private TextField tfOutros = new TextField();
    private TextField tfSalario = new TextField();
    private DatePicker dpData = new DatePicker();


    @Override
    public void start(Stage stage) {
        GridPane grid = new GridPane();

        String[] labels = {"Casa", "Seguros", "Carro", "Educação", "Internet", "Saúde", "Cartão", "Conta de Água", "Conta de Luz", "Outros"};
        TextField[] fields = {tfCasa, tfSeguros, tfCarro, tfEducacao, tfInternet, tfSaude, tfCartao, tfContaAgua, tfContaLuz, tfOutros};

        for (int i = 0; i < labels.length; i++) {
            grid.add(new Label(labels[i] + ":"), 0, i);
            grid.add(fields[i], 1, i);
        }

        grid.add(new Label("Data:"), 0, labels.length);
        grid.add(dpData, 1, labels.length);

        grid.add(new Label("Salário:"), 0, labels.length + 1);
        grid.add(tfSalario, 1, labels.length + 1);


        Button btnSalvar = new Button("Salvar");
        btnSalvar.setOnAction(e -> salvar());

        Button btnHistorico = new Button("Ver Histórico");
        btnHistorico.setOnAction(e -> mostrarHistorico());

        VBox vbox = new VBox(10, grid, btnSalvar, btnHistorico);
        vbox.setPadding(new javafx.geometry.Insets(20));
        stage.setScene(new Scene(vbox, 320, 550));
        stage.setTitle("GastaMenos");
        stage.show();
    }

    private void salvar() {
        try {
            Gastos g = new Gastos();
            g.casa = Double.parseDouble(tfCasa.getText());
            g.seguros = Double.parseDouble(tfSeguros.getText());
            g.carro = Double.parseDouble(tfCarro.getText());
            g.educacao = Double.parseDouble(tfEducacao.getText());
            g.internet = Double.parseDouble(tfInternet.getText());
            g.saude = Double.parseDouble(tfSaude.getText());
            g.cartao = Double.parseDouble(tfCartao.getText());
            g.contaAgua = Double.parseDouble(tfContaAgua.getText());
            g.contaLuz = Double.parseDouble(tfContaLuz.getText());
            g.outros = Double.parseDouble(tfOutros.getText());


            Mes mes = new Mes();
            mes.data = dpData.getValue() != null ? dpData.getValue() : LocalDate.now();
            mes.salario = Double.parseDouble(tfSalario.getText());
            mes.gastos = g;

            new MesDAO().salvar(mes);

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Gastos salvos com sucesso!");
            alert.show();
        } catch (Exception ex) {
            ex.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao salvar gastos.");
            alert.show();
        }
    }

    private void mostrarHistorico() {
        Stage stage = new Stage();
        VBox vbox = new VBox(10);
        vbox.setPadding(new javafx.geometry.Insets(10));

        TableView<Mes> tabela = new TableView<>();

        TableColumn<Mes, String> dataCol = new TableColumn<>("Data");
        dataCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
            cellData.getValue().data.toString()
        ));

        TableColumn<Mes, String> totalCol = new TableColumn<>("Total");
        totalCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
            String.format("R$ %.2f", cellData.getValue().gastos.total())
        ));

        TableColumn<Mes, String> economiaCol = new TableColumn<>("Economia");
        economiaCol.setCellValueFactory(cellData -> {
            Mes m = cellData.getValue();
            double economia = m.salario - m.gastos.total();
            return new javafx.beans.property.SimpleStringProperty(String.format("R$ %.2f", economia));
        });

        tabela.getColumns().addAll(dataCol, totalCol, economiaCol);

        try {
            List<Mes> lista = new MesDAO().listarTodos();
            tabela.getItems().addAll(lista);
        } catch (Exception e) {
            e.printStackTrace();
        }

        vbox.getChildren().addAll(new Label("Histórico de Gastos"), tabela);
        stage.setScene(new Scene(vbox, 400, 300));
        stage.setTitle("Histórico");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
