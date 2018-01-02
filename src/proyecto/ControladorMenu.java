package proyecto;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

public class ControladorMenu {
    @FXML public void ParametrosManual() {
        try {
            Parent mostrarParametrosManual = FXMLLoader.load(getClass().getResource("ParametrosManual.fxml"));
            Scene scene = new Scene(mostrarParametrosManual, Main.ANCHO_VENTANA, Main.ALTURA_VENTANA);
            Main.stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML public void ParametrosArchivo() {
        try {
            Parent mostrarParametrosArchivo = FXMLLoader.load(getClass().getResource("ParametrosArchivo.fxml"));
            Scene scene = new Scene(mostrarParametrosArchivo, Main.ANCHO_VENTANA, Main.ALTURA_VENTANA);
            Main.stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML public void Instrucciones() {
        try {
            Parent mostrarInstrucciones = FXMLLoader.load(getClass().getResource("Instrucciones.fxml"));
            Scene scene = new Scene(mostrarInstrucciones, Main.ANCHO_VENTANA, Main.ALTURA_VENTANA);
            Main.stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}