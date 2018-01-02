package proyecto;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.io.IOException;

public class ControladorParametrosManual {

    private Alert alerta;

    @FXML private TextField demandaDiaria_tf;
    @FXML private TextField probDemanda_tf;
    @FXML private TextField tiempoEntrega_tf;
    @FXML private TextField probEntrega_tf;
    @FXML private TextField tiempoEspera_tf;
    @FXML private TextField probEspera_tf;

    @FXML private TextField dias_tf;
    @FXML private TextField inventarioIni_tf;
    @FXML private TextField costoInventario_tf;
    @FXML private TextField costoOrdenar_tf;
    @FXML private TextField faltante1_tf;
    @FXML private TextField faltante2_tf;

    public void alerta(){
        alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setHeaderText("ERROR");
    }

    @FXML public void aceptar() {

        if (demandaDiaria_tf.getText().isEmpty()) {
            alerta();
            alerta.setContentText("El campo \"Demanda diaria\" no puede estar vacio. Intente nuevamente.");
            alerta.show();
            return;
        }
        if (probDemanda_tf.getText().isEmpty()) {
            alerta();
            alerta.setContentText("El campo \"Probabilidad (correspondiente a la demanda diaria)\" no puede estar vacio. Intente nuevamente.");
            alerta.show();
            return;
        }
        if (tiempoEntrega_tf.getText().isEmpty()) {
            alerta();
            alerta.setContentText("El campo \"Tiempo entrega\" no puede estar vacio. Intente nuevamente.");
            alerta.show();
            return;
        }
        if (probEntrega_tf.getText().isEmpty()) {
            alerta();
            alerta.setContentText("El campo \"Probabilidad (correspondiente al tiempo de entrega)\" no puede estar vacio. Intente nuevamente.");
            alerta.show();
            return;
        }
        if (tiempoEspera_tf.getText().isEmpty()) {
            alerta();
            alerta.setContentText("El campo \"Tiempo espera\" no puede estar vacio. Intente nuevamente.");
            alerta.show();
            return;
        }
        if (probEspera_tf.getText().isEmpty()) {
            alerta();
            alerta.setContentText("El campo \"Probabilidad (correspondiente al tiempo de espera)\" no puede estar vacio" +
                    ". Intente nuevamente.");
            alerta.show();
            return;
        }

        if (dias_tf.getText().isEmpty()) {
            alerta();
            alerta.setContentText("El campo \"Días a simular\" no puede estar vacio. Intente nuevamente.");
            alerta.show();
            return;
        }
        if (inventarioIni_tf.getText().isEmpty()) {
            alerta();
            alerta.setContentText("El campo \"Inventario inicial\" no puede estar vacio. Intente nuevamente.");
            alerta.show();
            return;
        }
        if (costoInventario_tf.getText().isEmpty()) {
            alerta();
            alerta.setContentText("El campo \"Costo de ordenar\" no puede estar vacio. Intente nuevamente.");
            alerta.show();
            return;
        }
        if (costoOrdenar_tf.getText().isEmpty()) {
            alerta();
            alerta.setContentText("El campo \"Costo de inventario\" no puede estar vacio. Intente nuevamente.");
            alerta.show();
            return;
        }
        if (faltante1_tf.getText().isEmpty()) {
            alerta();
            alerta.setContentText("El campo \"Faltante (con espera)\" no puede estar vacio. Intente nuevamente.");
            alerta.show();
            return;
        }
        if (faltante2_tf.getText().isEmpty()) {
            alerta();
            alerta.setContentText("El campo \"Faltante (sin espera)\" no puede estar vacio. Intente nuevamente.");
            alerta.show();
            return;
        }
    }

    /* Regresar al menu principal */
    @FXML private void atras() {
        try {
            Parent mostrarMenu = FXMLLoader.load(getClass().getResource("Menu.fxml"));
            Scene scene = new Scene(mostrarMenu, Main.ANCHO_VENTANA, Main.ALTURA_VENTANA);
            Main.stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}