package proyecto;

import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static proyecto.Main.stage;

public class ControladorSimulacion implements Initializable {

    // Declaramos los text utilizables
    @FXML
    public Text limitesQText;
    @FXML
    public Text limitesRText;
    @FXML
    public Text costoFaltanteText;
    @FXML
    public Text costoOrdenText;
    @FXML
    public Text costoInventarioText;
    @FXML
    public Text costoTotalText;
    @FXML
    public Text qMinimaText;
    @FXML
    public Text rMinimaText;

    // Declaramos los spinners utilizables
    @FXML
    public Spinner<Integer> valorQSpinner = new Spinner<>();
    @FXML
    public Spinner<Integer> valorRSpinner = new Spinner<>();

    // Declaramos la tabla y las columnas
    @FXML
    private TableView<Inventario> inventarioTV;
    @FXML
    private TableColumn diaTC;
    @FXML
    private TableColumn invInicioTC;
    @FXML
    private TableColumn aleatorioDemandaTC;
    @FXML
    private TableColumn DemandaTC;
    @FXML
    private TableColumn invFinalTC;
    @FXML
    private TableColumn invPromTC;
    @FXML
    private TableColumn FaltanteTC;
    @FXML
    private TableColumn noOrdenTC;
    @FXML
    private TableColumn aleatorioEntregaTC;
    @FXML
    private TableColumn tiempoEntregaTC;
    @FXML
    private TableColumn aleatorioEsperaTC;
    @FXML
    private TableColumn tiempoEsperaTC;

    private ObservableList<Inventario> inventarios;

    // Datos para cada costo minimo segun cada valor de Q. Para el grafico de lineas
    private ArrayList<XYChart.Data<Number, Number>> costoMinimoEnFuncionDeQ = new ArrayList<>();

    // Datos para los todos costos asociados al costo minimo. Para el primer grafico de torta
    private ArrayList<PieChart.Data> costosDelCostoMinimo = new ArrayList<>();
    // Datos para los costos de faltante con y sin espera asociados al costo de faltante minimo. Para el segundo grafico de torta
    private ArrayList<PieChart.Data> costosDelFaltante = new ArrayList<>();

    // Datos para los costos asociados a cada valor de Q. Para el grafico de barras
    private ArrayList<XYChart.Data<String, Number>> costoInventarioPorCadaQ = new ArrayList<>();
    private ArrayList<XYChart.Data<String, Number>> costoOrdenarPorCadaQ = new ArrayList<>();
    private ArrayList<XYChart.Data<String, Number>> costoFaltantePorCadaQ = new ArrayList<>();

    private Simulacion simulacion;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        inventarioTV.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        //Se inicializan los parametros con los valores que recibe ControladorParametrosManual del archivo
        DistribucionProbabilidad tablaDemanda = ControladorParametrosManual.tablaDemanda;
        DistribucionProbabilidad tablaEntregas = ControladorParametrosManual.tablaEntregas;
        DistribucionProbabilidad tablaEsperas = ControladorParametrosManual.tablaEsperas;

        int costoDeOrden = ControladorParametrosManual.costoDeOrden;
        int costoDeInventario = ControladorParametrosManual.costoDeInventario;
        int escasezConEspera = ControladorParametrosManual.escasezConEspera;
        int escasezSinEspera = ControladorParametrosManual.escasezSinEspera;
        int inventarioInicial = ControladorParametrosManual.inventarioInicial;
        int diasSimulacion = ControladorParametrosManual.diasSimulacion;

        //Se calcula Q minimo y Q max
        int qmin = calcularQAsterisco(costoDeOrden, tablaDemanda.obtenerValorMinimo(), costoDeInventario, escasezSinEspera);
        int qmax = calcularQAsterisco(costoDeOrden, tablaDemanda.obtenerValorMaximo(), costoDeInventario, escasezConEspera);

        //Se calcula R minimo y R maximo
        int rmin = calcularPuntoDeReorden(tablaDemanda.obtenerValorMinimo(), diasSimulacion, qmin, tablaEntregas.obtenerValorMinimo());
        int rmax = calcularPuntoDeReorden(tablaDemanda.obtenerValorMaximo(), diasSimulacion, qmax, tablaEntregas.obtenerValorMaximo());

        inicializarSpinners(qmin, qmax, rmin, rmax);

        // Muestra o no la tabla de eventos, segun lo que quiso el usuario
        inventarioTV.setManaged(ControladorParametrosManual.tablaEventos);

