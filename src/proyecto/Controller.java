package proyecto;

import javafx.collections.FXCollections;
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
import java.util.ResourceBundle;

public class Controller implements Initializable {

    // Declaramos los text utilizables
    @FXML public Text limitesQText;
    @FXML public Text limitesRText;
    @FXML public Text costoFaltanteText;
    @FXML public Text costoOrdenText;
    @FXML public Text costoInventarioText;
    @FXML public Text costoTotalText;

    // Declaramos los spinners utilizables
    @FXML private Spinner<Integer> valorQSpinner = new Spinner<>();
    @FXML private Spinner<Integer> valorRSpinner = new Spinner<>();

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

    ObservableList<Inventario> inventarios;

    // Solo para probar que la simulacion de los mismos resultados que el ejemplo. Al final esto y el metodo
    // crearTablasDePrueba lo eliminamos
    private ArrayList<Integer> valoresDemanda;
    private ArrayList<Float> probabilidadesDemanda;
    private DistribucionProbabilidad tablaDemanda;
    private ArrayList<Integer> valoresEntrega;
    private ArrayList<Float> probabilidadesEntrega;
    private DistribucionProbabilidad tablaEntregas;
    private ArrayList<Integer> valoresEspera;
    private ArrayList<Float> probabilidadesEspera;
    private DistribucionProbabilidad tablaEsperas;

