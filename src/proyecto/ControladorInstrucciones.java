package proyecto;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

public class ControladorInstrucciones {
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
