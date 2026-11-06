module com.joaozanata.contas_a_pagar {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.joaozanata.contas_a_pagar to javafx.fxml;
    exports com.joaozanata.contas_a_pagar;
}