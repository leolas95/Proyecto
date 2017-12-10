package proyecto;

import java.util.ArrayList;

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
}
