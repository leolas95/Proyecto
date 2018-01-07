package proyecto;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Screen;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import static proyecto.Main.stage;

public class ControladorParametrosManual implements Initializable {

    static DistribucionProbabilidad tablaDemanda;
    static DistribucionProbabilidad tablaEntregas;
    static DistribucionProbabilidad tablaEsperas;

    static int costoDeOrden, costoDeInventario, escasezConEspera, escasezSinEspera, inventarioInicial, diasSimulacion;

    @FXML
    private TextField demandaDiaria_tf;
    @FXML
    private TextField probDemanda_tf;
    @FXML
    private TextField tiempoEntrega_tf;
    @FXML
    private TextField probEntrega_tf;
    @FXML
    private TextField tiempoEspera_tf;
    @FXML
    private TextField probEspera_tf;

    @FXML
    private TextField dias_tf;
    @FXML
    private TextField inventarioIni_tf;
    @FXML
    private TextField costoInventario_tf;
    @FXML
    private TextField costoOrdenar_tf;
    @FXML
    private TextField faltante1_tf;
    @FXML
    private TextField faltante2_tf;

    @FXML
    public CheckBox tablaEventosCB;

    @FXML
    private Button guardarBT;

    static boolean tablaEventos = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        if (ControladorParametrosArchivo.archivoCargado) {
            guardarBT.setManaged(true);
            String tablaDemandaString = String.valueOf(String.valueOf(ControladorParametrosArchivo.tablaDemanda.valores));
            tablaDemandaString = ((tablaDemandaString.replace("[", "")).replace("]", "")).replace(" ", "");
            demandaDiaria_tf.setText(tablaDemandaString);

            String probDemandaString = String.valueOf(ControladorParametrosArchivo.tablaDemanda.probabilidades);
            probDemandaString = ((probDemandaString.replace("[", "")).replace("]", "")).replace(" ", "");
            probDemanda_tf.setText(probDemandaString);

            String tablaEntregasString = String.valueOf(ControladorParametrosArchivo.tablaEntregas.valores);
            tablaEntregasString = ((tablaEntregasString.replace("[", "")).replace("]", "")).replace(" ", "");
            tiempoEntrega_tf.setText(tablaEntregasString);

            String probEntregaString = String.valueOf(ControladorParametrosArchivo.tablaEntregas.probabilidades);
            probEntregaString = ((probEntregaString.replace("[", "")).replace("]", "")).replace(" ", "");
            probEntrega_tf.setText(probEntregaString);

            String tablaEsperasString = String.valueOf(String.valueOf(ControladorParametrosArchivo.tablaEsperas.valores));
            tablaEsperasString = ((tablaEsperasString.replace("[", "")).replace("]", "")).replace(" ", "");
            tiempoEspera_tf.setText(tablaEsperasString);

            String probEsperaString = String.valueOf(ControladorParametrosArchivo.tablaEsperas.probabilidades);
            probEsperaString = ((probEsperaString.replace("[", "")).replace("]", "")).replace(" ", "");
            probEspera_tf.setText(probEsperaString);

            dias_tf.setText(String.valueOf(ControladorParametrosArchivo.diasSimulacion));
            inventarioIni_tf.setText(String.valueOf(ControladorParametrosArchivo.inventarioInicial));
            costoInventario_tf.setText(String.valueOf(ControladorParametrosArchivo.costoDeInventario));
            costoOrdenar_tf.setText(String.valueOf(ControladorParametrosArchivo.costoDeOrden));
            faltante1_tf.setText(String.valueOf(ControladorParametrosArchivo.escasezConEspera));
            faltante2_tf.setText(String.valueOf(ControladorParametrosArchivo.escasezSinEspera));
        }
    }

    @FXML
    public void aceptar() {

        if (!validarFormatoCampos())
            return;

        String[] demanda = demandaDiaria_tf.getText().split("\\,");
        String[] probDemanda = probDemanda_tf.getText().split("\\,");

        String[] tiempoEntrega = tiempoEntrega_tf.getText().split("\\,");
        String[] probTiempoEntrega = probEntrega_tf.getText().split("\\,");

        String[] tiempoEspera = tiempoEspera_tf.getText().split("\\,");
        String[] probTiempoEspera = probEspera_tf.getText().split("\\,");


        // Verifica que la cantidad de probabilidades sea igual a la de valores
        if (demanda.length != probDemanda.length) {
            alertaError("En la tabla de -distribucion de demanda-, la cantidad de probabilidades es distinta" +
                    " a la cantidad de valores. Cada probabilidad debe estar asignada a un valor de demanda.");
            return;
        }

        if (tiempoEntrega.length != probTiempoEntrega.length) {
            alertaError("En la tabla de -distribucion de tiempos de entrega-, la cantidad de probabilidaes es" +
                    " distinta a la cantidad de valores. Cada probabilidad debe estar asignada a un valor tiempo de entrega");
            return;
        }

        if (tiempoEspera.length != probTiempoEspera.length) {
            alertaError("En la tabla de -distribucion de tiempos de esperas-, la cantidad de probabilidades es" +
                    " distinta a la cantidad de valores. Cada probabilidad debe estar asignada a un valor de tiempo de espera");
            return;
        }

        tablaDemanda = DistribucionProbabilidad.crearTabla(demanda, probDemanda);

        if (tablaDemanda == null) {
            alertaError("En la tabla de -distribucion de demanda- la suma de de la frecuencia no es igual a 1");
            return;
        }

        tablaEntregas = DistribucionProbabilidad.crearTabla(tiempoEntrega, probTiempoEntrega);

        if (tablaDemanda == null) {
            alertaError("En la tabla de -distribucion de tiempos de entrega- la suma de de la frecuencia no es igual a 1");
            return;
        }

        tablaEsperas = DistribucionProbabilidad.crearTabla(tiempoEspera, probTiempoEspera);

        if (tablaEsperas == null) {
            alertaError("En la tabla de -distribucion de tiempos de esperas- la suma de de la frecuencia no es igual a 1");
            return;
        }

        costoDeOrden = Integer.parseInt(costoOrdenar_tf.getText());
        costoDeInventario = Integer.parseInt(costoInventario_tf.getText());
        escasezConEspera = Integer.parseInt(faltante1_tf.getText());
        escasezSinEspera = Integer.parseInt(faltante2_tf.getText());
        inventarioInicial = Integer.parseInt(inventarioIni_tf.getText());
        diasSimulacion = Integer.parseInt(dias_tf.getText());

        System.out.println("PARAMETROS DE ENTRADA:");
        System.out.println("tablaDemanda : " + tablaDemanda.valores + "\nprobDemanda: " + tablaDemanda.probabilidades);
        System.out.println("tablaEntregas : " + tablaEntregas.valores + "\nprobDemanda: " + tablaEntregas.probabilidades);
        System.out.println("tablaEsperas : " + tablaEsperas.valores + "\nprobDemanda: " + tablaEntregas.probabilidades);
        System.out.println("costoDeOrden: " + costoDeOrden);
        System.out.println("costoDeInventario: " + costoDeInventario);
        System.out.println("escasezConEspera: " + escasezConEspera);
        System.out.println("escasezSinEspera: " + escasezSinEspera);
        System.out.println("inventarioInicial: " + inventarioInicial);
        System.out.println("diasSimulacion: " + diasSimulacion);

        tablaEventos = tablaEventosCB.isSelected();

        try {
            Parent mostrarMenu = FXMLLoader.load(getClass().getResource("Interfaz.fxml"));
            Scene scene = new Scene(mostrarMenu);
            stage.setScene(scene);
            Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
            stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean validarFormatoCampos() {

        // Expresion regular para validar que los valores sean solo numeros enteros positivos separados por comas.
        // Les juro por Dios que si funciona.
        Pattern patronValoresTablas = Pattern.compile("((?<!^,)\\d+(,(?!$)|$))+");

        // Para validar que las probabilidades sean numeros decimales positivos separados por coma
        Pattern patronProbabilidadesTablas = Pattern.compile("^([+]?([01])+(\\.\\d+)\\,?)+");

        // Verifica que ningun campo este vacio y que el formato sea el correcto
        if (demandaDiaria_tf.getText().isEmpty() || !patronValoresTablas.matcher(demandaDiaria_tf.getCharacters()).matches()) {
            alertaError("El campo \"Demanda diaria\" no puede estar vacio y solo debe contener numeros positivos" +
                    " separados por una sola coma. Intente nuevamente.");
            return false;
        }

        if (probDemanda_tf.getText().isEmpty() || !patronProbabilidadesTablas.matcher(probDemanda_tf.getCharacters()).matches()) {
            alertaError("El campo \"Probabilidad (correspondiente a la demanda diaria)\" no puede estar vacio " +
                    "y solo debe contener numeros positivos separados por una sola coma. Intente nuevamente.");
            return false;
        }
        if (tiempoEntrega_tf.getText().isEmpty() || !patronValoresTablas.matcher(tiempoEntrega_tf.getCharacters()).matches()) {
            alertaError("El campo \"Tiempo entrega\" no puede estar vacio y solo debe contener numeros " +
                    "positivos separados por una sola coma. Intente nuevamente.");
            return false;
        }
        if (probEntrega_tf.getText().isEmpty() || !patronProbabilidadesTablas.matcher(probEntrega_tf.getCharacters()).matches()) {
            alertaError("El campo \"Probabilidad (correspondiente al tiempo de entrega)\" no puede estar vacio " +
                    "y solo debe contener numeros positivos separados por una sola coma. Intente nuevamente.");
            return false;
        }
        if (tiempoEspera_tf.getText().isEmpty() || !patronValoresTablas.matcher(tiempoEspera_tf.getCharacters()).matches()) {
            alertaError("El campo \"Tiempo espera\" no puede estar vacio y solo debe contener numeros " +
                    "positivos separados por una sola coma. Intente nuevamente.");
            return false;
        }
        if (probEspera_tf.getText().isEmpty() || !patronProbabilidadesTablas.matcher(probEspera_tf.getCharacters()).matches()) {
            alertaError("El campo \"Probabilidad (correspondiente al tiempo de espera)\" no puede estar vacio " +
                    "y solo debe contener numeros positivos separados por una sola coma" + ". Intente nuevamente.");
            return false;
        }

        // Para validar numeros enteros positivos
        Pattern patronSoloNumeros = Pattern.compile("^[+]?\\d+");

        if (dias_tf.getText().isEmpty() || !patronSoloNumeros.matcher(dias_tf.getCharacters()).matches()) {
            alertaError("El campo \"Días a simular\" no puede estar vacio y solo debe contener numeros enteros " +
                    "positivos. Intente nuevamente.");
            return false;
        }

        if (inventarioIni_tf.getText().isEmpty() || !patronSoloNumeros.matcher(inventarioIni_tf.getCharacters()).matches()) {
            alertaError("El campo \"Inventario inicial\" no puede estar vacio y solo debe contener numeros " +
                    "enteros positivos. Intente nuevamente.");
            return false;
        }

        if (costoInventario_tf.getText().isEmpty() || !patronSoloNumeros.matcher(costoInventario_tf.getCharacters()).matches()) {
            alertaError("El campo \"Costo de ordenar\" no puede estar vacio y solo debe contener numeros " +
                    "enteros positivos. Intente nuevamente.");
            return false;
        }
        if (costoOrdenar_tf.getText().isEmpty() || !patronSoloNumeros.matcher(costoOrdenar_tf.getCharacters()).matches()) {
            alertaError("El campo \"Costo de inventario\" no puede estar vacio y solo debe contener numeros " +
                    "enteros positivos. Intente nuevamente.");
            return false;
        }
        if (faltante1_tf.getText().isEmpty() || !patronSoloNumeros.matcher(faltante1_tf.getCharacters()).matches()) {
            alertaError("El campo \"Faltante (con espera)\" no puede estar vacio y solo debe contener numeros " +
                    "enteros positivos. Intente nuevamente.");
            return false;
        }
        if (faltante2_tf.getText().isEmpty() || !patronSoloNumeros.matcher(faltante2_tf.getCharacters()).matches()) {
            alertaError("El campo \"Faltante (sin espera)\" no puede estar vacio y solo debe contener numeros " +
                    "enteros positivos. Intente nuevamente.");
        }

        return true;
    }

    /* Regresar al menu principal */
    @FXML
    private void atras() {
        if (ControladorParametrosArchivo.archivoCargado) {
            try {
                ControladorParametrosArchivo.selectedFile = null;
                limpiarCampos();
                Parent mostrarMenu = FXMLLoader.load(getClass().getResource("ParametrosArchivo.fxml"));
                Scene scene = new Scene(mostrarMenu, Main.ANCHO_VENTANA, Main.ALTURA_VENTANA);
                stage.setScene(scene);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                Parent mostrarMenu = FXMLLoader.load(getClass().getResource("Menu.fxml"));
                Scene scene = new Scene(mostrarMenu, Main.ANCHO_VENTANA, Main.ALTURA_VENTANA);
                stage.setScene(scene);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);

    }

    //guardar: permite guardar los parametros de los TextField en un archivo nuevo o sobreescribir uno existente
    @FXML
    private void guardar() {

        if (!validarFormatoCampos()) {
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar como...");

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TEXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));

        File nuevoArchivo = fileChooser.showSaveDialog(null);

        if (nuevoArchivo != null) {
            guardarParametrosEnArchivo(nuevoArchivo);
        }
    }

    /**
     * Limpia los TextFields
     */
    private void limpiarCampos() {
        demandaDiaria_tf.clear();
        probDemanda_tf.clear();
        tiempoEntrega_tf.clear();
        probEntrega_tf.clear();
        tiempoEspera_tf.clear();
        probEspera_tf.clear();
        dias_tf.clear();
        inventarioIni_tf.clear();
        costoInventario_tf.clear();
        costoOrdenar_tf.clear();
        faltante1_tf.clear();
        faltante2_tf.clear();
        tablaEventosCB.setSelected(false);
    }

    /**
     * Construye y muestra una alerta de error, con el mensaje especificado
     *
     * @param mensaje el mensaje a mostrar
     */
    static void alertaError(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setHeaderText("ERROR");
        alerta.setContentText(mensaje);
        alerta.show();
    }

    /**
     * Guarda los parametros de la simulacion en el archivo especificado
     *
     * @param archivo el archivo en donde se guardaran los parametros
     */
    private void guardarParametrosEnArchivo(File archivo) {
        try {
            FileWriter file = new FileWriter(archivo);
            BufferedWriter fileBW = new BufferedWriter(file);

            fileBW.write(demandaDiaria_tf.getText().replace(",", "|"));
            fileBW.newLine();
            fileBW.write(probDemanda_tf.getText().replace(",", "|"));
            fileBW.newLine();
            fileBW.write(tiempoEntrega_tf.getText().replace(",", "|"));
            fileBW.newLine();
            fileBW.write(probEntrega_tf.getText().replace(",", "|"));
            fileBW.newLine();
            fileBW.write(tiempoEspera_tf.getText().replace(",", "|"));
            fileBW.newLine();
            fileBW.write(probEspera_tf.getText().replace(",", "|"));
            fileBW.newLine();
            fileBW.write("h=" + costoInventario_tf.getText());
            fileBW.newLine();
            fileBW.write("k=" + costoOrdenar_tf.getText());
            fileBW.newLine();
            fileBW.write("fce=" + faltante1_tf.getText());
            fileBW.newLine();
            fileBW.write("fse=" + faltante2_tf.getText());
            fileBW.newLine();
            fileBW.write("ii=" + inventarioIni_tf.getText());
            fileBW.newLine();
            fileBW.write("dds=" + dias_tf.getText());

            fileBW.close();
            file.close();

            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Exito al guardar archivo");
            alerta.setContentText("Archivo guardado con éxito!");
            alerta.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}