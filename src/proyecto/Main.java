package proyecto;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Main extends Application {

    float eps = 0.000001F;
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) {
        //launch(args);

        ArrayList<Integer> valoresDemanda = new ArrayList<>();
        valoresDemanda.add(25);
        valoresDemanda.add(26);
        valoresDemanda.add(27);
        valoresDemanda.add(28);
        valoresDemanda.add(29);
        valoresDemanda.add(30);
        valoresDemanda.add(31);
        valoresDemanda.add(35);
        valoresDemanda.add(33);
        valoresDemanda.add(34);

        ArrayList<Float> probabilidadesDemanda = new ArrayList<>();
        probabilidadesDemanda.add(0.02F);
        probabilidadesDemanda.add(0.04F);
        probabilidadesDemanda.add(0.06F);
        probabilidadesDemanda.add(0.12F);
        probabilidadesDemanda.add(0.20F);
        probabilidadesDemanda.add(0.24F);
        probabilidadesDemanda.add(0.15F);
        probabilidadesDemanda.add(0.1F);
        probabilidadesDemanda.add(0.05F);
        probabilidadesDemanda.add(0.02F);

        DistribucionProbabilidad tablaDemanda = new DistribucionProbabilidad(valoresDemanda, probabilidadesDemanda);

        ArrayList<Integer> valoresEntrega = new ArrayList<>();
        valoresEntrega.add(1);
        valoresEntrega.add(2);
        valoresEntrega.add(3);
        valoresEntrega.add(4);

        ArrayList<Float> probabilidadesEntrega = new ArrayList<>();
        probabilidadesEntrega.add(0.2F);
        probabilidadesEntrega.add(0.3F);
        probabilidadesEntrega.add(0.25F);
        probabilidadesEntrega.add(0.25F);

        DistribucionProbabilidad tablaEntregas = new DistribucionProbabilidad(valoresEntrega, probabilidadesEntrega);

        ArrayList<Integer> valoresEspera = new ArrayList<>();
        valoresEspera.add(0);
        valoresEspera.add(1);
        valoresEspera.add(2);
        valoresEspera.add(3);
        valoresEspera.add(4);

        ArrayList<Float> probabilidadesEspera = new ArrayList<>();
        probabilidadesEspera.add(0.4F);
        probabilidadesEspera.add(0.2F);
        probabilidadesEspera.add(0.15F);
        probabilidadesEspera.add(0.15F);
        probabilidadesEspera.add(0.1F);

        DistribucionProbabilidad tablaEsperas = new DistribucionProbabilidad(valoresEspera, probabilidadesEspera);

        Simulacion simulacion = new Simulacion();
        simulacion.setTablaDemanda(tablaDemanda);
        simulacion.setTablaTiempoEntrega(tablaEntregas);
        simulacion.setTablaTiempoEspera(tablaEsperas);
        simulacion.ejecutar(15);
    }
}