        // Se crea la simulacion con los datos que no variaran: las tablas y estos costos con constantes
        simulacion = new Simulacion(tablaDemanda, tablaEntregas, tablaEsperas, diasSimulacion,
                costoDeInventario, costoDeOrden, escasezConEspera,
                escasezSinEspera, inventarioInicial,
                this);

        inicializarListeners();

        for (int qactual = qmin; qactual <= qmax; qactual++) {
            for (int ractual = rmin; ractual <= rmax; ractual++) {
                valorQSpinner.getValueFactory().setValue(qactual);
                valorRSpinner.getValueFactory().setValue(ractual);
                simulacion.ejecutar(qactual, ractual);
            }
            costoMinimoEnFuncionDeQ.add(new XYChart.Data<>(qactual, simulacion.getCostoTotalMinimo()));

            costoInventarioPorCadaQ.add(new XYChart.Data<>(String.valueOf(qactual), simulacion.get_costoInventario()));
            costoOrdenarPorCadaQ.add(new XYChart.Data<>(String.valueOf(qactual), simulacion.get_costoOrdenar()));
            costoFaltantePorCadaQ.add(new XYChart.Data<>(String.valueOf(qactual), simulacion.get_costoFaltante()));
        }

        costosDelCostoMinimo.add(new PieChart.Data("Costo faltante", simulacion.getCostoFaltanteMinimo()));
        costosDelCostoMinimo.add(new PieChart.Data("Costo orden", simulacion.getCostoDeOrdenMinimo()));
        costosDelCostoMinimo.add(new PieChart.Data("Costo inventario", simulacion.getCostoDeInventarioMinimo()));

