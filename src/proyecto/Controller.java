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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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

    private ObservableList<Inventario> inventarios;

    private DistribucionProbabilidad tablaDemanda;
    private DistribucionProbabilidad tablaEntregas;
    private DistribucionProbabilidad tablaEsperas;

    private int costoDeOrden, costoDeInventario, escasezConEspera, escasezSinEspera, inventarioInicial, diasSimulacion;

    // Modificar segun cada quien
    private final static String RUTA_ARCHIVO_DEFECTO = "C:\\Users\\Leonardo\\Documents\\Parametros.txt";
    private Simulacion simulacion;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

//        crearTablasDePrueba();
        leerParametrosDeArchivo(RUTA_ARCHIVO_DEFECTO);

        int qmin = calcularQAsterisco(costoDeOrden, tablaDemanda.obtenerValorMinimo(), costoDeInventario, escasezSinEspera);
        int qmax = calcularQAsterisco(costoDeOrden, tablaDemanda.obtenerValorMaximo(), costoDeInventario, escasezConEspera);

        int rmin = calcularPuntoDeReorden(tablaDemanda.obtenerValorMinimo(), diasSimulacion, qmin, tablaEntregas.obtenerValorMinimo());
        int rmax = calcularPuntoDeReorden(tablaDemanda.obtenerValorMaximo(), diasSimulacion, qmax, tablaEntregas.obtenerValorMaximo());

        inicializarSpinners(qmin, qmax, rmin, rmax);

        // Se crea la simulacion con los datos que no variaran: las tablas y estos costos con constantes
        simulacion = new Simulacion(tablaDemanda, tablaEntregas, tablaEsperas, diasSimulacion,
                costoDeInventario, costoDeOrden, escasezConEspera,
                escasezSinEspera, inventarioInicial,
                this);

        inicializarListeners();

        for (int qactual = qmin; qactual <= qmax; qactual++) {
            for (int ractual = rmin; ractual <= rmax; ractual++) {
                simulacion.ejecutar(qactual, ractual);
            }
        }

        System.out.println("qmin = " + simulacion.getQmin());
        System.out.println("rmax = " + simulacion.getRmin());
    }

    private void inicializarListeners() {
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
    }

    /**
     * Calcula el punto de reorden
     *
     * @param D              la demanda
     * @param diasSimulacion los dias que va a durar la simulacion
     * @param q              la cantidad de articulos solicitados por pedido
     * @param tiempoEntrega  el tiempo de entrega del pedido
     * @return el punto de reorden calculado
     */
    private int calcularPuntoDeReorden(int D, int diasSimulacion, int q, int tiempoEntrega) {
        float L = tiempoEntrega / (float) diasSimulacion; // Transforma el tiempo de entrega de dias a anos
        float t0 = (float) q / (float) D;

        if (L < t0) {
            // PR = LD
            return (int) (L * D);
        } else {
            //      _ _
            //     | L |
            // n = |---|
            //     | D |
            // Le = L - nt0;
            // PR = Le * D;

            int n = (int) (L / D);
            float Le = L - n * t0;
            return (int) (Le * D);
        }
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
     * Lee los parametros de la simulacion del archivo especificado
     *
     * @param rutaArchivo nombre del archivo de donde leer los datos
     */
    private void leerParametrosDeArchivo(String rutaArchivo) {
        try {
            FileReader f = new FileReader(rutaArchivo);
            BufferedReader br = new BufferedReader(f);

            String lineaDemanda = br.readLine();
            String lineaProbDemanda = br.readLine();

            String lineaTiempoEntrega = br.readLine();
            String lineaProbTiempoEntrega = br.readLine();

            String lineaTiempoEspera = br.readLine();
            String lineaProbTiempoEspera = br.readLine();

            String lineaCostoInventario = br.readLine();
            costoDeInventario = Integer.parseInt(lineaCostoInventario);

            String lineaCostoOrden = br.readLine();
            costoDeOrden = Integer.parseInt(lineaCostoOrden);

            String lineaCostoFaltante_espera = br.readLine();
            escasezConEspera = Integer.parseInt(lineaCostoFaltante_espera);

            String lineaCostoFaltante_sinEspera = br.readLine();
            escasezSinEspera = Integer.parseInt(lineaCostoFaltante_sinEspera);

            String lineaInventarioInicial = br.readLine();
            inventarioInicial = Integer.parseInt(lineaInventarioInicial);

            String lineaDiasSimulacion = br.readLine();
            diasSimulacion = Integer.parseInt(lineaDiasSimulacion);

            String[] demanda = lineaDemanda.split("\\|");
            String[] probDemanda = lineaProbDemanda.split("\\|");

            String[] tiempoEntrega = lineaTiempoEntrega.split("\\|");
            String[] probTiempoEntrega = lineaProbTiempoEntrega.split("\\|");

            String[] tiempoEspera = lineaTiempoEspera.split("\\|");
            String[] probTiempoEspera = lineaProbTiempoEspera.split("\\|");

            tablaDemanda = crearTabla(demanda, probDemanda);
            tablaEntregas = crearTabla(tiempoEntrega, probTiempoEntrega);
            tablaEsperas = crearTabla(tiempoEspera, probTiempoEspera);

        } catch (IOException ex) {
            System.out.println("Error leyendo archivo de parametros");
        }
    }

    /**
     * Calcula q* (la cantidad optima por pedido, segun los costos y demanda dada)
     * ______________
     * /   2KD(h+s)
     * q* = \  / ---------------
     * \/         hs
     *
     * @param k costo por pedido
     * @param d demanda
     * @param h costo de inventario
     * @param s costo de escasez (faltante)
     * @return q*
     */
    int calcularQAsterisco(int k, int d, int h, int s) {
        return (int) Math.sqrt((2 * k * d * (h + s)) / (h * s));
    }


    /**
     * Crea y retorna una Tabla con los valores y probabilidades dados. Se encarga de parsear los valores y
     * probabilidades, crear las listas y devolver la tabla creada.
     *
     * @param valores        los valores para la tabla
     * @param probabilidades las probabilidades para la tabla
     * @return la tabla creada con los valores y probabilidades
     */
    private DistribucionProbabilidad crearTabla(String[] valores, String[] probabilidades) {
        ArrayList<Integer> valoresTabla = new ArrayList<>();
        ArrayList<Float> probabilidadesTabla = new ArrayList<>();

        for (String valor : valores) {
            valoresTabla.add(Integer.parseInt(valor));
        }

        for (String probabilidad : probabilidades) {
            probabilidadesTabla.add(Float.parseFloat(probabilidad));
        }

        // TODO: verificar que la suma de las probabilidades sea igual a 1, y retornar error si no es asi
        return new DistribucionProbabilidad(valoresTabla, probabilidadesTabla);
    }


    /**
     * Inserta una nueva fila en la tabla de eventos
     *
     * @param inventario la fila a insertar
     */
    void insertarFila(Inventario inventario) {
        inventarios.add(inventario);
    }

    /**
     * Muestra los datos de una tabla por pantalla. Se usa para probar que los datos esten bien
     *
     * @param tabla la tabla que se va a mostrar
     */
    void mostrarTabla(DistribucionProbabilidad tabla) {
        for (int i = 0; i < tabla.valores.size(); i++)
            System.out.printf("%d| ", tabla.valores.get(i));
        System.out.println();
        for (int i = 0; i < tabla.probabilidades.size(); i++)
            System.out.printf("%.2f| ", tabla.probabilidades.get(i));
        System.out.println("\n");
    }


    /**
     * Crea las mismas tablas del ejemplo dado en la asignacion del proyecto
     */
    private void crearTablasDePrueba() {
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

        // Se crea la tabla de demanda
        tablaDemanda = new DistribucionProbabilidad(valoresDemanda, probabilidadesDemanda);

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

        tablaEntregas = new DistribucionProbabilidad(valoresEntrega, probabilidadesEntrega);

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

        tablaEsperas = new DistribucionProbabilidad(valoresEspera, probabilidadesEspera);
    }
}
