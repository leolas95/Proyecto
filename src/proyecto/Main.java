package proyecto;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;


public class Main extends Application {

    /*@Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("Interfaz.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }*/

    final static int ANCHO_VENTANA = 600;
    final static int ALTURA_VENTANA = 400;

    static Scene escena;
    static Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        /* Loading fonts */
        Font.loadFont(getClass().getResourceAsStream("Fonts/Roboto.ttf"), 16);
        Font.loadFont(getClass().getResourceAsStream("Fonts/Roboto-Black.ttf"), 16);

        /* Init */
        Parent root = FXMLLoader.load(getClass().getResource("Menu.fxml"));
        escena = new Scene(root, ANCHO_VENTANA, ALTURA_VENTANA);
        escena.getStylesheets().add(Main.class.getResource("Estilos.css").toExternalForm());
        stage = primaryStage;
        stage.setTitle("Simulador de Inventario");
        stage.setScene(escena);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}