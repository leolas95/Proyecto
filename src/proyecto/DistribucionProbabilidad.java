package proyecto;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Representa las tablas de demanda, tiempos de entrega y tiempos de espera
 */
public class DistribucionProbabilidad {
    // Los valores de la tabla (en el caso de la demanda de ejemplo, seria 25, 26, 27, etc...)
    ArrayList<Integer> valores;

    // Guarda la probabilidad asociada a cada valor
    ArrayList<Float> probabilidades;

    // Guarda las probabilidades acumuladas en cada casilla de la tabla. Se usa para obtenerValor()
    ArrayList<Float> probabilidadesAcumuladas;


    public DistribucionProbabilidad(ArrayList<Integer> valores, ArrayList<Float> probabilidades) {
        this.valores = valores;
        this.probabilidades = probabilidades;
        this.probabilidadesAcumuladas = new ArrayList<>(probabilidades.size());
        llenarProbabilidadesAcumuladas();
    }

    private void llenarProbabilidadesAcumuladas() {
        float acumulado = 0.0F;
        for (Float probabilidad : probabilidades) {
            acumulado += probabilidad;
            probabilidadesAcumuladas.add(acumulado);
        }
    }

    public float frecuenciaAcumulada() {
        float acumulado = 0;
        for (Float probabilidad : probabilidades) acumulado += probabilidad;
        return acumulado;
    }

    public boolean frecuenciaAcumuladaEsUno() {
        float frecuenciaAcumulada = frecuenciaAcumulada();
        float epsilon = 0.000001F;
        return Math.abs(frecuenciaAcumulada - 1.0F) < epsilon;
    }

    public int obtenerValorMinimo() {
        return Collections.min(valores);
    }

    public int obtenerValorMaximo() {
        return Collections.max(valores);
    }

    /**
     * Obtiene el valor (demanda, tiempo de entrega, etc) de la tabla asociado al numero n
     * @param n numero aleatorio usado para obtener el valor de la distribucion
     * @return el valor de la tabla asociado a n, o -1 si hay error
     */
    public int obtenerValor(int n) {
        int limiteInferior = 0;
        int limiteSuperior;

        for (int i = 0; i < probabilidadesAcumuladas.size(); i++) {
            limiteSuperior = ((int) (probabilidadesAcumuladas.get(i) * 100)) - 1;

            if (n >= limiteInferior && n <= limiteSuperior)
                return valores.get(i);
            limiteInferior = limiteSuperior + 1;
        }
        return -1;
    }

    /**
     * Crea y retorna una Tabla con los valores y probabilidades dados. Se encarga de parsear los valores y
     * probabilidades, crear las listas y devolver la tabla creada.
     *
     * @param valores        los valores para la tabla
     * @param probabilidades las probabilidades para la tabla
     * @return la tabla creada con los valores y probabilidades, o null si la suma de las probabilidades no es igual a 1
     */
    static DistribucionProbabilidad crearTabla(String[] valores, String[] probabilidades) {
        ArrayList<Integer> valoresTabla = new ArrayList<>();
        ArrayList<Float> probabilidadesTabla = new ArrayList<>();

        for (String valor : valores) {
            valoresTabla.add(Integer.parseInt(valor));
        }

        for (String probabilidad : probabilidades) {
            probabilidadesTabla.add(Float.parseFloat(probabilidad));
        }

        DistribucionProbabilidad distribucionProbabilidad = new DistribucionProbabilidad(valoresTabla, probabilidadesTabla);

        // Retorna la tabla si la suma de las probabilidades es 1, null sino
        return (distribucionProbabilidad.frecuenciaAcumuladaEsUno() ? distribucionProbabilidad : null);
    }
}
