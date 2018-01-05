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

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static proyecto.Main.stage;

public class ControladorParametrosManual implements Initializable {

    public static DistribucionProbabilidad tablaDemanda;
    public static DistribucionProbabilidad tablaEntregas;
    public static DistribucionProbabilidad tablaEsperas;

    public static int costoDeOrden, costoDeInventario, escasezConEspera, escasezSinEspera, inventarioInicial, diasSimulacion;

    private Alert alerta;

    @FXML private TextField demandaDiaria_tf;
    @FXML private TextField probDemanda_tf;
    @FXML private TextField tiempoEntrega_tf;
    @FXML private TextField probEntrega_tf;
    @FXML private TextField tiempoEspera_tf;
    @FXML private TextField probEspera_tf;

    @FXML private TextField dias_tf;
    @FXML private TextField inventarioIni_tf;
    @FXML private TextField costoInventario_tf;
    @FXML private TextField costoOrdenar_tf;
    @FXML private TextField faltante1_tf;
    @FXML private TextField faltante2_tf;

    @FXML public CheckBox tablaEventosCB;

    @FXML private Button guardarBT;

    public static boolean tablaEventos = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(ControladorParametrosArchivo.archivoCargado){
            guardarBT.setManaged(true);
            String tablaDemandaString = String.valueOf(String.valueOf(ControladorParametrosArchivo.tablaDemanda.valores));
            tablaDemandaString = ((tablaDemandaString.replace("[","")).replace("]","")).replace(" ","");
            demandaDiaria_tf.setText(tablaDemandaString);

            String probDemandaString = String.valueOf(ControladorParametrosArchivo.tablaDemanda.probabilidades);
            probDemandaString = ((probDemandaString.replace("[","")).replace("]","")).replace(" ","");
            probDemanda_tf.setText(probDemandaString);

            String tablaEntregasString = String.valueOf(ControladorParametrosArchivo.tablaEntregas.valores);
            tablaEntregasString = ((tablaEntregasString.replace("[","")).replace("]","")).replace(" ","");
            tiempoEntrega_tf.setText(tablaEntregasString);

            String probEntregaString = String.valueOf(ControladorParametrosArchivo.tablaEntregas.probabilidades);
            probEntregaString = ((probEntregaString.replace("[","")).replace("]","")).replace(" ","");
            probEntrega_tf.setText(probEntregaString);

            String tablaEsperasString = String.valueOf(String.valueOf(ControladorParametrosArchivo.tablaEsperas.valores));
            tablaEsperasString = ((tablaEsperasString.replace("[","")).replace("]","")).replace(" ","");
            tiempoEspera_tf.setText(tablaEsperasString);

            String probEsperaString = String.valueOf(ControladorParametrosArchivo.tablaEsperas.probabilidades);
            probEsperaString = ((probEsperaString.replace("[","")).replace("]","")).replace(" ","");
            probEspera_tf.setText(probEsperaString);

            dias_tf.setText(String.valueOf(ControladorParametrosArchivo.diasSimulacion));
            inventarioIni_tf.setText(String.valueOf(ControladorParametrosArchivo.inventarioInicial));
            costoInventario_tf.setText(String.valueOf(ControladorParametrosArchivo.costoDeInventario));
            costoOrdenar_tf.setText(String.valueOf(ControladorParametrosArchivo.costoDeOrden));
            faltante1_tf.setText(String.valueOf(ControladorParametrosArchivo.escasezConEspera));
            faltante2_tf.setText(String.valueOf(ControladorParametrosArchivo.escasezSinEspera));
        } else
            guardarBT.setManaged(false);
    }


    public void alerta(){
        alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setHeaderText("ERROR");
    }


    @FXML public void aceptar() {

        if (demandaDiaria_tf.getText().isEmpty()) {
            alerta();
            alerta.setContentText("El campo \"Demanda diaria\" no puede estar vacio. Intente nuevamente.");
            alerta.show();
            return;
        }
        if (probDemanda_tf.getText().isEmpty()) {
            alerta();
            alerta.setContentText("El campo \"Probabilidad (correspondiente a la demanda diaria)\" no puede estar vacio. Intente nuevamente.");
            alerta.show();
            return;
        }
        if (tiempoEntrega_tf.getText().isEmpty()) {
            alerta();
            alerta.setContentText("El campo \"Tiempo entrega\" no puede estar vacio. Intente nuevamente.");
            alerta.show();
            return;
        }
        if (probEntrega_tf.getText().isEmpty()) {
            alerta();
            alerta.setContentText("El campo \"Probabilidad (correspondiente al tiempo de entrega)\" no puede estar vacio. Intente nuevamente.");
            alerta.show();
            return;
        }
        if (tiempoEspera_tf.getText().isEmpty()) {
            alerta();
            alerta.setContentText("El campo \"Tiempo espera\" no puede estar vacio. Intente nuevamente.");
            alerta.show();
            return;
        }
        if (probEspera_tf.getText().isEmpty()) {
            alerta();
            alerta.setContentText("El campo \"Probabilidad (correspondiente al tiempo de espera)\" no puede estar vacio" + ". Intente nuevamente.");
            alerta.show();
            return;
        }

        if (dias_tf.getText().isEmpty()) {
            alerta();
            alerta.setContentText("El campo \"Días a simular\" no puede estar vacio. Intente nuevamente.");
            alerta.show();
            return;
        }
        if (inventarioIni_tf.getText().isEmpty()) {
            alerta();
            alerta.setContentText("El campo \"Inventario inicial\" no puede estar vacio. Intente nuevamente.");
            alerta.show();
            return;
        }
        if (costoInventario_tf.getText().isEmpty()) {
            alerta();
            alerta.setContentText("El campo \"Costo de ordenar\" no puede estar vacio. Intente nuevamente.");
            alerta.show();
            return;
        }
        if (costoOrdenar_tf.getText().isEmpty()) {
            alerta();
            alerta.setContentText("El campo \"Costo de inventario\" no puede estar vacio. Intente nuevamente.");
            alerta.show();
            return;
        }
        if (faltante1_tf.getText().isEmpty()) {
            alerta();
            alerta.setContentText("El campo \"Faltante (con espera)\" no puede estar vacio. Intente nuevamente.");
            alerta.show();
            return;
        }
        if (faltante2_tf.getText().isEmpty()) {
            alerta();
            alerta.setContentText("El campo \"Faltante (sin espera)\" no puede estar vacio. Intente nuevamente.");
            alerta.show();
            return;
        }


        String[] demanda = demandaDiaria_tf.getText().split("\\,");
        String[] probDemanda = probDemanda_tf.getText().split("\\,");

        String[] tiempoEntrega = tiempoEntrega_tf.getText().split("\\,");
        String[] probTiempoEntrega = probEntrega_tf.getText().split("\\,");

        String[] tiempoEspera = tiempoEspera_tf.getText().split("\\,");
        String[] probTiempoEspera = probEspera_tf.getText().split("\\,");

        tablaDemanda = crearTabla(demanda, probDemanda);
        tablaEntregas = crearTabla(tiempoEntrega, probTiempoEntrega);
        tablaEsperas = crearTabla(tiempoEspera, probTiempoEspera);

        costoDeOrden = Integer.parseInt(costoOrdenar_tf.getText());
        costoDeInventario = Integer.parseInt(costoInventario_tf.getText());
        escasezConEspera = Integer.parseInt(faltante1_tf.getText());
        escasezSinEspera = Integer.parseInt(faltante2_tf.getText());
        inventarioInicial = Integer.parseInt(inventarioIni_tf.getText());
        diasSimulacion = Integer.parseInt(dias_tf.getText());

        System.out.println("PARAMETROS DE ENTRADA:");
        System.out.println("tablaDemanda : "+tablaDemanda.valores+"\nprobDemanda: "+tablaDemanda.probabilidades);
        System.out.println("tablaEntregas : "+tablaEntregas.valores+"\nprobDemanda: "+tablaEntregas.probabilidades);
        System.out.println("tablaEsperas : "+tablaEsperas.valores+"\nprobDemanda: "+tablaEntregas.probabilidades);
        System.out.println("costoDeOrden: "+costoDeOrden);
        System.out.println("costoDeInventario: "+costoDeInventario);
        System.out.println("escasezConEspera: "+escasezConEspera);
        System.out.println("escasezSinEspera: "+escasezSinEspera);
        System.out.println("inventarioInicial: "+inventarioInicial);
        System.out.println("diasSimulacion: "+diasSimulacion);

        if(tablaEventosCB.isSelected())
            tablaEventos = true;
        else
            tablaEventos = false;

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

    /* Regresar al menu principal */
    @FXML private void atras() {
        if(ControladorParametrosArchivo.archivoCargado){
            try {
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

    //TODO: validar datos de los TextField
    //guardar: permite guardar los parametros de los TextField en un archivo nuevo o sobreescribir uno existente
    @FXML private void guardar(){
        if (demandaDiaria_tf.getText().isEmpty()) {
            alerta();
            alerta.setContentText("El campo \"Demanda diaria\" no puede estar vacio. Intente nuevamente.");
            alerta.show();
            return;
        }
        if (probDemanda_tf.getText().isEmpty()) {
            alerta();
            alerta.setContentText("El campo \"Probabilidad (correspondiente a la demanda diaria)\" no puede estar vacio. Intente nuevamente.");
            alerta.show();
            return;
        }
        if (tiempoEntrega_tf.getText().isEmpty()) {
            alerta();
            alerta.setContentText("El campo \"Tiempo entrega\" no puede estar vacio. Intente nuevamente.");
            alerta.show();
            return;
        }
        if (probEntrega_tf.getText().isEmpty()) {
            alerta();
            alerta.setContentText("El campo \"Probabilidad (correspondiente al tiempo de entrega)\" no puede estar vacio. Intente nuevamente.");
            alerta.show();
            return;
        }
        if (tiempoEspera_tf.getText().isEmpty()) {
            alerta();
            alerta.setContentText("El campo \"Tiempo espera\" no puede estar vacio. Intente nuevamente.");
            alerta.show();
            return;
        }
        if (probEspera_tf.getText().isEmpty()) {
            alerta();
            alerta.setContentText("El campo \"Probabilidad (correspondiente al tiempo de espera)\" no puede estar vacio" + ". Intente nuevamente.");
            alerta.show();
            return;
        }

        if (dias_tf.getText().isEmpty()) {
            alerta();
            alerta.setContentText("El campo \"Días a simular\" no puede estar vacio. Intente nuevamente.");
            alerta.show();
            return;
        }
        if (inventarioIni_tf.getText().isEmpty()) {
            alerta();
            alerta.setContentText("El campo \"Inventario inicial\" no puede estar vacio. Intente nuevamente.");
            alerta.show();
            return;
        }
        if (costoInventario_tf.getText().isEmpty()) {
            alerta();
            alerta.setContentText("El campo \"Costo de ordenar\" no puede estar vacio. Intente nuevamente.");
            alerta.show();
            return;
        }
        if (costoOrdenar_tf.getText().isEmpty()) {
            alerta();
            alerta.setContentText("El campo \"Costo de inventario\" no puede estar vacio. Intente nuevamente.");
            alerta.show();
            return;
        }
        if (faltante1_tf.getText().isEmpty()) {
            alerta();
            alerta.setContentText("El campo \"Faltante (con espera)\" no puede estar vacio. Intente nuevamente.");
            alerta.show();
            return;
        }
        if (faltante2_tf.getText().isEmpty()) {
            alerta();
            alerta.setContentText("El campo \"Faltante (sin espera)\" no puede estar vacio. Intente nuevamente.");
            alerta.show();
            return;
        }


        if(ControladorParametrosArchivo.selectedFile == null){
            alerta();
            alerta.setContentText("EASTER EGG!");
            alerta.show();
            return;
        } else {
            try {
                FileWriter file = new FileWriter(ControladorParametrosArchivo.selectedFile);
                BufferedWriter fileBW = new BufferedWriter(file);

                fileBW.write(demandaDiaria_tf.getText().replace(",","|"));
                fileBW.newLine();
                fileBW.write(probDemanda_tf.getText().replace(",","|"));
                fileBW.newLine();
                fileBW.write(tiempoEntrega_tf.getText().replace(",","|"));
                fileBW.newLine();
                fileBW.write(probEntrega_tf.getText().replace(",","|"));
                fileBW.newLine();
                fileBW.write(tiempoEspera_tf.getText().replace(",","|"));
                fileBW.newLine();
                fileBW.write(probEspera_tf.getText().replace(",","|"));
                fileBW.newLine();
                fileBW.write("h="+costoInventario_tf.getText());
                fileBW.newLine();
                fileBW.write("k="+costoOrdenar_tf.getText());
                fileBW.newLine();
                fileBW.write("fce="+faltante1_tf.getText());
                fileBW.newLine();
                fileBW.write("fse="+faltante2_tf.getText());
                fileBW.newLine();
                fileBW.write("ii="+inventarioIni_tf.getText());
                fileBW.newLine();
                fileBW.write("dds="+dias_tf.getText());

                fileBW.close();
                file.close();


                alerta = new Alert(Alert.AlertType.INFORMATION);
                alerta.setContentText("Archivo guardado!");
                alerta.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


}