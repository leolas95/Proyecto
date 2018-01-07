package proyecto;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;


public class Main extends Application {

    final static int ANCHO_VENTANA = 600;
    final static int ALTURA_VENTANA = 400;

    private static Scene escena;
    static Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        //* Loading fonts *//*
        Font.loadFont(getClass().getResourceAsStream("Fonts/Roboto.ttf"), 16);
        Font.loadFont(getClass().getResourceAsStream("Fonts/Roboto-Black.ttf"), 16);

        //* Init *//*
        Parent root = FXMLLoader.load(getClass().getResource("Menu.fxml"));
        escena = new Scene(root, ANCHO_VENTANA, ALTURA_VENTANA);
        escena.getStylesheets().add(Main.class.getResource("Estilos.css").toExternalForm());
        stage = primaryStage;
        stage.setTitle("Simulador de Inventario");
        stage.setScene(escena);
        stage.setResizable(false);
        stage.show();

        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
    }

    public static void main(String[] args) {
        launch(args);
    }
}