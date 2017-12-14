package proyecto;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    //Declaramos los text utilizables
    @FXML public Text limitesQText;
    @FXML public Text limitesRText;
    @FXML public Text costoFaltanteText;
    @FXML public Text costoOrdenText;
    @FXML public Text costoInventarioText;
    @FXML public Text costoTotalText;

    //Declaramos los spinners utilizables
    @FXML private Spinner<Integer> valorQSpinner = new Spinner<Integer>();
    @FXML private Spinner<Integer> valorRSpinner = new Spinner<Integer>();

    // Declaramos la tabla y las columnas
    @FXML private TableView<Inventario> inventarioTV;
    @FXML private TableColumn diaTC;
    @FXML private TableColumn invInicioTC;
    @FXML private TableColumn aleatorioDemandaTC;
    @FXML private TableColumn DemandaTC;
    @FXML private TableColumn invFinalTC;
    @FXML private TableColumn invPromTC;
    @FXML private TableColumn FaltanteTC;
    @FXML private TableColumn noOrdenTC;
    @FXML private TableColumn aleatorioEntregaTC;
    @FXML private TableColumn tiempoEntregaTC;
    @FXML private TableColumn aleatorioEsperaTC;
    @FXML private TableColumn tiempoEsperaTC;

    public ObservableList<Inventario> inventarios;
    private int posicionInventarioEnTabla;

    float eps = 0.000001F;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        int MINQ = 5;
        int MAXQ = 1000;

        int MINR = 5;
        int MAXR = 1000;

        //se establecen los limites de Valores de Q
        SpinnerValueFactory<Integer> valoresQ = new SpinnerValueFactory.IntegerSpinnerValueFactory(MINQ, MAXQ, MINQ);
        valorQSpinner.setValueFactory(valoresQ);

        //Se establecen los limites de Valores de R
        SpinnerValueFactory<Integer> valoresR = new SpinnerValueFactory.IntegerSpinnerValueFactory(MINR, MAXR, MINR);
        valorRSpinner.setValueFactory(valoresR);

        //se escriben en los text de la vista los rangos de Q y R
        limitesQText.setText(MINQ+" <= Q <= "+MAXQ);
        limitesRText.setText(MINR+" <= R <= "+MAXR);

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
        probabilidadesDemanda.add(0.10F);
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
        simulacion.ejecutar(15, this);

    }

    /**
     * Método para inicializar la tabla
     */
    public void inicializarTablaPersonas() {
        diaTC.setCellValueFactory(new PropertyValueFactory<Inventario, Integer>("dia"));
        invInicioTC.setCellValueFactory(new PropertyValueFactory<Inventario, Integer>("invInicio"));
        aleatorioDemandaTC.setCellValueFactory(new PropertyValueFactory<Inventario, Float>("aleatorioDemanda"));
        DemandaTC.setCellValueFactory(new PropertyValueFactory<Inventario, Integer>("Demanda"));
        invFinalTC.setCellValueFactory(new PropertyValueFactory<Inventario, Integer>("invFinal"));
        invPromTC.setCellValueFactory(new PropertyValueFactory<Inventario, Integer>("invProm"));
        FaltanteTC.setCellValueFactory(new PropertyValueFactory<Inventario, Integer>("Faltante"));
        noOrdenTC.setCellValueFactory(new PropertyValueFactory<Inventario, Integer>("noOrden"));
        aleatorioEntregaTC.setCellValueFactory(new PropertyValueFactory<Inventario, Float>("aleatorioEntrega"));
        tiempoEntregaTC.setCellValueFactory(new PropertyValueFactory<Inventario, Integer>("tiempoEntrega"));
        aleatorioEsperaTC.setCellValueFactory(new PropertyValueFactory<Inventario, Float>("aleatorioEspera"));
        tiempoEsperaTC.setCellValueFactory(new PropertyValueFactory<Inventario, Integer>("tiempoEspera"));

        inventarios = FXCollections.observableArrayList();
        inventarioTV.setItems(inventarios);
    }

}