    private void crearTablasDePrueba() {
        valoresDemanda = new ArrayList<>();
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

        probabilidadesDemanda = new ArrayList<>();
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

        // Se crea la tabla de demanda
        tablaDemanda = new DistribucionProbabilidad(valoresDemanda, probabilidadesDemanda);

        valoresEntrega = new ArrayList<>();
        valoresEntrega.add(1);
        valoresEntrega.add(2);
        valoresEntrega.add(3);
        valoresEntrega.add(4);

        probabilidadesEntrega = new ArrayList<>();
        probabilidadesEntrega.add(0.2F);
        probabilidadesEntrega.add(0.3F);
        probabilidadesEntrega.add(0.25F);
        probabilidadesEntrega.add(0.25F);

        tablaEntregas = new DistribucionProbabilidad(valoresEntrega, probabilidadesEntrega);

        valoresEspera = new ArrayList<>();
        valoresEspera.add(0);
        valoresEspera.add(1);
        valoresEspera.add(2);
        valoresEspera.add(3);
        valoresEspera.add(4);

        probabilidadesEspera = new ArrayList<>();
        probabilidadesEspera.add(0.4F);
        probabilidadesEspera.add(0.2F);
        probabilidadesEspera.add(0.15F);
        probabilidadesEspera.add(0.15F);
        probabilidadesEspera.add(0.1F);

        tablaEsperas = new DistribucionProbabilidad(valoresEspera, probabilidadesEspera);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // 1. Mariam obtiene los datos
        // 2. Con esos datos calculamos qmin, qmax, rmin y rmax
        // 3. Creamos la simulacion con esos datos
        // 4. Por cada valor entre qmin y qmax:
        //   4.1 Por cada valor entre rmin y rmax:
        //     4.1.2 ejecutamos la simulacion con el valor actual de q y r
        // 5. Mostramos los resultados

        /**
         * Mariam,
         * aqui es donde debes leer los datos del archivo. Para el momento en que se llegue a la parte de
         * calcular qmin, qmax, rmin y rmax, las tablas deben estar creadas.
         *
         * Por cada tabla que hay en el archivo vas a hacer primero:
         *      ArrayList<Integer> valoresTablaxxx = new ArrayList<>()
         *      ArrayList<Float> probabilidadesTablaxxx = new ArrayList<>()
         * Reemplaxa 'xxx' por demanda, tiempoEntrega y tiempoEspera
         *
         * Luego vas a leer linea por linea (sin while ni for, manualmente)  el archivo. En el formato que dijimos la
         * primera linea son los valores de la tabla, y la segunda las probabilidades:
         *
         *      File archivo = ...
         *      BufferedReader bufferedReader = ...
         *
         *      String lineaValores = bufferedReader.readLine();
         *      String lineaProbabilidades = bufferedReader.readLine();
         *
         * Luego extraes solamente lo que interesa:
         *
         *      String[] valores = lineaValores.split("\\|")
         *      String[] probabilidades = lineaProbabilidades.split("\\|")
         *
         * Luego los recorres y vas guardando el valor en los ArrayList de antes. Haces lo mismo para las demas
         * tablas del archivo. Luego fijate en crearTablasDePrueba() para ver como crear las tablas
         * en si.
         *
         * Recuerda tambien leer los costos.
         */

        crearTablasDePrueba();

        // Aqui debemos calcular los valores de qmin, qmax, rmin y rmax
        int qmin = 5;
        int qmax = 1000;
        int rmin = 5;
        int rmax = 1000;

        inicializarSpinners(qmin, qmax, rmin, rmax);

        // Se crea la simulacion con los datos que no variaran: las tablas y estos costos con constantes
        Simulacion simulacion = new Simulacion(tablaDemanda, tablaEntregas, tablaEsperas, 15,
                52, 100, 20,
                50, 50,
                this);

        // Listener para rehacer la simulacion con el nuevo valor de q
        valorQSpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            // Ejecutamos la simulacion con el nuevo valor de q, pero EL MISMO VALOR de r
            int r = valorRSpinner.getValue();
            simulacion.ejecutar(newValue, r);
        });

        // Listener para rehacer la simulacion con el nuevo valor de r
        valorRSpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            int q = valorQSpinner.getValue();
            simulacion.ejecutar(q, newValue);
        });

        simulacion.ejecutar(100, 75);

        /*for (int qactual = qmin; qactual <= 100; qactual++) {
            for (int ractual = rmin; ractual <= 100; ractual++) {
                simulacion.ejecutar(qactual, ractual);
            }
        }*/

        System.out.println("qmin = " + simulacion.getQmin());
        System.out.println("rmin = " + simulacion.getRmin());

    }

    private void inicializarSpinners(int qmin, int qmax, int rmin, int rmax) {
        //se establecen los limites de Valores de Q
        SpinnerValueFactory<Integer> valoresQ = new SpinnerValueFactory.IntegerSpinnerValueFactory(qmin, qmax, qmin);
        valorQSpinner.setValueFactory(valoresQ);

        //Se establecen los limites de Valores de R
        SpinnerValueFactory<Integer> valoresR = new SpinnerValueFactory.IntegerSpinnerValueFactory(rmin, rmax, rmin);
        valorRSpinner.setValueFactory(valoresR);

        //se escriben en los text de la vista los rangos de Q y R
        limitesQText.setText(qmin + " <= Q <= " + qmax);
        limitesRText.setText(rmin + " <= R <= " + rmax);
    }

    /**
     * Metodo para inicializar la tabla
     */
    void inicializarTablaSimulacion() {
        diaTC.setCellValueFactory(new PropertyValueFactory<Inventario, Integer>("dia"));
        invInicioTC.setCellValueFactory(new PropertyValueFactory<Inventario, Integer>("invInicio"));
        aleatorioDemandaTC.setCellValueFactory(new PropertyValueFactory<Inventario, Integer>("aleatorioDemanda"));
        DemandaTC.setCellValueFactory(new PropertyValueFactory<Inventario, Integer>("Demanda"));
        invFinalTC.setCellValueFactory(new PropertyValueFactory<Inventario, Integer>("invFinal"));
        invPromTC.setCellValueFactory(new PropertyValueFactory<Inventario, Integer>("invProm"));
        FaltanteTC.setCellValueFactory(new PropertyValueFactory<Inventario, Integer>("faltante"));
        noOrdenTC.setCellValueFactory(new PropertyValueFactory<Inventario, Integer>("noOrden"));
        aleatorioEntregaTC.setCellValueFactory(new PropertyValueFactory<Inventario, Integer>("aleatorioEntrega"));
        tiempoEntregaTC.setCellValueFactory(new PropertyValueFactory<Inventario, Integer>("tiempoEntrega"));
        aleatorioEsperaTC.setCellValueFactory(new PropertyValueFactory<Inventario, Integer>("aleatorioEspera"));
        tiempoEsperaTC.setCellValueFactory(new PropertyValueFactory<Inventario, Integer>("tiempoEspera"));

        inventarios = FXCollections.observableArrayList();
        inventarioTV.setItems(inventarios);
    }

    /**
     * Inserta una nueva fila en la tabla de eventos
     *
     * @param inventario la fila a insertar
     */
    void insertarFila(Inventario inventario) {
        inventarios.add(inventario);
    }
}
