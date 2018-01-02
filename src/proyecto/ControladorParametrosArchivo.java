package proyecto;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class ControladorParametrosArchivo {

    @FXML private ListView listView;

    private Alert alerta;

    private DistribucionProbabilidad tablaDemanda;
    private DistribucionProbabilidad tablaEntregas;
    private DistribucionProbabilidad tablaEsperas;

    private int costoDeOrden, costoDeInventario, escasezConEspera, escasezSinEspera, inventarioInicial, diasSimulacion;

    // Para el boton "ATRAS" -> Regresar al menu principal
    @FXML private void atras() {
        try {
            Parent mostrarMenu = FXMLLoader.load(getClass().getResource("Menu.fxml"));
            Scene scene = new Scene(mostrarMenu, Main.ANCHO_VENTANA, Main.ALTURA_VENTANA);
            Main.stage.setScene(scene);
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
        FileChooser fileChooser = new FileChooser();

        // Para seleccionar solamente archivos de texto
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("TEXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        // Abrir
        File selectedFile = fileChooser.showOpenDialog(null);

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

            /* TODO: verificar que la frecuencia acumualada sea igual a uno. Puede ser con frecuenciaAcumulada()
             * o frecuenciaAcumuladaEsUno() de la clase DistribucionProbabilidad */
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