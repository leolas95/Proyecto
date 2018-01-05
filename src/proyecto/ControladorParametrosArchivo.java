package proyecto;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;
import javafx.stage.Screen;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import static proyecto.Main.stage;

public class ControladorParametrosArchivo {

    @FXML private ListView listView;

    private Alert alerta;

    public static DistribucionProbabilidad tablaDemanda;
    public static DistribucionProbabilidad tablaEntregas;
    public static DistribucionProbabilidad tablaEsperas;

    public static int costoDeOrden, costoDeInventario, escasezConEspera, escasezSinEspera, inventarioInicial, diasSimulacion;

    public static boolean archivoCargado = false;
    public static int archivoErroneo = 0;

    public static FileChooser fileChooser;
    public static File selectedFile;

    //Para el boton "ACEPTAR" -> Va hacia el controladorParametrosManual con los datos del archivo
    @FXML private void aceptar(){
        try{
            if(selectedFile==null){
                alerta = new Alert(Alert.AlertType.ERROR);
                alerta.setHeaderText("ERROR");
                alerta.setContentText("Archivo no seleccionado!");
                alerta.show();
                return;
            }

            if(archivoErroneo==1){
                alerta = new Alert(Alert.AlertType.ERROR);
                alerta.setHeaderText("ERROR");
                alerta.setContentText("En la tabla de -distribucion de demanda- la suma de de la frecuencia no es igual a 1");
                alerta.show();
                return;
            }
            if(archivoErroneo==2){
                alerta = new Alert(Alert.AlertType.ERROR);
                alerta.setHeaderText("ERROR");
                alerta.setContentText("En la tabla de -distribucion de tiempos de entrega- la suma de de la frecuencia no es igual a 1");
                alerta.show();
                return;
            }
            if(archivoErroneo==3){
                alerta = new Alert(Alert.AlertType.ERROR);
                alerta.setHeaderText("ERROR");
                alerta.setContentText("En la tabla de -distribucion de tiempos de esperas- la suma de de la frecuencia no es igual a 1");
                alerta.show();
                return;
            }

            archivoCargado = true;
            Parent mostrarMenu = FXMLLoader.load(getClass().getResource("ParametrosManual.fxml"));
            Scene scene = new Scene(mostrarMenu, Main.ANCHO_VENTANA, Main.ALTURA_VENTANA);
            Main.stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Para el boton "ATRAS" -> Regresar al menu principal
    @FXML private void atras() {
        try {
            selectedFile = null;
            archivoCargado = false;
            Parent mostrarMenu = FXMLLoader.load(getClass().getResource("Menu.fxml"));
            Scene scene = new Scene(mostrarMenu, Main.ANCHO_VENTANA, Main.ALTURA_VENTANA);
            Main.stage.setScene(scene);

            Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
            stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Para el boton "INSTRUCCIONES" -> Ir a las instrucciones
    @FXML private void instrucciones() {
        try {
            Parent mostrarInstrucciones = FXMLLoader.load(getClass().getResource("Instrucciones.fxml"));
            Scene scene = new Scene(mostrarInstrucciones, Main.ANCHO_VENTANA, Main.ALTURA_VENTANA);
            Main.stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Para el boton "Seleccionar archivo" -> solo acepta archivos de texto y verifica si este cumple con el formato establecido
    public void seleccionarArchivoBtn() throws IOException {
        fileChooser = new FileChooser();

        // Para seleccionar solamente archivos de texto
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TEXT files (*.txt)", "*.txt");

        fileChooser.getExtensionFilters().add(extFilter);

        // Abrir
        selectedFile = fileChooser.showOpenDialog(null);

        if(selectedFile==null){
            System.out.println("Archivo no seleccionado\n");
            return;
        }

        // Verificar que el archivo cumpla con los parámetros (si es que no esta vacio)
        if (selectedFile.length() == 0) {
            alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setHeaderText("ERROR");
            alerta.setContentText("Ups! este archivo no tiene ningún parámetro. Inténtelo de nuevo.\"");
            alerta.show();
            return;
        } else if (selectedFile != null) {
            listView.getItems().add(selectedFile.getName());

            FileReader f = new FileReader(selectedFile);
            BufferedReader br = new BufferedReader(f);

            // Lee las tablas
            String lineaDemanda = br.readLine();
            String lineaProbDemanda = br.readLine();

            String lineaTiempoEntrega = br.readLine();
            String lineaProbTiempoEntrega = br.readLine();

            String lineaTiempoEspera = br.readLine();
            String lineaProbTiempoEspera = br.readLine();

            Iterator<String> iterator = br.lines().iterator();
            while (iterator.hasNext()) {
                String lineaActual;

                // Ignora lineas vacias
                if ((lineaActual = iterator.next()).isEmpty())
                    continue;

                // Separa lo que esta antes y despues del igual. Antes esta el nombre del parametro, y despues su valor
                String[] clavesValores = lineaActual.split("=");

                if (clavesValores.length != 2) {
                    alerta = new Alert(Alert.AlertType.ERROR);
                    alerta.setHeaderText("ERROR");
                    alerta.setContentText("Error al leer el archivo de parametros. Cada linea debe tener la " +
                            "forma \"clave = valor\", si tiene duda con respecto al formato del archivo, consulte " +
                            "las instrucciones.");
                    alerta.show();
                    return;
                }

                // Lee el valor y lo guarda en la variable correspondiente
                switch (clavesValores[0]) {
                    // h: costo de inventario
                    case "h":
                        costoDeInventario = Integer.parseInt(clavesValores[1]);
                        break;
                    // k: costo por pedido/orden
                    case "k":
                        costoDeOrden = Integer.parseInt(clavesValores[1]);
                        break;
                    // fce: costo de faltante con espera de cliente
                    case "fce":
                        escasezConEspera = Integer.parseInt(clavesValores[1]);
                        break;
                    // fse: costo de faltante sin espera de cliente
                    case "fse":
                        escasezSinEspera = Integer.parseInt(clavesValores[1]);
                        break;
                    // ii: inventario inicial
                    case "ii":
                        inventarioInicial = Integer.parseInt(clavesValores[1]);
                        break;
                    // dds: dias de simulacion
                    case "dds":
                        diasSimulacion = Integer.parseInt(clavesValores[1]);
                }
            }

            String[] demanda = lineaDemanda.split("\\|");
            String[] probDemanda = lineaProbDemanda.split("\\|");

            String[] tiempoEntrega = lineaTiempoEntrega.split("\\|");
            String[] probTiempoEntrega = lineaProbTiempoEntrega.split("\\|");

            String[] tiempoEspera = lineaTiempoEspera.split("\\|");
            String[] probTiempoEspera = lineaProbTiempoEspera.split("\\|");

            tablaDemanda = crearTabla(demanda, probDemanda);
            tablaEntregas = crearTabla(tiempoEntrega, probTiempoEntrega);
            tablaEsperas = crearTabla(tiempoEspera, probTiempoEspera);

            if(!tablaDemanda.frecuenciaAcumuladaEsUno()){
                alerta = new Alert(Alert.AlertType.ERROR);
                alerta.setHeaderText("ERROR");
                alerta.setContentText("En la tabla de -distribucion de demanda- la suma de de la frecuencia no es igual a 1");
                alerta.show();
                archivoErroneo = 1;
                return;
            }
            if(!tablaEntregas.frecuenciaAcumuladaEsUno()){
                alerta = new Alert(Alert.AlertType.ERROR);
                alerta.setHeaderText("ERROR");
                alerta.setContentText("En la tabla de -distribucion de tiempos de entrega- la suma de de la frecuencia no es igual a 1");
                alerta.show();
                archivoErroneo = 2;
                return;
            }
            if(!tablaEsperas.frecuenciaAcumuladaEsUno()){
                alerta = new Alert(Alert.AlertType.ERROR);
                alerta.setHeaderText("ERROR");
                alerta.setContentText("En la tabla de -distribucion de tiempos de esperas- la suma de de la frecuencia no es igual a 1");
                alerta.show();
                archivoErroneo = 3;
                return;
            }

            archivoErroneo = 0;

            f.close();
        }

    }

    /**
     * Crea y retorna una Tabla con los valores y probabilidades dados. Se encarga de parsear los valores y
     * probabilidades, crear las listas y devolver la tabla creada.
     *
     * @param valores        los valores para la tabla
     * @param probabilidades las probabilidades para la tabla
     * @return la tabla creada con los valores y probabilidades
     * */
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
}