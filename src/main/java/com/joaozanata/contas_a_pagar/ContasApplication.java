package com.joaozanata.contas_a_pagar;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ContasApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ContasApplication.class.getResource("lancamentos.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1010, 600);
        stage.setTitle("ERP - System");
        stage.setScene(scene);
        stage.show();
    }
}