        costosDelFaltante.add(new PieChart.Data("Faltante con espera", simulacion.getCostoFaltanteConEspera()));
        costosDelFaltante.add(new PieChart.Data("Faltante sin espera", simulacion.getCostoFaltanteSinEspera()));
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
    private int calcularQAsterisco(int k, int d, int h, int s) {
        return (int) Math.sqrt((2 * k * d * (h + s)) / (h * s));
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

    /* Regresar al menu principal */
    @FXML
    private void atras() {
        try {
            Parent mostrarMenu = FXMLLoader.load(getClass().getResource("ParametrosManual.fxml"));
            Scene scene = new Scene(mostrarMenu, Main.ANCHO_VENTANA, Main.ALTURA_VENTANA);
            Main.stage.setScene(scene);

            Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
            stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void mostrarGraficos() {
        Stage stage = new Stage();
        stage.setScene(new Scene(crearParrilaDeGraficos(800, 600)));
        stage.show();
    }

    private Pane crearParrilaDeGraficos(double width, double height) {
        GridPane grid = new GridPane();
        grid.setStyle("-fx-background-color: ivory;");

        // Crea los graficos de torta

        ObservableList<PieChart.Data> observableList = FXCollections.observableArrayList(costosDelCostoMinimo);
        // Grafico cuanto representa cada costo en el costo minimo
        final PieChart graficoTortaCostosTotales = new PieChart(observableList);
        graficoTortaCostosTotales.setTitle("Costos relacionados al costo mínimo");

        ObservableList<PieChart.Data> observableList1 = FXCollections.observableArrayList(costosDelFaltante);

        // Grafico que representa que tanto del costo por faltante minimo ocupan el costo sin espera y sin espera
        final PieChart graficoTortaCostosFaltante = new PieChart(observableList1);
        graficoTortaCostosFaltante.setTitle("Costos con y sin espera de cliente");

        // Grafico que representa, para cada valor de Q, los costos de inventario, ordenar y faltante
        final BarChart<String, Number> graficoBarrasCostos = crearGraficoBarras();
        graficoBarrasCostos.setTitle("Costos asociados a cada valor de Q");

        // Agrega los graficos al grid
        grid.addRow(0,
                setChartGridPos(Pos.TOP_LEFT, crearGraficoLineas(costoMinimoEnFuncionDeQ
                )),
                setChartGridPos(Pos.TOP_RIGHT, graficoTortaCostosTotales)
        );
        grid.addRow(1,
                setChartGridPos(Pos.BOTTOM_LEFT, graficoTortaCostosFaltante),
                setChartGridPos(Pos.BOTTOM_RIGHT, graficoBarrasCostos)
        );

        grid.setMinHeight(Pane.USE_PREF_SIZE);
        grid.setPrefSize(width, height);
        grid.setMaxHeight(Pane.USE_PREF_SIZE);

        for (final Node node : grid.getChildren()) {
            final Chart chart = (Chart) node;
            chart.setMinHeight(Pane.USE_PREF_SIZE);
            chart.setPrefSize(width / 2, height / 2);
            chart.setMaxHeight(Pane.USE_PREF_SIZE);
        }

        return grid;
    }

    private Chart setChartGridPos(final Pos pos, final Chart chart) {
        final BooleanProperty enlarged = new SimpleBooleanProperty(false);

        final TranslateTransition toCenter = new TranslateTransition(Duration.millis(250));
        final ScaleTransition grow = new ScaleTransition(Duration.millis(250), chart);
        grow.setFromX(1);
        grow.setFromY(1);
        grow.setToX(2);
        grow.setToY(2);

        final SequentialTransition explode = new SequentialTransition(chart, toCenter, grow);
        explode.setOnFinished(t -> chart.getParent().setMouseTransparent(false));

        final TranslateTransition fromCenter = new TranslateTransition(Duration.millis(250));
        final ScaleTransition shrink = new ScaleTransition(Duration.millis(250), chart);
        shrink.setFromX(2);
        shrink.setFromY(2);
        shrink.setToX(1);
        shrink.setToY(1);

        final SequentialTransition implode = new SequentialTransition(chart, shrink, fromCenter);
        implode.setOnFinished(t -> chart.getParent().setMouseTransparent(false));

        chart.setOnMouseClicked(t -> {
            chart.getParent().setMouseTransparent(true);
            chart.toFront();

            if (!enlarged.get()) {
                toCenter.setByX(
                        chart.getWidth() / 2 *
                                ((pos == Pos.TOP_RIGHT || pos == Pos.BOTTOM_RIGHT) ? -1 : 1)
                );
                toCenter.setByY(
                        chart.getHeight() / 2 *
                                ((pos == Pos.BOTTOM_LEFT || pos == Pos.BOTTOM_RIGHT) ? -1 : 1)
                );
                explode.play();
            } else {
                fromCenter.setByX(
                        chart.getWidth() / 2 *
                                ((pos == Pos.TOP_RIGHT || pos == Pos.BOTTOM_RIGHT) ? 1 : -1)
                );
                fromCenter.setByY(
                        chart.getHeight() / 2 *
                                ((pos == Pos.BOTTOM_LEFT || pos == Pos.BOTTOM_RIGHT) ? 1 : -1)
                );
                implode.play();
            }

            enlarged.set(!enlarged.get());
        });

        switch (pos) {
            case TOP_LEFT:
                chart.setStyle("-fx-background-color: lemonchiffon");
                break;
            case TOP_RIGHT:
                chart.setStyle("-fx-background-color: aliceblue");
                break;
            case BOTTOM_LEFT:
                chart.setStyle("-fx-background-color: aliceblue");
                break;
            case BOTTOM_RIGHT:
                chart.setStyle("-fx-background-color: lemonchiffon");
                break;
        }

        return chart;
    }

    private LineChart<Number, Number> crearGraficoLineas(ArrayList<XYChart.Data<Number, Number>> datos) {
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();

        xAxis.setLabel("Q");
        yAxis.setLabel("Costo minimo");

        final LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Variacion del costo minimo segun Q");

        XYChart.Series<Number, Number> series = new XYChart.Series<>();

        series.setName("Q");
        series.getData().addAll(datos);
        lineChart.getData().add(series);

        return lineChart;
    }

    private BarChart<String, Number> crearGraficoBarras() {

        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);

        XYChart.Series<String, Number> serieCostoInventario = new XYChart.Series<>();
        serieCostoInventario.setName("Costo de inventario");
        for (XYChart.Data<String, Number> data : costoInventarioPorCadaQ) {
            serieCostoInventario.getData().add(data);
        }

        XYChart.Series<String, Number> serieCostoOrdenar = new XYChart.Series<>();
        serieCostoOrdenar.setName("Costo de ordenar");
        for (XYChart.Data<String, Number> data : costoOrdenarPorCadaQ) {
            serieCostoOrdenar.getData().add(data);
        }

        XYChart.Series<String, Number> serieCostoFaltante = new XYChart.Series<>();
        serieCostoFaltante.setName("Costo por faltante");
        for (XYChart.Data<String, Number> data : costoFaltantePorCadaQ) {
            serieCostoFaltante.getData().add(data);
        }

        barChart.getData().addAll(serieCostoInventario, serieCostoOrdenar, serieCostoFaltante);

        return barChart;
    }
}
